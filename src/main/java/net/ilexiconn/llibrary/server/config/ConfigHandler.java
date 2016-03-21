package net.ilexiconn.llibrary.server.config;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.config.entry.EntryAdapters;
import net.ilexiconn.llibrary.server.config.entry.IEntryAdapter;
import net.ilexiconn.llibrary.server.util.Tuple3;
import net.minecraft.crash.CrashReport;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.util.*;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public enum ConfigHandler {
    INSTANCE;

    private List<Tuple3<String, Configuration, Object>> configList = new ArrayList<>();
    private Map<String, Map<String, IEntryAdapter>> valueTypeMap = new HashMap<>();
    private Map<String, Map<String, Object>> defaultValueMap = new HashMap<>();
    private Map<Class<?>, IEntryAdapter<?>> entryAdapterMap = new HashMap<>();

    /**
     * Register an entry adapter.
     *
     * @param entryClass   the class to handle
     * @param entryAdapter the adapter
     * @param <T>      the entry type
     */
    public <T> void registerEntryAdapter(Class<T> entryClass, IEntryAdapter<T> entryAdapter) {
        this.entryAdapterMap.put(entryClass, entryAdapter);
    }

    /**
     * Register your mod's config. the first oject has to be the main mod class - the class with the {@link Mod}
     * annotation. This method returns the new instance of the config.
     *
     * @param mod      the mod instance
     * @param file     the file to use
     * @param config   the config instance
     * @param <T> the config type
     * @return the config instance
     */
    public <T> T registerConfig(Object mod, File file, T config) {
        if (!mod.getClass().isAnnotationPresent(Mod.class)) {
            LLibrary.LOGGER.warn("Please register the config using the main mod class. Skipping registeration of object " + mod + ".");
            return null;
        }

        Mod annotaton = mod.getClass().getAnnotation(Mod.class);
        this.configList.add(new Tuple3<>(annotaton.modid(), new Configuration(file), config));
        Map<String, IEntryAdapter> typeMap = new HashMap<>();
        Map<String, Object> valueMap = new HashMap<>();
        Arrays.stream(config.getClass().getFields()).filter(field -> field.isAnnotationPresent(ConfigEntry.class)).forEach(field -> {
            try {
                ConfigEntry configEntry = field.getAnnotation(ConfigEntry.class);
                if (configEntry.side().isServer() || FMLCommonHandler.instance().getEffectiveSide().isClient()) {
                    String name = configEntry.name().isEmpty() ? field.getName() : configEntry.name();
                    IEntryAdapter type = EntryAdapters.getBuiltinAdapter(field);
                    if (type == null) {
                        type = this.entryAdapterMap.get(field.getType());
                    }
                    if (type != null) {
                        field.setAccessible(true);
                        typeMap.put(name, EntryAdapters.getBuiltinAdapter(field));
                        valueMap.put(name, field.get(config));
                    } else {
                        LLibrary.LOGGER.error("Found unsupported config entry " + field.getName() + " for mod " + annotaton.modid());
                    }
                }
            } catch (IllegalAccessException e) {
                LLibrary.LOGGER.error(CrashReport.makeCrashReport(e, "Failed to get config value " + field.getName() + " for mod " + annotaton.modid()).getCompleteReport());
            }
        });
        this.valueTypeMap.put(annotaton.modid(), typeMap);
        this.defaultValueMap.put(annotaton.modid(), valueMap);
        this.saveConfigForID(annotaton.modid());
        return config;
    }

    /**
     * @param modid the mod id
     * @return true if the mod with that id registered a config
     */
    public boolean hasConfigForID(String modid) {
        final boolean[] result = new boolean[1];
        this.configList.stream().filter(tuple -> tuple.getA().equals(modid)).forEach(tuple -> result[0] = true);
        return result[0];
    }

    /**
     * @param modid the mod id
     * @return the {@link Configuration} instance of the mod, null if none can be found
     */
    public Configuration getConfigForID(String modid) {
        final Configuration[] result = {null};
        this.configList.stream().filter(tuple -> tuple.getA().equals(modid)).forEach(tuple -> result[0] = tuple.getB());
        return result[0];
    }

    /**
     * @param modid the mod id
     * @return the config instance of the mod, null if none can be found
     */
    public Object getObjectForID(String modid) {
        final Object[] result = {null};
        this.configList.stream().filter(tuple -> tuple.getA().equals(modid)).forEach(tuple -> result[0] = tuple.getC());
        return result[0];
    }

    /**
     * Saves the config file for the mod. There is no need to call this, LLibrary handles this internally.
     *
     * @param modid the mod id
     */
    public void saveConfigForID(String modid) {
        Object object = this.getObjectForID(modid);
        Configuration config = this.getConfigForID(modid);
        Map<String, IEntryAdapter> typeMap = this.valueTypeMap.get(modid);
        Map<String, Object> valueMap = this.defaultValueMap.get(modid);
        Arrays.stream(object.getClass().getFields()).filter(field -> field.isAnnotationPresent(ConfigEntry.class)).forEach(field -> {
            try {
                ConfigEntry configEntry = field.getAnnotation(ConfigEntry.class);
                if (configEntry.side().isServer() || FMLCommonHandler.instance().getEffectiveSide().isClient()) {
                    String name = configEntry.name().isEmpty() ? field.getName() : configEntry.name();
                    IEntryAdapter type = typeMap.get(name);
                    if (type != null) {
                        field.set(object, type.getValue(config, name, configEntry, valueMap.get(name)));
                    }
                }
            } catch (IllegalAccessException e) {
                LLibrary.LOGGER.error(CrashReport.makeCrashReport(e, "Failed to set config value " + field.getName() + " for mod " + modid).getCompleteReport());
            }
        });
        config.save();
    }
}
