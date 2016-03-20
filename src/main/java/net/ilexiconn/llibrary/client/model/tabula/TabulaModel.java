package net.ilexiconn.llibrary.client.model.tabula;

import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.container.*;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class TabulaModel extends AdvancedModelBase {
    private Map<String, AdvancedModelRenderer> cubes = new HashMap<>();
    private List<AdvancedModelRenderer> rootBoxes = new ArrayList<>();
    private TabulaModelContainer container;
    private ITabulaModelAnimator tabulaAnimator;
    private ModelAnimator modelAnimator;
    private Map<TabulaAnimationContainer, Animation> animations = new HashMap<>();
    private Map<String, AdvancedModelRenderer> identifierMap = new HashMap<>();

    public TabulaModel(TabulaModelContainer container, ITabulaModelAnimator tabulaAnimator) {
        this.textureWidth = container.getTextureWidth();
        this.textureHeight = container.getTextureHeight();
        this.container = container;
        this.tabulaAnimator = tabulaAnimator;
        this.modelAnimator = new ModelAnimator(this);
        for (TabulaCubeContainer cube : container.getCubes()) {
            this.parseCube(cube, null);
        }
        container.getCubeGroups().forEach(this::parseCubeGroup);
        this.updateDefaultPose();
    }

    public TabulaModel(TabulaModelContainer container) {
        this(container, null);
    }

    private void parseCubeGroup(TabulaCubeGroupContainer container) {
        for (TabulaCubeContainer cube : container.getCubes()) {
            parseCube(cube, null);
        }
        container.getCubeGroups().forEach(this::parseCubeGroup);
    }

    private void parseCube(TabulaCubeContainer cube, AdvancedModelRenderer parent) {
        AdvancedModelRenderer box = this.createBox(cube);
        this.cubes.put(cube.getName(), box);
        this.identifierMap.put(cube.getIdentifier(), box);
        if (parent != null) {
            parent.addChild(box);
        } else {
            this.rootBoxes.add(box);
        }
        for (TabulaCubeContainer child : cube.getChildren()) {
            this.parseCube(child, box);
        }
    }

    private AdvancedModelRenderer createBox(TabulaCubeContainer cube) {
        int[] textureOffset = cube.getTextureOffset();
        double[] position = cube.getPosition();
        double[] rotation = cube.getRotation();
        double[] offset = cube.getOffset();
        int[] dimensions = cube.getDimensions();

        AdvancedModelRenderer box = new AdvancedModelRenderer(this, textureOffset[0], textureOffset[1]);
        box.setRotationPoint((float) position[0], (float) position[1], (float) position[2]);
        box.addBox((float) offset[0], (float) offset[1], (float) offset[2], dimensions[0], dimensions[1], dimensions[2], 0.0F);
        box.rotateAngleX = (float) Math.toRadians(rotation[0]);
        box.rotateAngleY = (float) Math.toRadians(rotation[1]);
        box.rotateAngleZ = (float) Math.toRadians(rotation[2]);

        return box;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, scale, entity);
        GlStateManager.pushMatrix();
        double[] modelScale = this.container.getScale();
        GlStateManager.scale(modelScale[0], modelScale[1], modelScale[2]);
        for (AdvancedModelRenderer box : this.rootBoxes) {
            box.render(scale);
        }
        GlStateManager.popMatrix();
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale, Entity entity) {
        this.resetToDefaultPose();
        if (this.tabulaAnimator != null) {
            this.tabulaAnimator.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, scale);
        }
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entity, float f, float f1, float f2) {
        if (entity instanceof IAnimatedEntity) {
            this.resetToDefaultPose();
            this.modelAnimator.update((IAnimatedEntity) entity);
            for (Map.Entry<TabulaAnimationContainer, Animation> entry : this.animations.entrySet()) {
                TabulaAnimationContainer container = entry.getKey();
                this.modelAnimator.setAnimation(entry.getValue());
                for (Map.Entry<String, List<TabulaAnimationComponentContainer>> componentEntry : container.getComponents().entrySet()) {
                    List<TabulaAnimationComponentContainer> componentContainers = componentEntry.getValue();
                    AdvancedModelRenderer box = this.identifierMap.get(componentEntry.getKey());
                    for (TabulaAnimationComponentContainer componentContainer : componentContainers) {
                        this.modelAnimator.startKeyframe(componentContainer.getLength());
                        double[] positionChange = componentContainer.getPositionChange();
                        this.modelAnimator.move(box, (float) positionChange[0], (float) positionChange[1], (float) positionChange[2]);
                        double[] rotationChange = componentContainer.getRotationChange();
                        this.modelAnimator.rotate(box, (float) rotationChange[0], (float) rotationChange[1], (float) rotationChange[2]);
                        this.modelAnimator.endKeyframe();
                    }
                }
            }
        }
    }

    public AdvancedModelRenderer getCube(String name) {
        return this.cubes.get(name);
    }

    public Map<String, AdvancedModelRenderer> getCubes() {
        return this.cubes;
    }

    public Animation getAnimation(String name, int id) {
        for (TabulaAnimationContainer animationContainer : this.container.getAnimations()) {
            if (name.equals(animationContainer.getName())) {
                int animationLength = 0;
                for (Map.Entry<String, List<TabulaAnimationComponentContainer>> entry : animationContainer.getComponents().entrySet()) {
                    for (TabulaAnimationComponentContainer component : entry.getValue()) {
                        int endKey = component.getEndKey();
                        if (endKey > animationLength) {
                            animationLength = endKey;
                        }
                    }
                }
                Animation animation = Animation.create(id, animationLength);
                this.animations.put(animationContainer, animation);
                return animation;
            }
        }
        return null;
    }
}
