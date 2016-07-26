package net.ilexiconn.llibrary.server.asm;

import net.minecraftforge.fml.relauncher.FMLRelaunchLog;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MethodPatcher {
    private ClassPatcher patcher;
    private String cls;
    private String method;

    private List<PatchData> patches = new ArrayList<>();

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
        this.patches.stream().filter(patch -> patch.predicate == null).forEach(patch -> {
            Method method = new Method(null);
            patch.consumer.accept(method);
            patch.at.apply(patch, methodNode, null, method);
        });
        for (AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
            this.patches.stream().filter(patch -> patch.predicate != null && patch.predicate.test(new PredicateData(this.patcher, this.cls, insnNode))).forEach(patch -> {
                Method method = new Method(insnNode);
                patch.consumer.accept(method);
                patch.at.apply(patch, methodNode, insnNode, method);
            });
        }
    }

    public MethodPatcher apply(RuntimePatcher.Patch at, Consumer<Method> consumer) {
        return this.apply(at, null, consumer);
    }

    public MethodPatcher apply(RuntimePatcher.Patch at, Predicate<PredicateData> insnType, Consumer<Method> consumer) {
        this.patches.add(new PatchData(at, insnType, consumer));
        return this;
    }

    public class Method {
        InsnList insnList = new InsnList();
        private AbstractInsnNode insnNode;

        public Method(AbstractInsnNode insnNode) {
            this.insnNode = insnNode;
        }

        public AbstractInsnNode getInsnNode() {
            return insnNode;
        }

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

    public class PatchData {
        public final RuntimePatcher.Patch at;
        public final Predicate<PredicateData> predicate;
        public final Consumer<MethodPatcher.Method> consumer;

        public PatchData(RuntimePatcher.Patch at, Predicate<PredicateData> predicate, Consumer<MethodPatcher.Method> consumer) {
            this.at = at;
            this.predicate = predicate;
            this.consumer = consumer;
        }
    }

    public class PredicateData {
        public final ClassPatcher patcher;
        public final String cls;
        public final AbstractInsnNode node;

        public PredicateData(ClassPatcher patcher, String cls, AbstractInsnNode node) {
            this.patcher = patcher;
            this.cls = cls;
            this.node = node;
        }
    }

    @Override
    public String toString() {
        return "method:" + this.method;
    }
}
