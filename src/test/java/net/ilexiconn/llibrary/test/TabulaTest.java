package net.ilexiconn.llibrary.test;

import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@Mod(modid = "TabulaTest")
public class TabulaTest {
    @Mod.Instance("TabulaTest")
    public static TabulaTest INSTANCE;
    @SidedProxy
    public static ServerProxy PROXY;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        TabulaTest.PROXY.onPreInit();
    }

    public static class ServerProxy {
        public void onPreInit() {
            EntityRegistry.registerModEntity(TabulaTestEntity.class, "tabula_test_entity", 0, INSTANCE, 64, 1, false);
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ClientProxy extends ServerProxy {
        @Override
        public void onPreInit() {
            super.onPreInit();
            RenderingRegistry.registerEntityRenderingHandler(TabulaTestEntity.class, manager -> {
                try {
                    return new RenderLiving<TabulaTestEntity>(manager, new TabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("assets/tabulatest/models/entity/tabula_model.tbl"), new Animator()), 0.0F) {
                        @Override
                        protected ResourceLocation getEntityTexture(TabulaTestEntity entity) {
                            return new ResourceLocation("missingno");
                        }
                    };
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            });
        }
    }

    public static class TabulaTestEntity extends EntityLiving {
        public TabulaTestEntity(World world) {
            super(world);
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Animator implements ITabulaModelAnimator<TabulaTestEntity> {
        @Override
        public void setRotationAngles(TabulaModel model, TabulaTestEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
            AdvancedModelRenderer waist = model.getCube("body3");
            AdvancedModelRenderer chest = model.getCube("body2");
            AdvancedModelRenderer shoulders = model.getCube("body1");
            AdvancedModelRenderer leftThigh = model.getCube("Left thigh");
            AdvancedModelRenderer rightThigh = model.getCube("Right thigh");
            AdvancedModelRenderer neck1 = model.getCube("neck1");
            AdvancedModelRenderer neck2 = model.getCube("neck2");
            AdvancedModelRenderer neck3 = model.getCube("neck3");
            AdvancedModelRenderer neck4 = model.getCube("neck4");
            AdvancedModelRenderer head = model.getCube("Head");
            AdvancedModelRenderer leftShin = model.getCube("Left shin");
            AdvancedModelRenderer rightShin = model.getCube("Right shin");
            AdvancedModelRenderer leftUpperFoot = model.getCube("Left upper foot");
            AdvancedModelRenderer leftFoot = model.getCube("Left foot");
            AdvancedModelRenderer rightUpperFoot = model.getCube("Right upper foot");
            AdvancedModelRenderer rightFoot = model.getCube("Right foot");
            AdvancedModelRenderer tail1 = model.getCube("tail1");
            AdvancedModelRenderer tail2 = model.getCube("tail2");
            AdvancedModelRenderer tail3 = model.getCube("tail3");
            AdvancedModelRenderer tail4 = model.getCube("tail4");
            AdvancedModelRenderer tail5 = model.getCube("tail5");
            AdvancedModelRenderer tail6 = model.getCube("tail6");

            AdvancedModelRenderer upperArmRight = model.getCube("Right arm");
            AdvancedModelRenderer upperArmLeft = model.getCube("Left arm");
            AdvancedModelRenderer lowerArmRight = model.getCube("Right forearm");
            AdvancedModelRenderer lowerArmLeft = model.getCube("Left forearm");
            AdvancedModelRenderer Hand_Right = model.getCube("Right hand");
            AdvancedModelRenderer Hand_Left = model.getCube("Left hand");

            AdvancedModelRenderer[] rightArmParts = new AdvancedModelRenderer[]{Hand_Right, lowerArmRight, upperArmRight};
            AdvancedModelRenderer[] leftArmParts = new AdvancedModelRenderer[]{Hand_Left, lowerArmLeft, upperArmLeft};
            AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[]{tail6, tail5, tail4, tail3, tail2, tail1};
            AdvancedModelRenderer[] bodyParts = new AdvancedModelRenderer[]{waist, chest, shoulders, neck4, neck3, neck2, neck1, head};

            float speed = 0.75F;
            float height = 2F * limbSwingAmount;

            waist.bob(1F * speed, height, false, limbSwing, limbSwingAmount);
            leftThigh.bob(1F * speed, height, false, limbSwing, limbSwingAmount);
            rightThigh.bob(1F * speed, height, false, limbSwing, limbSwingAmount);
            shoulders.walk(1F * speed, 0.2F, true, 1, 0, limbSwing, limbSwingAmount);
            chest.walk(1F * speed, 0.2F, false, 0.5F, 0, limbSwing, limbSwingAmount);

            leftThigh.walk(0.5F * speed, 0.7F, false, 3.14F, 0.2F, limbSwing, limbSwingAmount);
            leftShin.walk(0.5F * speed, 0.6F, false, 1.5F, 0.3F, limbSwing, limbSwingAmount);
            leftUpperFoot.walk(0.5F * speed, 0.8F, false, -1F, -0.1F, limbSwing, limbSwingAmount);
            leftFoot.walk(0.5F * speed, 1.5F, true, -1F, 1F, limbSwing, limbSwingAmount);

            rightThigh.walk(0.5F * speed, 0.7F, true, 3.14F, 0.2F, limbSwing, limbSwingAmount);
            rightShin.walk(0.5F * speed, 0.6F, true, 1.5F, 0.3F, limbSwing, limbSwingAmount);
            rightUpperFoot.walk(0.5F * speed, 0.8F, true, -1F, -0.1F, limbSwing, limbSwingAmount);
            rightFoot.walk(0.5F * speed, 1.5F, false, -1F, 1F, limbSwing, limbSwingAmount);

            rightShin.setScale(1.5F, 1.5F, 1.5F);

            model.chainSwing(tailParts, 0.5F * speed, -0.1F, 2, limbSwing, limbSwingAmount);
            model.chainWave(tailParts, 1F * speed, -0.1F, 2.5F, limbSwing, limbSwingAmount);
            model.chainWave(bodyParts, 1F * speed, -0.1F, 4, limbSwing, limbSwingAmount);

            model.chainWave(rightArmParts, 1F * speed, -0.3F, 4, limbSwing, limbSwingAmount);
            model.chainWave(leftArmParts, 1F * speed, -0.3F, 4, limbSwing, limbSwingAmount);

            model.chainWave(tailParts, 0.1F, 0.05F, 2, entity.ticksExisted, 1F);
            model.chainWave(bodyParts, 0.1F, -0.03F, 5, entity.ticksExisted, 1F);
            model.chainWave(rightArmParts, 0.1F, -0.1F, 4, entity.ticksExisted, 1F);
            model.chainWave(leftArmParts, 0.1F, -0.1F, 4, entity.ticksExisted, 1F);
        }
    }
}
