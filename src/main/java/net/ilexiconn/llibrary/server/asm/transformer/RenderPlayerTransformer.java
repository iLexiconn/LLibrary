package net.ilexiconn.llibrary.server.asm.transformer;

import net.ilexiconn.llibrary.client.event.PlayerModelEvent;
import net.ilexiconn.llibrary.client.event.RenderArmEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.common.MinecraftForge;
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
                insnList.add(new FieldInsnNode(GETSTATIC, "net/minecraft/util/EnumHandSide", dev ? renderLeftArm ? "LEFT" : "RIGHT" : renderLeftArm ? "a" : "b", "Lnet/minecraft/util/EnumHandSide;"));
                insnList.add(new MethodInsnNode(INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/transformer/RenderPlayerTransformer", "renderArm", "(Lnet/minecraft/client/renderer/entity/RenderPlayer;Lnet/minecraft/client/entity/AbstractClientPlayer;Lnet/minecraft/util/EnumHandSide;)V", false));
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
                        insnList.insertBefore(insnNode, new FieldInsnNode(INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/transformer/RenderPlayerTransformer", "assignModel", "(Lnet/minecraft/client/renderer/entity/RenderPlayer;Lnet/minecraft/client/model/ModelPlayer;Z)Lnet/minecraft/client/model/ModelPlayer;"));
                        insnList.insertBefore(insnNode, new FieldInsnNode(PUTFIELD, "net/minecraft/client/renderer/entity/RenderPlayer", dev ? "mainModel" : "field_77045_g", "Lnet/minecraft/client/model/ModelBase;"));
                    }
                }
            }
        }
    }

    @SuppressWarnings("unused")
    public static void renderArm(RenderPlayer renderPlayer, AbstractClientPlayer player, EnumHandSide side) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        ModelPlayer modelplayer = renderPlayer.getMainModel();
        renderPlayer.setModelVisibilities(player);
        modelplayer.swingProgress = 0.0F;
        modelplayer.isSneak = false;
        modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
        if (side == EnumHandSide.LEFT) {
            modelplayer.bipedLeftArm.rotateAngleX = 0.0F;
            modelplayer.bipedLeftArmwear.rotateAngleX = 0.0F;
        } else {
            modelplayer.bipedRightArm.rotateAngleX = 0.0F;
            modelplayer.bipedRightArmwear.rotateAngleX = 0.0F;
        }
        if (!MinecraftForge.EVENT_BUS.post(new RenderArmEvent.Pre(player, renderPlayer, renderPlayer.getMainModel(), side))) {
            GlStateManager.enableBlend();
            if (side == EnumHandSide.LEFT) {
                modelplayer.bipedLeftArm.render(0.0625F);
                modelplayer.bipedLeftArmwear.render(0.0625F);
            } else {
                modelplayer.bipedRightArm.render(0.0625F);
                modelplayer.bipedRightArmwear.render(0.0625F);
            }
            GlStateManager.disableBlend();
            MinecraftForge.EVENT_BUS.post(new RenderArmEvent.Post(player, renderPlayer, renderPlayer.getMainModel(), side));
        }
    }

    @SuppressWarnings("unused")
    public static ModelPlayer assignModel(RenderPlayer renderPlayer, ModelPlayer model, boolean smallArms) {
        PlayerModelEvent.Assign event = new PlayerModelEvent.Assign(renderPlayer, model, smallArms);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getModel();
    }
}
