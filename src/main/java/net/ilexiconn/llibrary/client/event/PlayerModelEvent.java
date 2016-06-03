package net.ilexiconn.llibrary.client.event;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerModelEvent extends Event {
    protected ModelPlayer model;

    PlayerModelEvent(ModelPlayer model) {
        this.model = model;
    }

    public ModelPlayer getModel() {
        return this.model;
    }

    public static class Assign extends PlayerModelEvent {
        private RenderPlayer renderPlayer;
        private boolean smallArms;

        public Assign(RenderPlayer renderPlayer, ModelPlayer model, boolean smallArms) {
            super(model);
            this.renderPlayer = renderPlayer;
            this.smallArms = smallArms;
        }

        public RenderPlayer getRenderPlayer() {
            return this.renderPlayer;
        }

        public void setModel(ModelPlayer model) {
            this.model = model;
        }

        public boolean hasSmallArms() {
            return this.smallArms;
        }
    }

    public static class Construct extends PlayerModelEvent {
        public Construct(ModelPlayer model) {
            super(model);
        }
    }

    public static class SetRotationAngles extends Render {
        public SetRotationAngles(ModelPlayer model, EntityPlayer player, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float scale) {
            super(model, player, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, scale);
        }
    }

    public static class Render extends PlayerModelEvent {
        private EntityPlayer entityPlayer;
        private float limbSwing;
        private float limbSwingAmount;
        private float rotation;
        private float rotationYaw;
        private float rotationPitch;
        private float scale;

        public Render(ModelPlayer model, EntityPlayer player, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float scale) {
            super(model);
            this.entityPlayer = player;
            this.limbSwing = limbSwing;
            this.limbSwingAmount = limbSwingAmount;
            this.rotation = rotation;
            this.rotationYaw = rotationYaw;
            this.rotationPitch = rotationPitch;
            this.scale = scale;
        }

        public EntityPlayer getEntityPlayer() {
            return entityPlayer;
        }

        public float getLimbSwing() {
            return limbSwing;
        }

        public float getLimbSwingAmount() {
            return limbSwingAmount;
        }

        public float getRotation() {
            return rotation;
        }

        public float getRotationYaw() {
            return rotationYaw;
        }

        public float getRotationPitch() {
            return rotationPitch;
        }

        public float getScale() {
            return scale;
        }
    }
}