package net.ilexiconn.llibrary.server.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.commons.compress.utils.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

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

    public Predicate<MethodPatcher.PredicateData> at(At at, Object... args) {
        return at.getPredicate(args);
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

    public enum At {
        METHOD {
            @Override
            public Predicate<MethodPatcher.PredicateData> getPredicate(Object... args) {
                return new Predicate<MethodPatcher.PredicateData>() {
                    private String mappedName;
                    private String mappedDesc;

                    @Override
                    public boolean test(MethodPatcher.PredicateData predicateData) {
                        if (predicateData.node instanceof MethodInsnNode) {
                            MethodInsnNode methodNode = (MethodInsnNode) predicateData.node;
                            if (this.mappedDesc == null) {
                                this.mappedDesc = MappingHandler.INSTANCE.getClassMapping(predicateData.patcher.methodDesc(Arrays.copyOfRange(args, 1, args.length)));
                            }
                            if (this.mappedName == null) {
                                this.mappedName = MappingHandler.INSTANCE.getMethodMapping(predicateData.cls, (String) args[0], this.mappedDesc);
                            }
                            return methodNode.name.equals(this.mappedName) && (this.mappedDesc.isEmpty() || methodNode.desc.equals(this.mappedDesc));
                        }
                        return false;
                    }
                };
            }
        },
        RETURN {
            @Override
            public Predicate<MethodPatcher.PredicateData> getPredicate(Object... args) {
                return predicateData -> {
                    int opcode = predicateData.node.getOpcode();
                    return opcode == Opcodes.RETURN || opcode == Opcodes.IRETURN || opcode == Opcodes.LRETURN || opcode == Opcodes.FRETURN || opcode == Opcodes.DRETURN || opcode == Opcodes.ARETURN;
                };
            }
        },
        LDC {
            @Override
            public Predicate<MethodPatcher.PredicateData> getPredicate(Object... args) {
                return predicateData -> {
                    if (predicateData.node instanceof LdcInsnNode) {
                        LdcInsnNode ldcNode = (LdcInsnNode) predicateData.node;
                        if (ldcNode.cst.equals(args[0])) {
                            return true;
                        }
                    }
                    return false;
                };
            }
        },
        NODE {
            @Override
            public Predicate<MethodPatcher.PredicateData> getPredicate(Object... args) {
                return predicateData -> predicateData.node.getOpcode() == (int) args[0];
            }
        };

        public abstract Predicate<MethodPatcher.PredicateData> getPredicate(Object... args);
    }

    public enum Patch {
        BEFORE {
            @Override
            public void apply(MethodPatcher.PatchData patch, MethodNode methodNode, AbstractInsnNode location, Method method) {
                methodNode.instructions.insertBefore(location, method.insnList);
            }
        },
        AFTER {
            @Override
            public void apply(MethodPatcher.PatchData patch, MethodNode methodNode, AbstractInsnNode location, Method method) {
                methodNode.instructions.insert(location, method.insnList);
            }
        },
        REPLACE {
            @Override
            public void apply(MethodPatcher.PatchData patch, MethodNode methodNode, AbstractInsnNode location, Method method) {
                methodNode.instructions.clear();
                methodNode.instructions.add(method.insnList);
            }
        },
        REPLACE_NODE {
            @Override
            public void apply(MethodPatcher.PatchData patch, MethodNode methodNode, AbstractInsnNode location, Method method) {
                methodNode.instructions.insertBefore(location, method.insnList);
                methodNode.instructions.remove(location);
            }
        };

        public abstract void apply(MethodPatcher.PatchData patch, MethodNode methodNode, AbstractInsnNode location, Method method);
    }

    @Override
    public String toString() {
        return "runtimePatcher:" + this.getClass().getSimpleName() + this.patcherMap.values();
    }
}
