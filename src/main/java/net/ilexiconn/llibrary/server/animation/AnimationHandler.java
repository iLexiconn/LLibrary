package net.ilexiconn.llibrary.server.animation;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.event.AnimationEvent;
import net.ilexiconn.llibrary.server.network.AnimationMessage;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Arrays;

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
     * @param <T>       the animated entity type
     */
    public <T extends Entity & IAnimatedEntity> void sendAnimationMessage(T entity, Animation animation) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            return;
        }
        entity.setAnimation(animation);
        LLibrary.NETWORK_WRAPPER.sendToAll(new AnimationMessage(entity.getEntityId(), Arrays.asList(entity.getAnimations()).indexOf(animation)));
    }

    /**
     * Updates all animations for a given entity
     *
     * @param entity the entity with an animation to be updated
     * @param <T>    the animated entity type
     */
    public <T extends Entity & IAnimatedEntity> void updateAnimations(T entity) {
        if (entity.getAnimation() == null) {
            entity.setAnimation(entity.getAnimations()[0]);
        } else {
            if (entity.getAnimation() != IAnimatedEntity.NO_ANIMATION) {
                if (entity.getAnimationTick() == 0) {
                    AnimationEvent event = new AnimationEvent.Start(entity, entity.getAnimation());
                    if (!MinecraftForge.EVENT_BUS.post(event)) {
                        sendAnimationMessage(entity, event.getAnimation());
                    }
                }
                if (entity.getAnimationTick() < entity.getAnimation().getDuration()) {
                    entity.setAnimationTick(entity.getAnimationTick() + 1);
                    MinecraftForge.EVENT_BUS.post(new AnimationEvent.Tick(entity, entity.getAnimation(), entity.getAnimationTick()));
                }
                if (entity.getAnimationTick() == entity.getAnimation().getDuration()) {
                    entity.setAnimationTick(0);
                    entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
                }
            }
        }
    }
}
