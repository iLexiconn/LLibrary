package net.ilexiconn.llibrary.test;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@Mod(modid = "AnimationTest")
public class AnimationTest {
    @Mod.Instance("AnimationTest")
    public static AnimationTest INSTANCE;
    @SidedProxy(serverSide = "net.ilexiconn.llibrary.test.AnimationTest$ServerProxy", clientSide = "net.ilexiconn.llibrary.test.AnimationTest$ClientProxy")
    public static ServerProxy PROXY;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        AnimationTest.PROXY.onPreInit();
    }

    public static class ServerProxy {
        public void onPreInit() {
            EntityRegistry.registerModEntity(AnimationTestEntity.class, "animation_test_entity", 0, INSTANCE, 64, 1, false);
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ClientProxy extends ServerProxy {
        @Override
        public void onPreInit() {
            super.onPreInit();
            RenderingRegistry.registerEntityRenderingHandler(AnimationTestEntity.class, new RenderLiving(new TestModel(), 0.0F) {
                @Override
                protected ResourceLocation getEntityTexture(Entity entity) {
                    return new ResourceLocation("missingno");
                }
            });
        }
    }

    public static class AnimationTestEntity extends EntityLiving implements IAnimatedEntity {
        private int animationTick;
        private Animation animation;

        private static final Animation TEST_ANIMATION = Animation.create(30);

        public AnimationTestEntity(World world) {
            super(world);
        }

        @Override
        public void onUpdate() {
            super.onUpdate();
            AnimationHandler.INSTANCE.updateAnimations(this);
            if (animation != TEST_ANIMATION) {
                setAnimation(TEST_ANIMATION);
                AnimationHandler.INSTANCE.sendAnimationMessage(this, TEST_ANIMATION);
            }
        }

        @Override
        public int getAnimationTick() {
            return animationTick;
        }

        @Override
        public void setAnimationTick(int tick) {
            this.animationTick = tick;
        }

        @Override
        public Animation getAnimation() {
            return animation;
        }

        @Override
        public void setAnimation(Animation animation) {
            this.animation = animation;
        }

        @Override
        public Animation[] getAnimations() {
            return new Animation[]{TEST_ANIMATION};
        }
    }

    @SideOnly(Side.CLIENT)
    public static class TestModel extends AdvancedModelBase {
        public AdvancedModelRenderer body, head;
        public ModelAnimator animator;

        public TestModel() {
            super();
            body = new AdvancedModelRenderer(this, 32, 0);
            body.addBox(-3f, -10f, -3f, 6, 10, 6);
            body.setRotationPoint(0f, 24f, 0f);
            head = new AdvancedModelRenderer(this, 0, 0);
            head.addBox(-4f, -8f, -4f, 8, 8, 8);
            head.setRotationPoint(0f, -10f, 0f);
            body.addChild(head);

            animator = ModelAnimator.create();

            updateDefaultPose();
        }

        @Override
        public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
            body.render(f5);
        }

        @Override
        public void setLivingAnimations(EntityLivingBase entity, float f, float f1, float f2) {
            animator.update((IAnimatedEntity) entity);

            resetToDefaultPose();

            animator.setAnimation(AnimationTestEntity.TEST_ANIMATION);
            animator.startKeyframe(10);
            animator.rotate(head, (float) (-Math.PI / 6f), 0f, 0f);
            animator.rotate(body, (float) (-Math.PI / 6f), 0f, 0f);
            animator.endKeyframe();
            animator.startKeyframe(4);
            animator.rotate(body, (float) (Math.PI / 2f), 0f, 0f);
            animator.move(body, 0f, -2f, 0f);
            animator.endKeyframe();
            animator.setStaticKeyframe(6);
            animator.resetKeyframe(10);
        }
    }
}