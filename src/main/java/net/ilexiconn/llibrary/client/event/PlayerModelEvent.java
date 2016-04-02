package net.ilexiconn.llibrary.client.event;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;

@SideOnly(Side.CLIENT)
public class PlayerModelEvent extends Event {
    protected ModelBiped model;

    public PlayerModelEvent(ModelBiped model) {
        this.model = model;
    }

    public static class Assign extends PlayerModelEvent {
        protected RenderPlayer renderPlayer;

        public Assign(RenderPlayer renderPlayer, ModelBiped model) {
            super(model);
            this.renderPlayer = renderPlayer;
        }

        public RenderPlayer getRenderPlayer() {
            return this.renderPlayer;
        }

        public void setModel(ModelBiped model) {
            this.model = model;
        }
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
        private EntityPlayer entityPlayer;
        private float limbSwing;
        private float limbSwingAmount;
        private float rotation;
        private float rotationYaw;
        private float rotationPitch;
        private float scale;

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

    public ModelBiped getModel() {
        return this.model;
    }
}