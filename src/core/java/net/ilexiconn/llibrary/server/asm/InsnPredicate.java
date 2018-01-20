package net.ilexiconn.llibrary.server.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.function.Predicate;

public abstract class InsnPredicate implements Predicate<MethodPatcher.PredicateData> {
    public static final Predicate<Integer> RETURNING = opcode -> opcode == Opcodes.RETURN || opcode == Opcodes.IRETURN || opcode == Opcodes.LRETURN || opcode == Opcodes.FRETURN || opcode == Opcodes.DRETURN || opcode == Opcodes.ARETURN;

    protected Predicate<Integer> opcodePredicate = opcode -> true;

    public InsnPredicate opcode(Predicate<Integer> opcodePredicate) {
        this.opcodePredicate = opcodePredicate;
        return this;
    }

    public InsnPredicate opcode(int _opcode) {
        return this.opcode(opcode -> opcode == _opcode);
    }

    @Override
    public boolean test(MethodPatcher.PredicateData predicateData) {
        return this.opcodePredicate.test(predicateData.node.getOpcode());
    }

    public static class Op extends InsnPredicate {
        @Override
        public boolean test(MethodPatcher.PredicateData predicateData) {
            return predicateData.node instanceof InsnNode && super.test(predicateData);
        }
    }

    public static class Frame extends InsnPredicate {
        private final int frameType;

        public Frame(int frameType) {
            this.frameType = frameType;
        }

        @Override
        public boolean test(MethodPatcher.PredicateData predicateData) {
            return predicateData.node instanceof FrameNode && ((FrameNode) predicateData.node).type == this.frameType;
        }
    }

    public static class Ldc extends InsnPredicate {
        protected Predicate<Object> cstPredicate = cst -> true;

        public Ldc cst(Predicate<Object> cstPredicate) {
            this.cstPredicate = cstPredicate;
            return this;
        }

        public Ldc cst(Object _cst) {
            return this.cst(cst -> cst.equals(_cst));
        }

        @Override
        public boolean test(MethodPatcher.PredicateData predicateData) {
            if (predicateData.node instanceof LdcInsnNode) {
                LdcInsnNode node = (LdcInsnNode) predicateData.node;
                return super.test(predicateData) && this.cstPredicate.test(node.cst);
            } else {
                return false;
            }
        }
    }

    public static class Method extends InsnPredicate {
        protected final String owner;
        protected final String desc;
        protected final String name;

        public Method(Object owner, String name, Object... desc) {
            this.owner = MappingHandler.INSTANCE.getClassMapping(owner);
            this.desc = MappingHandler.INSTANCE.getClassMapping(Descriptors.method(desc));
            this.name = MappingHandler.INSTANCE.getMethodMapping(owner, name, this.desc);
        }

        @Override
        public boolean test(MethodPatcher.PredicateData predicateData) {
            if (predicateData.node instanceof MethodInsnNode) {
                MethodInsnNode node = (MethodInsnNode) predicateData.node;
                return super.test(predicateData) && this.owner.equals(node.owner) && this.desc.equals(node.desc) && this.name.equals(node.name);
            } else {
                return false;
            }
        }
    }

    public static class Field extends InsnPredicate {
        protected final String owner;
        protected final String desc;
        protected final String name;

        public Field(Object owner, String name, Object desc) {
            this.owner = MappingHandler.INSTANCE.getClassMapping(owner);
            this.desc = MappingHandler.INSTANCE.getClassMapping(Descriptors.field(desc));
            this.name = MappingHandler.INSTANCE.getFieldMapping(owner, name);
        }

        @Override
        public boolean test(MethodPatcher.PredicateData predicateData) {
            if (predicateData.node instanceof FieldInsnNode) {
                FieldInsnNode node = (FieldInsnNode) predicateData.node;
                return super.test(predicateData) && this.owner.equals(node.owner) && this.desc.equals(node.desc) && this.name.equals(node.name);
            } else {
                return false;
            }
        }
    }

    public static class Var extends InsnPredicate {
        protected Predicate<Integer> varPredicate = index -> true;

        public Var var(Predicate<Integer> varPredicate) {
            this.varPredicate = varPredicate;
            return this;
        }

        public Var var(int _var) {
            return this.var(var -> var == _var);
        }

        @Override
        public boolean test(MethodPatcher.PredicateData predicateData) {
            if (predicateData.node instanceof VarInsnNode) {
                VarInsnNode node = (VarInsnNode) predicateData.node;
                return super.test(predicateData) && this.varPredicate.test(node.var);
            } else {
                return false;
            }
        }
    }
}
