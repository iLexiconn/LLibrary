package net.ilexiconn.llibrary.server.asm.transformer;

import org.objectweb.asm.tree.*;

public class RenderPlayerTransformer implements ITransformer {
    @Override
    public String getTarget() {
        return "net.minecraft.client.renderer.entity.RenderPlayer";
    }

    @Override
    public void transform(ClassNode node, boolean dev) {
        for (MethodNode methodNode : node.methods) {
            boolean renderLeftArm = methodNode.name.equals("renderLeftArm") || methodNode.name.equals("func_177139_c");
            boolean renderRightArm = methodNode.name.equals("renderRightArm") || methodNode.name.equals("func_177138_b");
            if (renderLeftArm || renderRightArm) {
                InsnList insnList = methodNode.instructions;
                insnList.clear();
                insnList.add(new VarInsnNode(ALOAD, 0));
                insnList.add(new VarInsnNode(ALOAD, 1));
                insnList.add(new FieldInsnNode(GETSTATIC, "net/ilexiconn/llibrary/server/util/EnumHandSide", renderLeftArm ? "LEFT" : "RIGHT", "Lnet/ilexiconn/llibrary/server/util/EnumHandSide;"));
                insnList.add(new MethodInsnNode(INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/LLibraryHooks", "renderArm", "(Lnet/minecraft/client/renderer/entity/RenderPlayer;Lnet/minecraft/client/entity/AbstractClientPlayer;Lnet/ilexiconn/llibrary/server/util/EnumHandSide;)V", false));
                insnList.add(new InsnNode(RETURN));
            } else if (methodNode.name.equals("<init>") && methodNode.desc.equals("(Lnet/minecraft/client/renderer/entity/RenderManager;Z)V")) {
                InsnList insnList = methodNode.instructions;
                for (AbstractInsnNode insnNode : insnList.toArray()) {
                    if (insnNode.getOpcode() == RETURN) {
                        insnList.insertBefore(insnNode, new VarInsnNode(ALOAD, 0));
                        insnList.insertBefore(insnNode, new VarInsnNode(ALOAD, 0));
                        insnList.insertBefore(insnNode, new VarInsnNode(ALOAD, 0));
                        insnList.insertBefore(insnNode, new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/renderer/entity/RenderPlayer", dev ? "getMainModel" : "func_177087_b", "()Lnet/minecraft/client/model/ModelPlayer;", false));
                        insnList.insertBefore(insnNode, new VarInsnNode(ALOAD, 0));
                        insnList.insertBefore(insnNode, new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/entity/RenderPlayer", dev ? "smallArms" : "field_177140_a", "Z"));
                        insnList.insertBefore(insnNode, new FieldInsnNode(INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/LLibraryHooks", "assignModel", "(Lnet/minecraft/client/renderer/entity/RenderPlayer;Lnet/minecraft/client/model/ModelPlayer;Z)Lnet/minecraft/client/model/ModelPlayer;"));
                        insnList.insertBefore(insnNode, new FieldInsnNode(PUTFIELD, "net/minecraft/client/renderer/entity/RenderPlayer", dev ? "mainModel" : "field_77045_g", "Lnet/minecraft/client/model/ModelBase;"));
                    }
                }
            }
        }
    }
}
