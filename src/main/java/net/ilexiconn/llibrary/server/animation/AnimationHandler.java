package net.ilexiconn.llibrary.server.animation;

import cpw.mods.fml.common.FMLCommonHandler;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.event.AnimationEvent;
import net.ilexiconn.llibrary.server.network.AnimationMessage;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;

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
     */
    public void sendAnimationMessage(IAnimatedEntity entity, Animation animation) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            return;
        }
        entity.setAnimation(animation);
        LLibrary.NETWORK_WRAPPER.sendToAll(new AnimationMessage(((Entity) entity).getEntityId(), Arrays.asList(entity.getAnimations()).indexOf(animation)));
    }

    /**
     * Updates all animations for a given entity
     *
     * @param entity the entity with an animation to be updated
     */
    public void updateAnimations(IAnimatedEntity entity) {
        if (entity.getAnimation() == null) {
            entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
        } else {
            if (entity.getAnimation() != IAnimatedEntity.NO_ANIMATION) {
                if (entity.getAnimationTick() == 0) {
                    AnimationEvent event = new AnimationEvent.Start((Entity) entity, entity.getAnimation());
                    if (!MinecraftForge.EVENT_BUS.post(event)) {
                        sendAnimationMessage(entity, event.getAnimation());
                    }
                }
                if (entity.getAnimationTick() < entity.getAnimation().getDuration()) {
                    entity.setAnimationTick(entity.getAnimationTick() + 1);
                    MinecraftForge.EVENT_BUS.post(new AnimationEvent.Tick((Entity) entity, entity.getAnimation(), entity.getAnimationTick()));
                }
                if (entity.getAnimationTick() == entity.getAnimation().getDuration()) {
                    entity.setAnimationTick(0);
                    entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
                }
            }
        }
    }
}
