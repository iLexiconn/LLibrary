package net.ilexiconn.llibrary.server.config;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.util.Tuple3;
import net.minecraft.crash.CrashReport;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.util.*;

public enum ConfigHandler {
    INSTANCE;

    private List<Tuple3<String, Configuration, Object>> configList = new ArrayList<>();
    private Map<String, Map<String, Object>> defaultValueMap = new HashMap<>();

    public <CONFIG> CONFIG registerConfig(Object mod, File file, CONFIG config) {
        if (!mod.getClass().isAnnotationPresent(Mod.class)) {
            LLibrary.LOGGER.warn("Please register the config using the main mod class. Skipping registeration of object " + mod + ".");
            return null;
        }

        Mod annotaton = mod.getClass().getAnnotation(Mod.class);
        this.configList.add(new Tuple3<>(annotaton.modid(), new Configuration(file), config));
        Map<String, Object> valueMap = new HashMap<>();
        Arrays.stream(config.getClass().getFields()).filter(field -> field.isAnnotationPresent(ConfigEntry.class)).forEach(field -> {
            try {
                ConfigEntry configEntry = field.getAnnotation(ConfigEntry.class);
                valueMap.put(configEntry.name(), field.get(config));
            } catch (IllegalAccessException e) {
                LLibrary.LOGGER.error(CrashReport.makeCrashReport(e, "Failed to get config value " + field.getName() + " for mod " + annotaton.modid()).getCompleteReport());
            }
        });
        this.defaultValueMap.put(annotaton.modid(), valueMap);
        this.saveConfigForID(annotaton.modid());
        return config;
    }

    public boolean hasConfigForID(String modid) {
        final boolean[] result = new boolean[1];
        this.configList.stream().filter(tuple -> tuple.getA().equals(modid)).forEach(tuple -> result[0] = true);
        return result[0];
    }

    public Configuration getConfigForID(String modid) {
        final Configuration[] result = {null};
        this.configList.stream().filter(tuple -> tuple.getA().equals(modid)).forEach(tuple -> result[0] = tuple.getB());
        return result[0];
    }

    public Object getObjectForID(String modid) {
        final Object[] result = {null};
        this.configList.stream().filter(tuple -> tuple.getA().equals(modid)).forEach(tuple -> result[0] = tuple.getC());
        return result[0];
    }

    public void saveConfigForID(String modid) {
        Object object = this.getObjectForID(modid);
        Configuration config = this.getConfigForID(modid);
        Map<String, Object> valueMap = this.defaultValueMap.get(modid);
        Arrays.stream(object.getClass().getFields()).filter(field -> field.isAnnotationPresent(ConfigEntry.class)).forEach(field -> {
            try {
                ConfigEntry configEntry = field.getAnnotation(ConfigEntry.class);
                field.set(object, configEntry.type().getValue(config, configEntry, valueMap.get(configEntry.name())));
            } catch (IllegalAccessException e) {
                LLibrary.LOGGER.error(CrashReport.makeCrashReport(e, "Failed to set config value " + field.getName() + " for mod " + modid).getCompleteReport());
            }
        });
        config.save();
    }
}
