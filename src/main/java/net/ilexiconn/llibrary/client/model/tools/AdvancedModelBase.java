package net.ilexiconn.llibrary.client.model.tools;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AdvancedModelBase extends ModelBase {
    private float movementScale;

    public void updateDefaultPose() {
        this.boxList.stream().filter(modelRenderer -> modelRenderer instanceof AdvancedModelRenderer).forEach(modelRenderer -> {
            AdvancedModelRenderer advancedModelRenderer = (AdvancedModelRenderer) modelRenderer;
            advancedModelRenderer.updateDefaultPose();
        });
    }

    public void resetToDefaultPose() {
        this.boxList.stream().filter(modelRenderer -> modelRenderer instanceof AdvancedModelRenderer).forEach(modelRenderer -> {
            AdvancedModelRenderer advancedModelRenderer = (AdvancedModelRenderer) modelRenderer;
            advancedModelRenderer.resetToDefaultPose();
        });
    }

    public void faceTarget(float yaw, float pitch, float rotationDivisor, AdvancedModelRenderer... boxes) {
        float actualRotationDivisor = rotationDivisor / boxes.length;
        float yawAmount = yaw / (180.0F / (float) Math.PI) / actualRotationDivisor;
        float pitchAmount = pitch / (180.0F / (float) Math.PI) / actualRotationDivisor;
        for (AdvancedModelRenderer box : boxes) {
            box.rotateAngleY += yawAmount;
            box.rotateAngleX += pitchAmount;
        }
    }

    public void chainSwing(float speed, float degree, double rootOffset, float swing, float swingAmount, AdvancedModelRenderer... boxes) {
        float offset = calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; index++) {
            boxes[index].rotateAngleY += calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }
    }

    public void chainWave(float speed, float degree, double rootOffset, float swing, float swingAmount, AdvancedModelRenderer... boxes) {
        float offset = calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; index++) {
            boxes[index].rotateAngleX += calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }
    }

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

    public void setMovementScale(float movementScale) {
        this.movementScale = movementScale;
    }

    public float getMovementScale() {
        return this.movementScale;
    }
}
