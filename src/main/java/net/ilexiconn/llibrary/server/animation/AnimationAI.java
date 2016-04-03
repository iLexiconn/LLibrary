package net.ilexiconn.llibrary.server.animation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public abstract class AnimationAI<T extends Entity & IAnimatedEntity> extends EntityAIBase {
    protected T entity;

    public AnimationAI(T entity) {
        this.entity = entity;
        this.setMutexBits(7);
    }

    public abstract Animation getAnimation();

    public boolean isAutomatic() {
        return false;
    }

    public boolean shouldAnimate() {
        return false;
    }

    @Override
    public boolean shouldExecute() {
        if (this.isAutomatic()) {
            return this.entity.getAnimation() == getAnimation();
        }
        return shouldAnimate();
    }

    @Override
    public void startExecuting() {
        if (!this.isAutomatic()) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, this.getAnimation());
        }
        this.entity.setAnimationTick(0);
    }

    @Override
    public boolean continueExecuting() {
        return this.entity.getAnimationTick() < this.getAnimation().getDuration();
    }

    @Override
    public void resetTask() {
        AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, IAnimatedEntity.NO_ANIMATION);
    }
}
