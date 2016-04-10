package net.ilexiconn.llibrary.test;

import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "PropertiesTest")
public class PropertiesTest {
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        EntityPropertiesHandler.INSTANCE.registerProperties(TestEntityProperties.class);
    }

    @SubscribeEvent
    public void onEntityJump(LivingEvent.LivingJumpEvent event) {
        if (event.entityLiving instanceof EntityPlayer && !event.entityLiving.worldObj.isRemote) {
            EntityPropertiesHandler.INSTANCE.getProperties(event.entityLiving, TestEntityProperties.class).testInt++;
        }
    }

    public static class TestEntityProperties extends EntityProperties<EntityPlayer> {
        public int testInt = 0;

        @Override
        public void init() {

        }

        @Override
        public String getID() {
            return "PropertiesTest";
        }

        @Override
        public Class<EntityPlayer> getEntityClass() {
            return EntityPlayer.class;
        }

        @Override
        public void saveNBTData(NBTTagCompound compound) {
            compound.setInteger("Test", this.testInt);
        }

        @Override
        public void loadNBTData(NBTTagCompound compound) {
            this.testInt = compound.getInteger("Test");
            System.out.println("Loading " + this.testInt + " on " + FMLCommonHandler.instance().getEffectiveSide());
        }

        @Override
        public int getTrackingTime() {
            return 0;
        }
    }
}
