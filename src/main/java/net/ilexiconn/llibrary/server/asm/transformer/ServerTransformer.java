package net.ilexiconn.llibrary.server.asm.transformer;

import org.objectweb.asm.tree.*;

public class ServerTransformer implements ITransformer {
    @Override
    public String getTarget() {
        return "net.minecraft.server.MinecraftServer";
    }

    @Override
    public void transform(ClassNode node, boolean dev) {
        for (MethodNode methodNode : node.methods) {
            if (methodNode.name.equals("run")) {
                InsnList insnList = methodNode.instructions;
                for (AbstractInsnNode insnNode : insnList.toArray()) {
                    if (insnNode.getOpcode() == LDC && ((LdcInsnNode) insnNode).cst instanceof Long && (Long) ((LdcInsnNode) insnNode).cst == 50L) {
                        insnList.insertBefore(insnNode, new FieldInsnNode(GETSTATIC, "net/ilexiconn/llibrary/server/world/TickRateHandler", "INSTANCE", "Lnet/ilexiconn/llibrary/server/world/TickRateHandler;"));
                        insnList.insertBefore(insnNode, new MethodInsnNode(INVOKEVIRTUAL, "net/ilexiconn/llibrary/server/world/TickRateHandler", "getTickRate", "()J", false));
                        insnList.remove(insnNode);
                    }
                }
                return;
            }
        }
    }
}
