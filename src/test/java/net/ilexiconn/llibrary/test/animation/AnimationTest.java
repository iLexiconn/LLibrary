package net.ilexiconn.llibrary.test.animation;

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
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "AnimationTest")
public class AnimationTest {
    public static final Logger LOGGER = LogManager.getLogger();

    @Mod.Instance("AnimationTest")
    public static AnimationTest instance;

    @SidedProxy
    private static ServerProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }

    public static class ServerProxy {
        public void preInit() {
            EntityRegistry.registerModEntity(AnimationTestEntity.class, "animation_test_entity", 0, instance, 64, 1, false);
        }
    }

    public static class ClientProxy extends ServerProxy {
        public void preInit() {
            super.preInit();
            RenderingRegistry.registerEntityRenderingHandler(AnimationTestEntity.class, manager -> {
                return new RenderLiving<AnimationTestEntity>(manager, new TestModel(), 0.0F) {
                    @Override
                    protected ResourceLocation getEntityTexture(AnimationTestEntity entity) {
                        return new ResourceLocation("missingno");
                    }
                };
            });
        }
    }

    public static class AnimationTestEntity extends EntityLiving implements IAnimatedEntity {
        private int animationTick;
        private Animation animation;

        private static final Animation TEST_ANIMATION = Animation.create(1, 30);

        public AnimationTestEntity(World world) {
            super(world);
        }

        @Override
        public void onUpdate() {
            super.onUpdate();
            AnimationHandler.INSTANCE.updateAnimations(this);
            if (animation.getID() != TEST_ANIMATION.getID()) {
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
            return new Animation[]{NO_ANIMATION, TEST_ANIMATION};
        }
    }

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

            animator = new ModelAnimator(this);

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
            animator.setStationaryKeyframe(6);
            animator.resetKeyframe(10);
        }
    }
}