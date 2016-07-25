package net.ilexiconn.llibrary.server.core.patcher;

import net.ilexiconn.llibrary.client.event.PlayerModelEvent;
import net.ilexiconn.llibrary.client.event.RenderArmEvent;
import net.ilexiconn.llibrary.server.util.EnumHandSide;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

public class LLibraryHooks {
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

    @SuppressWarnings("unused")
    public static void setRotationAngles(ModelBiped model, Entity entity, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float scale) {
        if (entity instanceof EntityPlayer) {
            MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.SetRotationAngles(model, (EntityPlayer) entity, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, scale));
        }
    }

    @SuppressWarnings("unused")
    public static void renderModel(ModelBiped model, Entity entity, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float scale) {
        if (entity instanceof EntityPlayer) {
            MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Render(model, (EntityPlayer) entity, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, scale));
        }
    }

    @SuppressWarnings("unused")
    public static void constructModel(ModelBiped model) {
        MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Construct(model));
    }
}
