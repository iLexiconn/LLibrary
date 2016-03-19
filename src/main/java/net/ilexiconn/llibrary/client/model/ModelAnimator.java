package net.ilexiconn.llibrary.client.model;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

@SideOnly(Side.CLIENT)
public class ModelAnimator {
    private int tempTick;
    private int prevTempTick;
    private boolean correctAnimation;
    private ModelBase model;
    private IAnimatedEntity entity;
    private HashMap<ModelRenderer, Transform> transformMap;
    private HashMap<ModelRenderer, Transform> prevTransformMap;

    public ModelAnimator(ModelBase model) {
        this.tempTick = 0;
        this.correctAnimation = false;
        this.model = model;
        this.transformMap = new HashMap<ModelRenderer, Transform>();
        this.prevTransformMap = new HashMap<ModelRenderer, Transform>();
    }

    public IAnimatedEntity getEntity() {
        return entity;
    }

    public void update(IAnimatedEntity entity) {
        this.tempTick = this.prevTempTick = 0;
        this.correctAnimation = false;
        this.entity = entity;
        this.transformMap.clear();
        this.prevTransformMap.clear();
        for (int i = 0; i < this.model.boxList.size(); i++) {
            ModelRenderer box = this.model.boxList.get(i);
            box.rotateAngleX = 0.0F;
            box.rotateAngleY = 0.0F;
            box.rotateAngleZ = 0.0F;
        }
    }

    public boolean setAnimation(Animation animation) {
        this.tempTick = this.prevTempTick = 0;
        this.correctAnimation = this.entity.getAnimation() == animation;
        return this.correctAnimation;
    }

    public void startPhase(int duration) {
        if (!this.correctAnimation) {
            return;
        }
        this.prevTempTick = this.tempTick;
        this.tempTick += duration;
    }

    public void setStationaryPhase(int duration) {
        this.startPhase(duration);
        this.endPhase(true);
    }

    public void resetPhase(int duration) {
        this.startPhase(duration);
        this.endPhase();
    }

    public void rotate(ModelRenderer box, float x, float y, float z) {
        if (!this.correctAnimation) {
            return;
        }
        if (!this.transformMap.containsKey(box)) {
            this.transformMap.put(box, new Transform(x, y, z));
        } else {
            this.transformMap.get(box).addRotation(x, y, z);
        }
    }

    public void move(ModelRenderer box, float x, float y, float z) {
        if (!this.correctAnimation) {
            return;
        }
        if (!this.transformMap.containsKey(box)) {
            this.transformMap.put(box, new Transform(x, y, z, 0.0F, 0.0F, 0.0F));
        } else {
            this.transformMap.get(box).addOffset(x, y, z);
        }
    }

    public void endPhase() {
        this.endPhase(false);
    }

    private void endPhase(boolean stationary) {
        if (!this.correctAnimation) {
            return;
        }
        int animationTick = this.entity.getAnimationTick();

        if (animationTick >= this.prevTempTick && animationTick < this.tempTick) {
            if (stationary) {
                for (ModelRenderer box : this.prevTransformMap.keySet()) {
                    Transform transform = this.prevTransformMap.get(box);
                    box.rotateAngleX += transform.rotX;
                    box.rotateAngleY += transform.rotY;
                    box.rotateAngleZ += transform.rotZ;
                    box.rotationPointX += transform.offsetX;
                    box.rotationPointY += transform.offsetY;
                    box.rotationPointZ += transform.offsetZ;
                }
            } else {
                float tick = (animationTick - this.prevTempTick + LLibrary.PROXY.getPartialTicks()) / (this.tempTick - this.prevTempTick);
                float inc = MathHelper.sin((float) (tick * Math.PI / 2F)), dec = 1F - inc;
                for (ModelRenderer box : this.prevTransformMap.keySet()) {
                    Transform transform = this.prevTransformMap.get(box);
                    box.rotateAngleX += dec * transform.rotX;
                    box.rotateAngleY += dec * transform.rotY;
                    box.rotateAngleZ += dec * transform.rotZ;
                    box.rotationPointX += dec * transform.offsetX;
                    box.rotationPointY += dec * transform.offsetY;
                    box.rotationPointZ += dec * transform.offsetZ;
                }
                for (ModelRenderer box : this.transformMap.keySet()) {
                    Transform transform = this.transformMap.get(box);
                    box.rotateAngleX += inc * transform.rotX;
                    box.rotateAngleY += inc * transform.rotY;
                    box.rotateAngleZ += inc * transform.rotZ;
                    box.rotationPointX += inc * transform.offsetX;
                    box.rotationPointY += inc * transform.offsetY;
                    box.rotationPointZ += inc * transform.offsetZ;
                }
            }
        }

        if (!stationary) {
            this.prevTransformMap.clear();
            this.prevTransformMap.putAll(this.transformMap);
            this.transformMap.clear();
        }
    }
}
