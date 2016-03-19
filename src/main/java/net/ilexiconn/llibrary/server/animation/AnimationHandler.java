package net.ilexiconn.llibrary.server.animation;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.network.AnimationMessage;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;

public enum AnimationHandler {
    INSTANCE;

    public void sendAnimationMessage(IAnimatedEntity entity, Animation animation) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            return;
        }
        entity.setAnimation(animation);
        LLibrary.NETWORK_WRAPPER.sendToAll(new AnimationMessage(((Entity) entity).getEntityId(), animation));
    }

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
