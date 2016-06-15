package net.ilexiconn.llibrary.server.entity.multipart;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public interface IMultipartEntity {
    Entity[] getParts();

    default void updateParts() {
        for (Entity entity : this.getParts()) {
            entity.onUpdate();
        }
    }

    default PartEntity create(float radius, float angleYaw, float offsetY, float sizeX, float sizeY) {
        return this.create(radius, angleYaw, offsetY, sizeX, sizeY, 1.0F);
    }

    default PartEntity create(float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        return new PartEntity((EntityLiving) this, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
    }
}
