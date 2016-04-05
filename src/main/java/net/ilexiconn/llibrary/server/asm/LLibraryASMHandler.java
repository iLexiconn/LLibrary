package net.ilexiconn.llibrary.server.asm;

import net.ilexiconn.llibrary.client.event.PlayerModelEvent;
import net.ilexiconn.llibrary.client.event.RenderArmEvent;
import net.ilexiconn.llibrary.server.world.TickRateHandler;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public enum LLibraryASMHandler {
    INSTANCE;

    public boolean renderArmPre(EntityPlayer player, RenderPlayer renderPlayer) {
        return MinecraftForge.EVENT_BUS.post(new RenderArmEvent.Pre(player, renderPlayer, renderPlayer.modelBipedMain));
    }

    public void renderArmPost(EntityPlayer player, RenderPlayer renderPlayer) {
        MinecraftForge.EVENT_BUS.post(new RenderArmEvent.Post(player, renderPlayer, renderPlayer.modelBipedMain));
    }

    public void setRotationAngles(ModelBiped model, Entity entity, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float scale) {
        if (entity instanceof EntityPlayer) {
            MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.SetRotationAngles(model, (EntityPlayer) entity, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, scale));
        }
    }

    public void constructModel(ModelBiped model) {
        MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Construct(model));
    }

    public void renderModel(ModelBiped model, Entity entity, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float scale) {
        if (entity instanceof EntityPlayer) {
            MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Render(model, (EntityPlayer) entity, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, scale));
        }
    }

    public ModelBiped assign(RenderPlayer renderPlayer, ModelBase model) {
        PlayerModelEvent.Assign event = new PlayerModelEvent.Assign(renderPlayer, (ModelBiped) model);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getModel();
    }

    public long getTickRate() {
        return TickRateHandler.INSTANCE.getTickRate();
    }
}
