package net.ilexiconn.llibrary.server.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.commons.compress.utils.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class RuntimePatcher implements IClassTransformer, Opcodes {
    private Map<String, ClassPatcher> patcherMap = new HashMap<>();

    public abstract void onInit();

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (this.patcherMap.isEmpty()) {
            this.onInit();
        }
        byte[] prev = bytes;
        bytes = this.handlePatches(bytes, MappingHandler.INSTANCE.getClassMapping(transformedName));
        if (prev != bytes) {
            this.saveBytecode(transformedName, bytes);
        }
        return bytes;
    }

    private byte[] handlePatches(byte[] bytes, String cls) {
        ClassPatcher patcher = this.patcherMap.get(cls);
        if (patcher != null) {
            ClassReader classReader = new ClassReader(bytes);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            patcher.handlePatches(classNode);
            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }
        return bytes;
    }

    public ClassPatcher patchClass(Object obj) {
        ClassPatcher patcher = null;
        if (obj instanceof String) {
            String cls = MappingHandler.INSTANCE.getClassMapping((String) obj);
            patcher = new ClassPatcher(cls);
            this.patcherMap.put(cls, patcher);
        }
        return patcher;
    }

    public InsnType _method(String name, Object... params) {
        return new InsnType.MethodInsnType() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public Object[] getDesc() {
                return params;
            }
        };
    }

    public InsnType _return() {
        return new InsnType.ReturnInsnType() {
        };
    }

    public InsnType _ldc(Object value) {
        return new InsnType.LDCInsnType() {
            @Override
            public Object getValue() {
                return value;
            }
        };
    }

    public InsnType _node(int opcode) {
        return new InsnType.NodeInsnType() {
            @Override
            public int getOpcode() {
                return opcode;
            }
        };
    }

    private void saveBytecode(String name, byte[] bytes) {
        FileOutputStream out = null;
        try {
            File debugDir = new File("llibrary/debug/");
            if (debugDir.exists()) {
                debugDir.delete();
            }
            debugDir.mkdirs();
            out = new FileOutputStream(new File(debugDir, name + ".class"));
            out.write(bytes);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    @Override
    public String toString() {
        return "runtimePatcher:" + this.getClass().getSimpleName() + this.patcherMap.values();
    }
}
