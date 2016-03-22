package net.ilexiconn.llibrary.server.animation;

import cpw.mods.fml.common.FMLCommonHandler;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.network.AnimationMessage;
import net.minecraft.entity.Entity;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public enum AnimationHandler {
    INSTANCE;

    /**
     * Sends an animation packet to all clients, notifying them of a changed animation
     *
     * @param entity    the entity with an animation to be updated
     * @param animation the animation to be updated
     */
    public void sendAnimationMessage(IAnimatedEntity entity, Animation animation) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            return;
        }
        entity.setAnimation(animation);
        LLibrary.NETWORK_WRAPPER.sendToAll(new AnimationMessage(((Entity) entity).getEntityId(), animation));
    }

    /**
     * Updates all animations for a given entity
     */
    public void updateAnimations(IAnimatedEntity entity) {
        if (entity.getAnimation() == null) {
            entity.setAnimation(entity.getAnimations()[0]);
        } else {
            if (entity.getAnimation().getID() != 0) {
                if (entity.getAnimationTick() == 0) {
                    sendAnimationMessage(entity, entity.getAnimation());
                }
                if (entity.getAnimationTick() < entity.getAnimation().getDuration()) {
                    entity.setAnimationTick(entity.getAnimationTick() + 1);
                }
                if (entity.getAnimationTick() == entity.getAnimation().getDuration()) {
                    entity.setAnimationTick(0);
                    entity.setAnimation(entity.getAnimations()[0]);
                }
            }
        }
    }
}
