package net.ilexiconn.llibrary.server.event;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class AnimationEvent<T extends Entity & IAnimatedEntity> extends Event {
    protected T entity;
    protected Animation animation;

    public AnimationEvent(T entity, Animation animation) {
        this.entity = entity;
        this.animation = animation;
    }

    @Cancelable
    public static class Start<T extends Entity & IAnimatedEntity> extends AnimationEvent {
        public Start(T entity, Animation animation) {
            super(entity, animation);
        }

        public void setAnimation(Animation animation) {
            this.animation = animation;
        }
    }

    public static class Tick<T extends Entity & IAnimatedEntity> extends AnimationEvent {
        protected int tick;

        public Tick(T entity, Animation animation, int tick) {
            super(entity, animation);
            this.tick = tick;
        }

        public int getTick() {
            return tick;
        }
    }

    public T getEntity() {
        return entity;
    }

    public Animation getAnimation() {
        return animation;
    }
}
