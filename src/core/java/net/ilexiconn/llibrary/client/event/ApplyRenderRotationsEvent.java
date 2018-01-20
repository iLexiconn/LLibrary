package net.ilexiconn.llibrary.client.event;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Called when applyRotations is called for an EntityLivingBase
 *
 * @author gegy1000
 * @since 1.7.7
 */
@SideOnly(Side.CLIENT)
public class ApplyRenderRotationsEvent extends Event {
    protected RenderLivingBase<EntityLivingBase> renderer;
    protected EntityLivingBase entity;
    protected float partialTicks;

    ApplyRenderRotationsEvent(RenderLivingBase<EntityLivingBase> renderer, EntityLivingBase entity, float partialTicks) {
        this.renderer = renderer;
        this.entity = entity;
        this.partialTicks = partialTicks;
    }

    public RenderLivingBase<EntityLivingBase> getRenderer() {
        return this.renderer;
    }

    public EntityLivingBase getEntity() {
        return this.entity;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public static class Pre extends ApplyRenderRotationsEvent {
        public Pre(RenderLivingBase<EntityLivingBase> renderer, EntityLivingBase entity, float partialTicks) {
            super(renderer, entity, partialTicks);
        }
    }

    public static class Post extends ApplyRenderRotationsEvent {
        public Post(RenderLivingBase<EntityLivingBase> renderer, EntityLivingBase entity, float partialTicks) {
            super(renderer, entity, partialTicks);
        }
    }
}
