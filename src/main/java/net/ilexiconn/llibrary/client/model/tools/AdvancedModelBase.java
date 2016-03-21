package net.ilexiconn.llibrary.client.model.tools;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * An enhanced ModelBase
 *
 * @author gegy1000
 * @since 1.0.0
 */
@SideOnly(Side.CLIENT)
public class AdvancedModelBase extends ModelBase {
    private float movementScale = 1.0F;

    /**
     * Sets the default pose to the current pose of this model
     */
    public void updateDefaultPose() {
        this.boxList.stream().filter(modelRenderer -> modelRenderer instanceof AdvancedModelRenderer).forEach(modelRenderer -> {
            AdvancedModelRenderer advancedModelRenderer = (AdvancedModelRenderer) modelRenderer;
            advancedModelRenderer.updateDefaultPose();
        });
    }

    /**
     * Sets the current pose to the previously set default pose
     */
    public void resetToDefaultPose() {
        this.boxList.stream().filter(modelRenderer -> modelRenderer instanceof AdvancedModelRenderer).forEach(modelRenderer -> {
            AdvancedModelRenderer advancedModelRenderer = (AdvancedModelRenderer) modelRenderer;
            advancedModelRenderer.resetToDefaultPose();
        });
    }

    /**
     * Rotates the given boxes to face a given target
     *
     * @param yaw             the yaw to face
     * @param pitch           the pitch to face
     * @param rotationDivisor the amount to divide the rotation angles by
     * @param boxes           the boxes to face the given target
     */
    public void faceTarget(float yaw, float pitch, float rotationDivisor, AdvancedModelRenderer... boxes) {
        float actualRotationDivisor = rotationDivisor / boxes.length;
        float yawAmount = yaw / (180.0F / (float) Math.PI) / actualRotationDivisor;
        float pitchAmount = pitch / (180.0F / (float) Math.PI) / actualRotationDivisor;
        for (AdvancedModelRenderer box : boxes) {
            box.rotateAngleY += yawAmount;
            box.rotateAngleX += pitchAmount;
        }
    }

    /**
     * Swings (rotates on the Y axis) the given model parts in a chain-like manner.
     *
     * @param speed      the speed to swing this at
     * @param degree     the amount to rotate this by
     * @param rootOffset the root rotation offset
     * @param boxes      the boxes to swing
     */
    public void chainSwing(float speed, float degree, double rootOffset, float swing, float swingAmount, AdvancedModelRenderer... boxes) {
        float offset = calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; index++) {
            boxes[index].rotateAngleY += calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }
    }

    /**
     * Waves (rotates on the X axis) the given model parts in a chain-like manner.
     *
     * @param speed      the speed to wave this at
     * @param degree     the amount to rotate this by
     * @param rootOffset the root rotation offset
     * @param boxes      the boxes to wave
     */
    public void chainWave(float speed, float degree, double rootOffset, float swing, float swingAmount, AdvancedModelRenderer... boxes) {
        float offset = calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; index++) {
            boxes[index].rotateAngleX += calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }
    }

    /**
     * Flaps (rotates on the Z axis) the given model parts in a chain-like manner.
     *
     * @param speed      the speed to flap this at
     * @param degree     the amount to rotate this by
     * @param rootOffset the root rotation offset
     * @param boxes      the boxes to flap
     */
    public void chainFlap(float speed, float degree, double rootOffset, float swing, float swingAmount, AdvancedModelRenderer... boxes) {
        float offset = calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; index++) {
            boxes[index].rotateAngleZ += calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }
    }

    private float calculateChainRotation(float speed, float degree, float swing, float swingAmount, float offset, int boxIndex) {
        return MathHelper.cos(swing * (speed * movementScale) + offset * boxIndex) * swingAmount * (degree * movementScale);
    }

    private float calculateChainOffset(double rootOffset, AdvancedModelRenderer... boxes) {
        return (float) ((rootOffset * Math.PI) / (2 * boxes.length));
    }

    /**
     * Multiplies all rotation and position changes by this value
     */
    public void setMovementScale(float movementScale) {
        this.movementScale = movementScale;
    }

    public float getMovementScale() {
        return this.movementScale;
    }
}
