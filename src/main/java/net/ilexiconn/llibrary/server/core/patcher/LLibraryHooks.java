package net.ilexiconn.llibrary.server.core.patcher;

import net.ilexiconn.llibrary.client.event.ApplyRenderRotationsEvent;
import net.ilexiconn.llibrary.client.event.PlayerModelEvent;
import net.ilexiconn.llibrary.client.event.PlayerViewDistanceEvent;
import net.ilexiconn.llibrary.client.event.RenderArmEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LLibraryHooks {
    @SideOnly(Side.CLIENT)
    public static float prevRenderViewDistance = 4.0F;

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    public static void renderArm(RenderPlayer renderPlayer, AbstractClientPlayer player, EnumHandSide side) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        ModelPlayer modelPlayer = renderPlayer.getMainModel();
        renderPlayer.setModelVisibilities(player);
        modelPlayer.swingProgress = 0.0F;
        modelPlayer.isSneak = false;
        modelPlayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
        if (side == EnumHandSide.LEFT) {
            modelPlayer.bipedLeftArm.rotateAngleX = 0.0F;
            modelPlayer.bipedLeftArmwear.rotateAngleX = 0.0F;
        } else {
            modelPlayer.bipedRightArm.rotateAngleX = 0.0F;
            modelPlayer.bipedRightArmwear.rotateAngleX = 0.0F;
        }
        if (!MinecraftForge.EVENT_BUS.post(new RenderArmEvent.Pre(player, renderPlayer, renderPlayer.getMainModel(), side))) {
            GlStateManager.enableBlend();
            if (side == EnumHandSide.LEFT) {
                modelPlayer.bipedLeftArm.render(0.0625F);
                modelPlayer.bipedLeftArmwear.render(0.0625F);
            } else {
                modelPlayer.bipedRightArm.render(0.0625F);
                modelPlayer.bipedRightArmwear.render(0.0625F);
            }
            GlStateManager.disableBlend();
            MinecraftForge.EVENT_BUS.post(new RenderArmEvent.Post(player, renderPlayer, renderPlayer.getMainModel(), side));
        }
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    public static ModelBiped assignModel(RenderPlayer renderPlayer, ModelBiped model, boolean smallArms) {
        PlayerModelEvent.Assign event = new PlayerModelEvent.Assign(renderPlayer, model, smallArms);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getModel();
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    public static void setRotationAngles(ModelBiped model, Entity entity, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float scale) {
        if (entity instanceof EntityPlayer) {
            MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.SetRotationAngles(model, (EntityPlayer) entity, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, scale));
        }
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    public static void renderModel(ModelBiped model, Entity entity, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float scale) {
        if (entity instanceof EntityPlayer) {
            MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Render(model, (EntityPlayer) entity, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, scale));
        }
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    public static void constructModel(ModelPlayer model) {
        MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Construct(model));
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    public static float getViewDistance(Entity entity, float partialTicks) {
        PlayerViewDistanceEvent event = new PlayerViewDistanceEvent(entity, partialTicks, 4.0);
        MinecraftForge.EVENT_BUS.post(event);
        float distance = (float) event.getNewViewDistance();
        prevRenderViewDistance = distance;
        return distance;
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    public static void applyRotationsPre(RenderLivingBase<EntityLivingBase> renderer, EntityLivingBase entity, float partialTicks) {
        MinecraftForge.EVENT_BUS.post(new ApplyRenderRotationsEvent.Pre(renderer, entity, partialTicks));
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    public static void applyRotationsPost(RenderLivingBase<EntityLivingBase> renderer, EntityLivingBase entity, float partialTicks) {
        MinecraftForge.EVENT_BUS.post(new ApplyRenderRotationsEvent.Post(renderer, entity, partialTicks));
    }
}
