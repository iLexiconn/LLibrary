package net.ilexiconn.llibrary.client.model.tools;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author gegy1000
 * @since 1.0.0
 */
@SideOnly(Side.CLIENT)
public class ChainBuffer {
    private int yawTimer;
    private float yawVariation;
    private int pitchTimer;
    private float pitchVariation;
    private float[] yawArray;
    private float[] pitchArray;

    public ChainBuffer(int size) {
        this.yawArray = new float[size];
        this.pitchArray = new float[size];
    }

    public void resetRotations() {
        this.yawVariation = 0.0F;
        this.pitchVariation = 0.0F;
    }

    public void calculateChainSwingBuffer(float maxAngle, int bufferTime, float angleDecrement, float divisor, EntityLivingBase entity) {
        if (entity.renderYawOffset != entity.prevRenderYawOffset && MathHelper.abs(this.yawVariation) < maxAngle) {
            this.yawVariation += (entity.prevRenderYawOffset - entity.renderYawOffset) / divisor;
        }
        if (this.yawVariation > 0.7F * angleDecrement) {
            if (this.yawTimer > bufferTime) {
                this.yawVariation -= angleDecrement;
                if (MathHelper.abs(this.yawVariation) < angleDecrement) {
                    this.yawVariation = 0.0F;
                    this.yawTimer = 0;
                }
            } else {
                this.yawTimer++;
            }
        } else if (this.yawVariation < -0.7F * angleDecrement) {
            if (this.yawTimer > bufferTime) {
                this.yawVariation += angleDecrement;
                if (MathHelper.abs(this.yawVariation) < angleDecrement) {
                    this.yawVariation = 0.0F;
                    this.yawTimer = 0;
                }
            } else {
                this.yawTimer++;
            }
        }
        for (int i = 0; i < yawArray.length; i++) {
            this.yawArray[i] = 0.01745329251F * yawVariation / pitchArray.length;
        }
    }

    public void calculateChainWaveBuffer(float maxAngle, int bufferTime, float angleDecrement, float divisor, EntityLivingBase entity) {
        if (entity.rotationPitch != entity.prevRotationPitch && MathHelper.abs(pitchVariation) < maxAngle) {
            this.pitchVariation += (entity.prevRotationPitch - entity.rotationPitch) / divisor;
        }
        if (this.pitchVariation > 0.7F * angleDecrement) {
            if (this.pitchTimer > bufferTime) {
                this.pitchVariation -= angleDecrement;
                if (MathHelper.abs(this.pitchVariation) < angleDecrement) {
                    this.pitchVariation = 0.0F;
                    this.pitchTimer = 0;
                }
            } else {
                this.pitchTimer++;
            }
        } else if (this.pitchVariation < -0.7F * angleDecrement) {
            if (this.pitchTimer > bufferTime) {
                this.pitchVariation += angleDecrement;
                if (MathHelper.abs(this.pitchVariation) < angleDecrement) {
                    this.pitchVariation = 0.0F;
                    this.pitchTimer = 0;
                }
            } else {
                this.pitchTimer++;
            }
        }
        for (int i = 0; i < this.pitchArray.length; i++) {
            this.pitchArray[i] = 0.01745329251F * this.pitchVariation / this.pitchArray.length;
        }
    }

    public void calculateChainSwingBuffer(float maxAngle, int bufferTime, float angleDecrement, EntityLivingBase entity) {
        if (entity.renderYawOffset != entity.prevRenderYawOffset && MathHelper.abs(this.yawVariation) < maxAngle) {
            this.yawVariation += (entity.prevRenderYawOffset - entity.renderYawOffset);
        }
        if (this.yawVariation > 0.7F * angleDecrement) {
            if (this.yawTimer > bufferTime) {
                this.yawVariation -= angleDecrement;
                if (MathHelper.abs(this.yawVariation) < angleDecrement) {
                    this.yawVariation = 0.0F;
                    this.yawTimer = 0;
                }
            } else {
                this.yawTimer++;
            }
        } else if (this.yawVariation < -0.7F * angleDecrement) {
            if (this.yawTimer > bufferTime) {
                this.yawVariation += angleDecrement;
                if (MathHelper.abs(this.yawVariation) < angleDecrement) {
                    this.yawVariation = 0.0F;
                    this.yawTimer = 0;
                }
            } else {
                this.yawTimer++;
            }
        }
        for (int i = 0; i < this.yawArray.length; i++) {
            this.yawArray[i] = 0.01745329251F * this.yawVariation / this.pitchArray.length;
        }
    }

    public void calculateChainWaveBuffer(float maxAngle, int bufferTime, float angleDecrement, EntityLivingBase entity) {
        if (entity.rotationPitch != entity.prevRotationPitch && MathHelper.abs(this.pitchVariation) < maxAngle) {
            this.pitchVariation += (entity.prevRotationPitch - entity.rotationPitch);
        }

        if (this.pitchVariation > 0.7F * angleDecrement) {
            if (this.pitchTimer > bufferTime) {
                this.pitchVariation -= angleDecrement;
                if (MathHelper.abs(pitchVariation) < angleDecrement) {
                    this.pitchVariation = 0.0F;
                    this.pitchTimer = 0;
                }
            } else {
                this.pitchTimer++;
            }
        } else if (this.pitchVariation < -0.7F * angleDecrement) {
            if (this.pitchTimer > bufferTime) {
                this.pitchVariation += angleDecrement;
                if (MathHelper.abs(this.pitchVariation) < angleDecrement) {
                    this.pitchVariation = 0.0F;
                    this.pitchTimer = 0;
                }
            } else {
                this.pitchTimer++;
            }
        }

        for (int i = 0; i < this.pitchArray.length; i++) {
            this.pitchArray[i] = 0.01745329251F * this.pitchVariation / this.pitchArray.length;
        }
    }

    public void applyChainSwingBuffer(AdvancedModelRenderer[] boxes) {
        if (boxes.length == this.yawArray.length) {
            for (int i = 0; i < boxes.length; i++) {
                boxes[i].rotateAngleY += this.yawArray[i];
            }
        } else {
            LLibrary.LOGGER.error("Wrong array length used in the y-axis buffer!");
        }
    }

    public void applyChainWaveBuffer(AdvancedModelRenderer[] boxes) {
        if (boxes.length == this.pitchArray.length) {
            for (int i = 0; i < boxes.length; i++) {
                boxes[i].rotateAngleX += this.pitchArray[i];
            }
        } else {
            LLibrary.LOGGER.error("Wrong array length used in the x-axis buffer!");
        }
    }
}