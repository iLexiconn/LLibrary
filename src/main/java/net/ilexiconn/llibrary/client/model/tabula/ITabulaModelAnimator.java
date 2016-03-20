package net.ilexiconn.llibrary.client.model.tabula;

import net.minecraft.entity.Entity;

public interface ITabulaModelAnimator<T extends Entity> {
    void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale);
}
