package net.ilexiconn.llibrary.test;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.nbt.NBTHandler;
import net.ilexiconn.llibrary.server.nbt.NBTProperty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;

@Mod(modid = "NBTTest")
public class NBTTest {
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        EntityPropertiesHandler.INSTANCE.registerProperties(TestEntityProperties.class);
    }

    public static class TestEntityProperties extends EntityProperties<EntityPlayer> {
        @NBTProperty
        public boolean[] first = {true, false, true};
        @NBTProperty
        public Boolean[] second = {false, true, false};

        @Override
        public void init() {

        }

        @Override
        public void saveNBTData(NBTTagCompound compound) {
            NBTHandler.INSTANCE.saveNBTData(this, compound);
        }

        @Override
        public void loadNBTData(NBTTagCompound compound) {
            NBTHandler.INSTANCE.loadNBTData(this, compound);
            System.out.println("Loading " + Arrays.toString(this.first) + " and " + Arrays.toString(this.second));
        }

        @Override
        public String getID() {
            return "NBTTest";
        }

        @Override
        public Class<EntityPlayer> getEntityClass() {
            return EntityPlayer.class;
        }

        @Override
        public int getTrackingTime() {
            return 0;
        }
    }
}
