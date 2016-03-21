package net.ilexiconn.llibrary.test;

import net.ilexiconn.llibrary.server.config.ConfigEntry;
import net.ilexiconn.llibrary.server.config.ConfigHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "ConfigTest")
public class ConfigTest {
    public static Config CONFIG;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        ConfigTest.CONFIG = ConfigHandler.INSTANCE.registerConfig(this, event.getSuggestedConfigurationFile(), new Config());
    }

    public static class Config {
        @ConfigEntry(minValue = "0", maxValue = "8")
        public int testInt = 4;

        @ConfigEntry(name = "customName")
        public boolean testBoolean = true;

        @ConfigEntry
        public String stringTest = "Hello world!";

        @ConfigEntry(category = "client", side = Side.CLIENT)
        public float floatTest = Float.MAX_VALUE;

        @ConfigEntry(comment = "This is a comment!")
        public double doubleTest = Double.MAX_VALUE;

        @ConfigEntry(validValues = {"0", "1", "2", "3", "4", "5"})
        public int[] intArrayTest = {0, 1, 2};

        @ConfigEntry
        public boolean[] booleanArrayTest = {true, false, true};

        @ConfigEntry
        public String[] stringArrayTest = {"Hello", "world", "!"};

        @ConfigEntry
        public float[] floatArrayTest = {Float.MAX_VALUE, Float.MIN_VALUE, Float.MAX_VALUE};

        @ConfigEntry
        public double[] doubleArrayTest = {Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE};
    }
}
