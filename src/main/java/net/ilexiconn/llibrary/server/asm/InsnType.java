package net.ilexiconn.llibrary.server.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public abstract class InsnType {
    static abstract class MethodInsnType extends InsnType {
        private String mappedName;
        private String mappedDesc;

        @Override
        public boolean equals(ClassPatcher patcher, String cls, AbstractInsnNode insnNode) {
            if (insnNode instanceof MethodInsnNode) {
                MethodInsnNode methodNode = (MethodInsnNode) insnNode;
                if (this.mappedDesc == null) {
                    this.mappedDesc = MappingHandler.INSTANCE.getClassMapping(patcher.methodDesc(this.getDesc()));
                }
                if (this.mappedName == null) {
                    this.mappedName = MappingHandler.INSTANCE.getMethodMapping(cls, this.getName(), this.mappedDesc);
                }
                return methodNode.name.equals(this.mappedName) && (this.mappedDesc.isEmpty() || methodNode.desc.equals(this.mappedDesc));
            }
            return false;
        }

        public abstract String getName();

        public abstract Object[] getDesc();
    }

    static abstract class ReturnInsnType extends InsnType {
        @Override
        public boolean equals(ClassPatcher patcher, String cls, AbstractInsnNode insnNode) {
            int opcode = insnNode.getOpcode();
            return opcode == Opcodes.RETURN || opcode == Opcodes.IRETURN || opcode == Opcodes.LRETURN || opcode == Opcodes.FRETURN || opcode == Opcodes.DRETURN || opcode == Opcodes.ARETURN;
        }
    }

    static abstract class LDCInsnType extends InsnType {
        @Override
        public boolean equals(ClassPatcher patcher, String cls, AbstractInsnNode insnNode) {
            if (insnNode instanceof LdcInsnNode) {
                LdcInsnNode ldcNode = (LdcInsnNode) insnNode;
                if (ldcNode.cst.equals(this.getValue())) {
                    return true;
                }
            }
            return false;
        }

        public abstract Object getValue();
    }

    static abstract class NodeInsnType extends InsnType {
        @Override
        public boolean equals(ClassPatcher patcher, String cls, AbstractInsnNode insnNode) {
            return insnNode.getOpcode() == this.getOpcode();
        }

        public abstract int getOpcode();
    }

    public abstract boolean equals(ClassPatcher patcher, String cls, AbstractInsnNode insnNode);
}
