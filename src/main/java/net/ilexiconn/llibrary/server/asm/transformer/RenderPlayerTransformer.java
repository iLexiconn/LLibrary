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
            if (methodNode.name.equals("renderFirstPersonArm") || methodNode.name.equals("func_82441_a")) {
                InsnList insnList = methodNode.instructions;
                insnList.clear();
                insnList.add(new VarInsnNode(ALOAD, 0));
                insnList.add(new VarInsnNode(ALOAD, 1));
                insnList.add(new FieldInsnNode(GETSTATIC, "net/ilexiconn/llibrary/server/util/EnumHandSide", "RIGHT", "Lnet/ilexiconn/llibrary/server/util/EnumHandSide;"));
                insnList.add(new MethodInsnNode(INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/LLibraryHooks", "renderArm", "(Lnet/minecraft/client/renderer/entity/RenderPlayer;Lnet/minecraft/entity/player/EntityPlayer;Lnet/ilexiconn/llibrary/server/util/EnumHandSide;)V", false));
                insnList.add(new InsnNode(RETURN));
            } else if (methodNode.name.equals("<init>")) {
                InsnList insnList = methodNode.instructions;
                for (AbstractInsnNode insnNode : insnList.toArray()) {
                    if (insnNode.getOpcode() == INVOKESPECIAL && ((MethodInsnNode) insnNode).desc.equals("(Lnet/minecraft/client/model/ModelBase;F)V")) {
                        insnList.insertBefore(insnNode, new VarInsnNode(ALOAD, 0));
                        insnList.insertBefore(insnNode, new VarInsnNode(ALOAD, 0));
                        insnList.insertBefore(insnNode, new VarInsnNode(ALOAD, 0));
                        insnList.insertBefore(insnNode, new VarInsnNode(ALOAD, 0));
                        insnList.insertBefore(insnNode, new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/entity/RenderPlayer", dev ? "mainModel" : "field_77045_g", "Lnet/minecraft/client/model/ModelBase;"));
                        insnList.insertBefore(insnNode, new TypeInsnNode(CHECKCAST, "net/minecraft/client/model/ModelBiped"));
                        insnList.insertBefore(insnNode, new FieldInsnNode(INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/LLibraryHooks", "assignModel", "(Lnet/minecraft/client/renderer/entity/RenderPlayer;Lnet/minecraft/client/model/ModelBiped;)Lnet/minecraft/client/model/ModelBiped;"));
                        insnList.insertBefore(insnNode, new FieldInsnNode(PUTFIELD, "net/minecraft/client/renderer/entity/RenderPlayer", dev ? "mainModel" : "field_77045_g", "Lnet/minecraft/client/model/ModelBase;"));
                    }
                }
            }
        }
    }
}
