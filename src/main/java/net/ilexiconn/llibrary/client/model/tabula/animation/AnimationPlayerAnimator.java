package net.ilexiconn.llibrary.client.model.tabula.animation;

import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaAnimationComponentContainer;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaAnimationContainer;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.animation.NamedAnimation;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class AnimationPlayerAnimator<T extends Entity&IAnimatedEntity> implements ITabulaModelAnimator<T> {
    @Override
    public void setRotationAngles(TabulaModel model, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        Animation anim = entity.getAnimation();
        TabulaAnimationContainer animation;
        if(anim instanceof NamedAnimation) {
            animation = model.getAnimation(((NamedAnimation) anim).getName());
        } else {
            return;
        }

        if(animation == null)
            return;

        model.resetToDefaultPose();

        animation.getComponents().entrySet().forEach(entry -> {
            for(TabulaAnimationComponentContainer component : entry.getValue()) {
                applyComponent(component, model, model.getCubeByIdentifier(entry.getKey()), entity);
            }
        });
    }

    private void applyComponent(TabulaAnimationComponentContainer component, TabulaModel model, AdvancedModelRenderer cube, T entity) {
        int tick = entity.getAnimationTick();
        double progress = (tick - component.getStartKey()) / (double)component.getLength();
        progress = MathHelper.clamp(progress, 0.0, 1.0);
        if(tick >= component.getStartKey()) {
            cube.rotateAngleX += Math.toRadians(component.getRotationOffset()[0]);
            cube.rotateAngleY += Math.toRadians(component.getRotationOffset()[1]);
            cube.rotateAngleZ += Math.toRadians(component.getRotationOffset()[2]);

            cube.rotationPointX += component.getPositionOffset()[0];
            cube.rotationPointY += component.getPositionOffset()[1];
            cube.rotationPointZ += component.getPositionOffset()[2];

            cube.scaleX += component.getScaleOffset()[0];
            cube.scaleY += component.getScaleOffset()[1];
            cube.scaleZ += component.getScaleOffset()[2];

            cube.opacity += component.getOpacityOffset();
        }
        cube.rotateAngleX += Math.toRadians(component.getRotationChange()[0] * progress);
        cube.rotateAngleY += Math.toRadians(component.getRotationChange()[1] * progress);
        cube.rotateAngleZ += Math.toRadians(component.getRotationChange()[2] * progress);

        cube.rotationPointX += component.getPositionChange()[0] * progress;
        cube.rotationPointY += component.getPositionChange()[1] * progress;
        cube.rotationPointZ += component.getPositionChange()[2] * progress;

        cube.scaleX += component.getScaleChange()[0] * progress;
        cube.scaleY += component.getScaleChange()[1] * progress;
        cube.scaleZ += component.getScaleChange()[2] * progress;

        cube.opacity += component.getOpacityChange() * progress;
    }
}
