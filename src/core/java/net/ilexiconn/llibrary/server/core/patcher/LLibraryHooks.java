package net.ilexiconn.llibrary.server.core.patcher;

import net.ilexiconn.llibrary.client.event.ApplyRenderRotationsEvent;
import net.ilexiconn.llibrary.client.event.PlayerModelEvent;
import net.ilexiconn.llibrary.client.event.PlayerViewDistanceEvent;
import net.ilexiconn.llibrary.client.event.RenderArmEvent;
import net.ilexiconn.llibrary.server.core.api.LLibraryCoreAPI;
import net.ilexiconn.llibrary.server.core.plugin.LLibraryPlugin;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

public class LLibraryHooks {
    @SideOnly(Side.CLIENT)
    public static float prevRenderViewDistance = 4.0F;

    @SideOnly(Side.CLIENT)
    public static void addRemoteLocalizations(String language, Map<String, String> properties) {
        LLibraryHooks.getAPI().addRemoteLocalizations(language, properties);
    }

    @SideOnly(Side.CLIENT)
    public static void provideStackContext(ItemStack stack) {
        if (stack != null) {
            LLibraryHooks.getAPI().provideStackContext(stack);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void providePerspectiveContext(ItemCameraTransforms.TransformType transforms) {
        LLibraryHooks.getAPI().providePerspectiveContext(transforms);
    }

    public static long getTickRate() {
        return LLibraryHooks.getAPI().getTickRate();
    }

    private static LLibraryCoreAPI getAPI() {
        if (LLibraryPlugin.api == null) {
            LLibraryPlugin.LOGGER.warn("Unable to call Core API! It has not been initialized yet!");
            return new LLibraryCoreAPI.Fallback();
        }
        return LLibraryPlugin.api;
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    public static void renderArmPre(RenderPlayer renderPlayer, AbstractClientPlayer player, EnumHandSide side) {
        ModelPlayer modelPlayer = renderPlayer.getMainModel();
        if (MinecraftForge.EVENT_BUS.post(new RenderArmEvent.Pre(player, renderPlayer, modelPlayer, side))) {
            if (side == EnumHandSide.LEFT) {
                modelPlayer.bipedLeftArm.showModel = false;
                modelPlayer.bipedLeftArmwear.showModel = false;
            } else {
                modelPlayer.bipedRightArm.showModel = false;
                modelPlayer.bipedRightArmwear.showModel = false;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    public static void renderArmPost(RenderPlayer renderPlayer, AbstractClientPlayer player, EnumHandSide side) {
        ModelPlayer modelPlayer = renderPlayer.getMainModel();
        MinecraftForge.EVENT_BUS.post(new RenderArmEvent.Post(player, renderPlayer, modelPlayer, side));
        if (side == EnumHandSide.LEFT) {
            modelPlayer.bipedLeftArm.showModel = true;
            modelPlayer.bipedLeftArmwear.showModel = true;
        } else {
            modelPlayer.bipedRightArm.showModel = true;
            modelPlayer.bipedRightArmwear.showModel = true;
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
