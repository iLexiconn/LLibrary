package net.ilexiconn.llibrary.server.core.patcher;

import net.ilexiconn.llibrary.client.event.PlayerModelEvent;
import net.ilexiconn.llibrary.client.event.RenderArmEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.common.MinecraftForge;

public class LLibraryHooks {
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

    @SuppressWarnings("unused")
    public static void setRotationAngles(ModelPlayer model, Entity entity, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float scale) {
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.SetRotationAngles(model, (EntityPlayer) entity, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, scale));
    }

    @SuppressWarnings("unused")
    public static void renderModel(ModelPlayer model, Entity entity, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float scale) {
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Render(model, (EntityPlayer) entity, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, scale));
    }

    @SuppressWarnings("unused")
    public static void constructModel(ModelPlayer model) {
        MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Construct(model));
    }
}
