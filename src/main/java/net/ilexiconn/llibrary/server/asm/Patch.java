package net.ilexiconn.llibrary.server.asm;

import java.util.function.Consumer;

public class Patch {
    public final InsnType insnType;
    public final PatchType patchType;
    public final Consumer<MethodPatcher.Method> consumer;

    public Patch(InsnType insnType, PatchType patchType, Consumer<MethodPatcher.Method> consumer) {
        this.insnType = insnType;
        this.patchType = patchType;
        this.consumer = consumer;
    }
}
