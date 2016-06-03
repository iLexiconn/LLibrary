package net.ilexiconn.llibrary.server.asm.transformer;

import net.ilexiconn.llibrary.client.event.PlayerModelEvent;
import net.ilexiconn.llibrary.client.event.RenderArmEvent;
import net.ilexiconn.llibrary.server.util.EnumHandSide;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
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
                insnList.add(new MethodInsnNode(INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/transformer/RenderPlayerTransformer", "renderArm", "(Lnet/minecraft/client/renderer/entity/RenderPlayer;Lnet/minecraft/entity/player/EntityPlayer;Lnet/ilexiconn/llibrary/server/util/EnumHandSide;)V", false));
                insnList.add(new InsnNode(RETURN));
            } else if (methodNode.name.equals("<init>")) {
                InsnList insnList = methodNode.instructions;
                for (AbstractInsnNode insnNode : insnList.toArray()) {
                    if (insnNode.getOpcode() == RETURN) {
                        insnList.insertBefore(insnNode, new VarInsnNode(ALOAD, 0));
                        insnList.insertBefore(insnNode, new VarInsnNode(ALOAD, 0));
                        insnList.insertBefore(insnNode, new VarInsnNode(ALOAD, 0));
                        insnList.insertBefore(insnNode, new VarInsnNode(ALOAD, 0));
                        insnList.insertBefore(insnNode, new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/entity/RenderPlayer", dev ? "mainModel" : "field_77045_g", "Lnet/minecraft/client/model/ModelBase;"));
                        insnList.insertBefore(insnNode, new TypeInsnNode(CHECKCAST, "net/minecraft/client/model/ModelBiped"));
                        insnList.insertBefore(insnNode, new FieldInsnNode(INVOKESTATIC, "net/ilexiconn/llibrary/server/asm/transformer/RenderPlayerTransformer", "assignModel", "(Lnet/minecraft/client/renderer/entity/RenderPlayer;Lnet/minecraft/client/model/ModelBiped;)Lnet/minecraft/client/model/ModelBiped;"));
                        insnList.insertBefore(insnNode, new FieldInsnNode(PUTFIELD, "net/minecraft/client/renderer/entity/RenderPlayer", dev ? "mainModel" : "field_77045_g", "Lnet/minecraft/client/model/ModelBase;"));
                    }
                }
            }
        }
    }

    @SuppressWarnings("unused")
    public static void renderArm(RenderPlayer renderPlayer, EntityPlayer player, EnumHandSide side) {
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        renderPlayer.modelBipedMain.onGround = 0.0F;
        renderPlayer.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
        if (!MinecraftForge.EVENT_BUS.post(new RenderArmEvent.Pre((AbstractClientPlayer) player, renderPlayer, renderPlayer.modelBipedMain, side))) {
            GL11.glEnable(GL11.GL_BLEND);
            renderPlayer.modelBipedMain.bipedRightArm.render(0.0625F);
            GL11.glDisable(GL11.GL_BLEND);
            MinecraftForge.EVENT_BUS.post(new RenderArmEvent.Post((AbstractClientPlayer) player, renderPlayer, renderPlayer.modelBipedMain, side));
        }
    }

    @SuppressWarnings("unused")
    public static ModelBiped assignModel(RenderPlayer renderPlayer, ModelBiped model) {
        PlayerModelEvent.Assign event = new PlayerModelEvent.Assign(renderPlayer, model);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getModel();
    }
}
