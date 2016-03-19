package net.ilexiconn.llibrary.server.animation;

public interface IAnimatedEntity {
    Animation ANIMATION_NONE = Animation.create(0, 0);

    int getAnimationTick();

    void setAnimationTick(int tick);

    Animation getAnimation();

    void setAnimation(Animation animation);

    Animation[] getAnimations();
}
