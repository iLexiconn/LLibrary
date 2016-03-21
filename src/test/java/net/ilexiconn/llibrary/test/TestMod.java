package net.ilexiconn.llibrary.test;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.capability.EntityDataHandler;
import net.ilexiconn.llibrary.server.capability.IEntityData;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.io.IOException;

/**
 * Things to be tested:
 * Animation System: DONE
 * Extended Entity Data: DONE
 * Command Builder: DONE
 * Tabula model loader + animation tools: DONE
 */
@Mod(modid = "testmod", name = "Test Mod", version = "0.0.0")
public class TestMod {
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        EntityRegistry.registerModEntity(TabulaTestEntity.class, "tabula_test_entity", 0, this, 64, 1, false);
        RenderingRegistry.registerEntityRenderingHandler(TabulaTestEntity.class, manager -> {
            try {
                return new RenderLiving<TabulaTestEntity>(manager, new TabulaModel(TabulaModelHandler.INSTANCE.loadModel("assets/testmod/models/entity/model.tbl"), (model, entity, limbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, scale) -> {
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
                    AdvancedModelRenderer jaw = model.getCube("down_jaw");
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
                    AdvancedModelRenderer rightToe = model.getCube("Right toe");
                    AdvancedModelRenderer leftToe = model.getCube("Left toe");

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

                    model.chainSwing(0.5F * speed, -0.1F, 2, limbSwing, limbSwingAmount, tailParts);
                    model.chainWave(1F * speed, -0.1F, 2.5F, limbSwing, limbSwingAmount, tailParts);
                    model.chainWave(1F * speed, -0.1F, 4, limbSwing, limbSwingAmount, bodyParts);

                    model.chainWave(1F * speed, -0.3F, 4, limbSwing, limbSwingAmount, rightArmParts);
                    model.chainWave(1F * speed, -0.3F, 4, limbSwing, limbSwingAmount, leftArmParts);

                    // Idling
                    model.chainWave(0.1F, 0.05F, 2, entity.ticksExisted, 1F, tailParts);
                    model.chainWave(0.1F, -0.03F, 5, entity.ticksExisted, 1F, bodyParts);
                    model.chainWave(0.1F, -0.1F, 4, entity.ticksExisted, 1F, rightArmParts);
                    model.chainWave(0.1F, -0.1F, 4, entity.ticksExisted, 1F, leftArmParts);
                }), 0.0F) {
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

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    @SubscribeEvent
    public void onAttachCapabilities(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            EntityDataHandler.INSTANCE.registerExtendedEntityData(event.entity, new IEntityData() {
                @Override
                public void writeToNBT(NBTTagCompound compound) {
                    compound.setString("Test", "yay");
                    System.out.println("Saved value \"yay\" to \"Test\"");
                }

                @Override
                public void readFromNBT(NBTTagCompound compound) {
                    System.out.println("Loaded value: \"" + compound.getString("Test") + "\"");
                }

                @Override
                public String getIdentifier() {
                    return "testmodentitydata";
                }
            });
        }
    }

    public static class TabulaTestEntity extends EntityLiving {
        public TabulaTestEntity(World world) {
            super(world);
        }
    }
}
