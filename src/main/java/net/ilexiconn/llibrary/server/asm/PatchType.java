package net.ilexiconn.llibrary.server.asm;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public enum PatchType {
    BEFORE {
        @Override
        void apply(InsnList insnList, AbstractInsnNode at, InsnList instructions) {
            insnList.insertBefore(at, instructions);
        }
    },
    AFTER {
        @Override
        void apply(InsnList insnList, AbstractInsnNode at, InsnList instructions) {
            insnList.insert(at, instructions);
        }
    },
    SET {
        @Override
        void apply(InsnList insnList, AbstractInsnNode at, InsnList instructions) {
            insnList.clear();
            insnList.add(instructions);
        }
    },
    REPLACE {
        @Override
        void apply(InsnList insnList, AbstractInsnNode at, InsnList instructions) {
            insnList.insertBefore(at, instructions);
            insnList.remove(at);
        }
    };

    abstract void apply(InsnList insnList, AbstractInsnNode at, InsnList instructions);
}
