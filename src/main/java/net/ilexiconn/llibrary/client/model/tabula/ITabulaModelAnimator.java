package net.ilexiconn.llibrary.client.model.tabula;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;

/**
 * @author gegy1000
 * @since 1.0.0
 */
@SideOnly(Side.CLIENT)
@FunctionalInterface
public interface ITabulaModelAnimator<T extends Entity> {
    void setRotationAngles(TabulaModel model, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale);
}
