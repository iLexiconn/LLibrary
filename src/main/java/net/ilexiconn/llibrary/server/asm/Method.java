package net.ilexiconn.llibrary.server.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class Method {
    private ClassPatcher patcher;
    InsnList insnList = new InsnList();
    private AbstractInsnNode insnNode;

    public Method(ClassPatcher patcher, AbstractInsnNode insnNode) {
        this.patcher = patcher;
        this.insnNode = insnNode;
    }

    public AbstractInsnNode getInsnNode() {
        return this.insnNode;
    }

    /**
     * Add a field instruction
     *
     * @param opcode the opcode
     * @param obj the class object or class name
     * @param name the field name
     * @param type the type class or name
     * @return this
     */
    public Method field(int opcode, Object obj, String name, Object type) {
        if (obj instanceof String) {
            String cls = MappingHandler.INSTANCE.getClassMapping((String) obj);
            String desc = MappingHandler.INSTANCE.getClassMapping(Descriptors.field(type));
            this.insnList.add(new FieldInsnNode(opcode, cls, MappingHandler.INSTANCE.getFieldMapping(cls, name), desc));
        }
        return this;
    }

    /**
     * Add a single var instruction
     *
     * @param opcode the opcode
     * @param var the var
     * @return this
     */
    public Method var(int opcode, int var) {
        return this.var(opcode, var, var);
    }

    /**
     * Load a range of vars of the same type
     *
     * @param opcode the opcode
     * @param from the range begin
     * @param to the range end
     * @return this
     */
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

    /**
     * Add a method instruction
     *
     * @param opcode the opcode
     * @param obj the class object or class name
     * @param name the method name
     * @param params the method parameters
     * @return this
     */
    public Method method(int opcode, Object obj, String name, Object... params) {
        if (obj instanceof String) {
            String cls = MappingHandler.INSTANCE.getClassMapping((String) obj);
            String desc = MappingHandler.INSTANCE.getClassMapping(Descriptors.method(params));
            this.insnList.add(new MethodInsnNode(opcode, cls, MappingHandler.INSTANCE.getMethodMapping(cls, name, desc), desc, opcode == Opcodes.INVOKEINTERFACE));
        }
        return this;
    }

    /**
     * Add a basic instruction
     *
     * @param opcode the opcode
     * @return this
     */
    public Method node(int opcode) {
        return this.node(new InsnNode(opcode));
    }

    /**
     * Add a custom instruction
     *
     * @param node the instruction
     * @return this
     */
    public Method node(AbstractInsnNode node) {
        this.insnList.add(node);
        return this;
    }
}
