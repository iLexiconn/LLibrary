package net.ilexiconn.llibrary.server.event;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class AnimationEvent extends Event {
    protected Entity entity;
    protected Animation animation;

    public AnimationEvent(Entity entity, Animation animation) {
        this.entity = entity;
        this.animation = animation;
    }

    @Cancelable
    public static class Start extends AnimationEvent {
        public Start(Entity entity, Animation animation) {
            super(entity, animation);
        }

        public void setAnimation(Animation animation) {
            this.animation = animation;
        }
    }

    public static class Tick extends AnimationEvent {
        protected int tick;

        public Tick(Entity entity, Animation animation, int tick) {
            super(entity, animation);
            this.tick = tick;
        }

        public int getTick() {
            return tick;
        }
    }

    public Entity getEntity() {
        return entity;
    }

    public Animation getAnimation() {
        return animation;
    }
}
