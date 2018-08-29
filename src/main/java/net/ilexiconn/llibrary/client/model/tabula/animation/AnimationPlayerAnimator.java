package net.ilexiconn.llibrary.client.model.tabula.animation;

import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraft.entity.Entity;

public class AnimationPlayerAnimator<T extends Entity> implements ITabulaModelAnimator<T> {
    @Override
    public void setRotationAngles(TabulaModel model, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {

    }
}
