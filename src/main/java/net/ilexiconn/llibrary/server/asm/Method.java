package net.ilexiconn.llibrary.server.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class Method {
    private ClassPatcher patcher;
    InsnList insnList = new InsnList();
    private AbstractInsnNode insnNode;

    public Method(ClassPatcher patcher, AbstractInsnNode insnNode) {
        this.patcher = patcher;
        this.insnNode = insnNode;
    }

    public AbstractInsnNode getInsnNode() {
        return insnNode;
    }

    public Method field(int opcode, Object obj, String name, Object type) {
        if (obj instanceof String) {
            String cls = MappingHandler.INSTANCE.getClassMapping((String) obj);
            String desc = MappingHandler.INSTANCE.getClassMapping(this.patcher.fieldDesc(type));
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
            String desc = MappingHandler.INSTANCE.getClassMapping(this.patcher.methodDesc(params));
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
