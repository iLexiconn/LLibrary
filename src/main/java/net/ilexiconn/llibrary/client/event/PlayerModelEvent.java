package net.ilexiconn.llibrary.client.event;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;

@SideOnly(Side.CLIENT)
public class PlayerModelEvent extends Event {
    public final ModelBiped model;

    public PlayerModelEvent(ModelBiped model) {
        this.model = model;
    }

    public static class Construct extends PlayerModelEvent {
        public Construct(ModelBiped model) {
            super(model);
        }
    }

    public static class SetRotationAngles extends Render {
        public SetRotationAngles(ModelBiped model, EntityPlayer player, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float scale) {
            super(model, player, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, scale);
        }
    }

    public static class Render extends PlayerModelEvent {
        public final EntityPlayer entityPlayer;
        public final float limbSwing;
        public final float limbSwingAmount;
        public final float rotation;
        public final float rotationYaw;
        public final float rotationPitch;
        public final float scale;

        public Render(ModelBiped model, EntityPlayer player, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float scale) {
            super(model);
            this.entityPlayer = player;
            this.limbSwing = limbSwing;
            this.limbSwingAmount = limbSwingAmount;
            this.rotation = rotation;
            this.rotationYaw = rotationYaw;
            this.rotationPitch = rotationPitch;
            this.scale = scale;
        }
    }
}