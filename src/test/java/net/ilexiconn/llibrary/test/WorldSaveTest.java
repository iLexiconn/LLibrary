package net.ilexiconn.llibrary.test;

import net.ilexiconn.llibrary.server.nbt.NBTHandler;
import net.ilexiconn.llibrary.server.nbt.NBTProperty;
import net.ilexiconn.llibrary.server.world.IWorldDataAdapter;
import net.ilexiconn.llibrary.server.world.WorldDataHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "WorldSaveTest")
public class WorldSaveTest {
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        WorldDataHandler.INSTANCE.registerDataAdapter(new TestDataAdapter());
    }

    public static class TestDataAdapter implements IWorldDataAdapter {
        @NBTProperty
        public String test = "yay";

        @Override
        public String getID() {
            return "test";
        }

        @Override
        public void loadNBTData(NBTTagCompound compound, World world) {
            NBTHandler.INSTANCE.loadNBTData(this, compound);
            System.out.println("Loading " + this.test + " from world");
        }

        @Override
        public void saveNBTData(NBTTagCompound compound, World world) {
            NBTHandler.INSTANCE.saveNBTData(this, compound);
            System.out.println("Saving " + this.test + " to world");
        }
    }
}
