package net.ilexiconn.llibrary.server.animation;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public interface IAnimatedEntity {
    /**
     * An empty animation container. Always use this field as first value in the animation array.
     */
    Animation NO_ANIMATION = Animation.create(0, 0);

    /**
     * @return the current animation tick
     */
    int getAnimationTick();

    /**
     * Sets the current animation tick to the given value
     */
    void setAnimationTick(int tick);

    /**
     * @return the current playing animation
     */
    Animation getAnimation();

    /**
     * Sets the currently playing animation
     */
    void setAnimation(Animation animation);

    /**
     * @return an array of all the Animations this assets.testmod.models.entity can play
     */
    Animation[] getAnimations();
}
