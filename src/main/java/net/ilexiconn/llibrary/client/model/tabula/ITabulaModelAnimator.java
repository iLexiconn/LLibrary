package net.ilexiconn.llibrary.client.model.tabula;

import net.minecraft.entity.Entity;

@FunctionalInterface
public interface ITabulaModelAnimator<ENTITY extends Entity> {
    void setRotationAngles(ENTITY entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale);
}
