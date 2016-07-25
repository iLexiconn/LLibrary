package net.ilexiconn.llibrary.server.asm;

import net.minecraftforge.fml.relauncher.FMLRelaunchLog;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.*;
import java.util.function.Consumer;

public class MethodPatcher {
    private ClassPatcher patcher;
    private String cls;
    private String method;

    private List<Patch> patches = new ArrayList<>();

    public MethodPatcher(ClassPatcher patcher, String cls, String method) {
        this.patcher = patcher;
        this.cls = cls;
        this.method = method;
    }

    public ClassPatcher pop() {
        return this.patcher;
    }

    void handlePatches(MethodNode methodNode) {
        FMLRelaunchLog.info("   Patching method " + this.method);
        this.patches.stream().filter(patch -> patch.insnType == null).forEach(patch -> {
            Method method = new Method();
            patch.consumer.accept(method);
            patch.patchType.apply(methodNode.instructions, null, method.insnList);
        });
        for (AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
            this.patches.stream().filter(patch -> patch.insnType != null && patch.insnType.equals(this.patcher, this.cls, insnNode)).forEach(patch -> {
                Method method = new Method();
                patch.consumer.accept(method);
                patch.patchType.apply(methodNode.instructions, insnNode, method.insnList);
            });
        }
    }

    public MethodPatcher insertBefore(InsnType insnType, Consumer<Method> consumer) {
        this.addPatch(PatchType.BEFORE, insnType, consumer);
        return this;
    }

    public MethodPatcher insertAfter(InsnType insnType, Consumer<Method> consumer) {
        this.addPatch(PatchType.AFTER, insnType, consumer);
        return this;
    }

    public MethodPatcher set(Consumer<Method> consumer) {
        this.addPatch(PatchType.SET, null, consumer);
        return this;
    }

    public MethodPatcher replace(InsnType insnType, Consumer<Method> consumer) {
        this.addPatch(PatchType.REPLACE, insnType, consumer);
        return this;
    }

    private void addPatch(PatchType patchType, InsnType insnType, Consumer<Method> consumer) {
        this.patches.add(new Patch(insnType, patchType, consumer));
    }

    public class Method {
        private InsnList insnList = new InsnList();

        public Method field(int opcode, Object obj, String name, Object type) {
            if (obj instanceof String) {
                String cls = MappingHandler.INSTANCE.getClassMapping((String) obj);
                String desc = MappingHandler.INSTANCE.getClassMapping(patcher.fieldDesc(type));
                this.insnList.add(new FieldInsnNode(opcode, cls, MappingHandler.INSTANCE.getFieldMapping(cls, name), desc));
            }
            return this;
        }

        public Method var(int opcode, int var) {
            return this.var(opcode, var, var);
        }

        public Method var(int opcode, int from, int to) {
            if (from < to) {
                for (; from <= to; from++) {
                    this.insnList.add(new VarInsnNode(opcode, from));
                }
            } else if (from > to) {
                for (; from >= to; from--) {
                    this.insnList.add(new VarInsnNode(opcode, from));
                }
            } else {
                this.insnList.add(new VarInsnNode(opcode, from));
            }
            return this;
        }

        public Method method(int opcode, Object obj, String name, Object... params) {
            if (obj instanceof String) {
                String cls = MappingHandler.INSTANCE.getClassMapping((String) obj);
                String desc = MappingHandler.INSTANCE.getClassMapping(patcher.methodDesc(params));
                this.insnList.add(new MethodInsnNode(opcode, cls, MappingHandler.INSTANCE.getMethodMapping(cls, name, desc), desc, opcode == Opcodes.INVOKEINTERFACE));
            }
            return this;
        }

        public Method node(int opcode) {
            return this.node(new InsnNode(opcode));
        }

        public Method node(AbstractInsnNode node) {
            this.insnList.add(node);
            return this;
        }
    }

    @Override
    public String toString() {
        return "method:" + this.method;
    }
}
