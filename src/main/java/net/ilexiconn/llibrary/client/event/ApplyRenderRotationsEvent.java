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
public class ApplyRenderRotationsEvent<T extends EntityLivingBase> extends Event {
    protected RenderLivingBase<T> renderer;
    protected T entity;
    protected float partialTicks;

    ApplyRenderRotationsEvent(RenderLivingBase<T> renderer, T entity, float partialTicks) {
        this.renderer = renderer;
        this.entity = entity;
        this.partialTicks = partialTicks;
    }

    public RenderLivingBase<T> getRenderer() {
        return this.renderer;
    }

    public T getEntity() {
        return this.entity;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public static class Pre<T extends EntityLivingBase> extends ApplyRenderRotationsEvent<T> {
        public Pre(RenderLivingBase<T> renderer, T entity, float partialTicks) {
            super(renderer, entity, partialTicks);
        }
    }

    public static class Post<T extends EntityLivingBase> extends ApplyRenderRotationsEvent<T> {
        public Post(RenderLivingBase<T> renderer, T entity, float partialTicks) {
            super(renderer, entity, partialTicks);
        }
    }
}
