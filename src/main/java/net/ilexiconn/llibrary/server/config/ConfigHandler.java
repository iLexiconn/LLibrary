package net.ilexiconn.llibrary.server.config;

import com.google.common.collect.SetMultimap;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.config.entry.EntryAdapters;
import net.ilexiconn.llibrary.server.config.entry.IEntryAdapter;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public enum ConfigHandler {
    INSTANCE;

    private Map<Class<?>, IEntryAdapter<?>> entryAdapters = new HashMap<>();
    private Map<String, Object> configObjects = new HashMap<>();
    private Map<Object, Configuration> configurations = new HashMap<>();
    private Map<Configuration, Map<String, Object>> defaultValues = new HashMap<>();
    private Map<Configuration, Map<String, IEntryAdapter<?>>> adapters = new HashMap<>();

    /**
     * Register an entry adapter.
     *
     * @param type         the class to handle
     * @param entryAdapter the adapter
     * @param <T>          the entry type
     */
    public <T> void registerEntryAdapter(Class<T> type, IEntryAdapter<T> entryAdapter) {
        this.entryAdapters.put(type, entryAdapter);
    }

    /**
     * @param modid the mod id
     * @return true if the mod with that id registered a config
     */
    public boolean hasConfigForID(String modid) {
        return this.configObjects.containsKey(modid);
    }

    /**
     * @param modid the mod id
     * @return the {@link Configuration} instance of the mod, null if none can be found
     */
    public Configuration getConfigForID(String modid) {
        if (this.hasConfigForID(modid)) {
            return this.configurations.get(this.getObjectForID(modid, Object.class));
        }
        return null;
    }

    /**
     * @param modid the mod id
     * @return the config instance of the mod, null if none can be found
     */
    @Deprecated
    public Object getObjectForID(String modid) {
        return this.getObjectForID(modid, Object.class);
    }

    /**
     * @param modid the mod id
     * @param type the config type
     * @return the config instance of the mod, null if none can be found
     */
    public <T> T getObjectForID(String modid, Class<T> type) {
        if (this.hasConfigForID(modid)) {
            return type.cast(this.configObjects.get(modid));
        }
        return null;
    }

    /**
     * Saves the config file for the mod.
     *
     * @param modid the mod id
     */
    public void saveConfigForID(String modid) {
        if (this.hasConfigForID(modid)) {
            Object object = this.configObjects.get(modid);
            Configuration config = this.configurations.get(object);
            Map<String, Object> defaultValues = this.defaultValues.get(config);
            Map<String, IEntryAdapter<?>> adapters = this.adapters.get(config);
            Arrays.stream(object.getClass().getFields()).filter(field -> field.isAnnotationPresent(ConfigEntry.class)).forEach(field -> {
                try {
                    ConfigEntry entry = field.getAnnotation(ConfigEntry.class);
                    if (entry.side().isServer() || FMLCommonHandler.instance().getSide().isClient()) {
                        String name = entry.name().isEmpty() ? field.getName() : entry.name();
                        IEntryAdapter<?> entryAdapter = adapters.get(name);
                        if (entryAdapter != null) {
                            entryAdapter.getProperty(config, name, entry, defaultValues.get(name)).set(String.valueOf(field.get(object)));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            config.save();
            this.loadConfigForID(modid);
        }
    }

    /**
     * Loads the config file for the mod.
     *
     * @param modid the mod id
     */
    public void loadConfigForID(String modid) {
        if (this.hasConfigForID(modid)) {
            Object object = this.configObjects.get(modid);
            Configuration config = this.configurations.get(object);
            Map<String, Object> defaultValues = this.defaultValues.get(config);
            Map<String, IEntryAdapter<?>> adapters = this.adapters.get(config);
            Arrays.stream(object.getClass().getFields()).filter(field -> field.isAnnotationPresent(ConfigEntry.class)).forEach(field -> {
                try {
                    ConfigEntry entry = field.getAnnotation(ConfigEntry.class);
                    if (entry.side().isServer() || FMLCommonHandler.instance().getSide().isClient()) {
                        String name = entry.name().isEmpty() ? field.getName() : entry.name();
                        IEntryAdapter<?> entryAdapter = adapters.get(name);
                        if (entryAdapter != null) {
                            field.set(object, entryAdapter.getValue(config, name, entry, defaultValues.get(name)));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void injectConfig(ModContainer mod, ASMDataTable data) {
        SetMultimap<String, ASMDataTable.ASMData> annotations = data.getAnnotationsFor(mod);
        if (annotations != null) {
            Set<ASMDataTable.ASMData> targetList = annotations.get(Config.class.getName());
            ClassLoader classLoader = Loader.instance().getModClassLoader();
            for (ASMDataTable.ASMData target : targetList) {
                try {
                    Class<?> targetClass = Class.forName(target.getClassName(), true, classLoader);
                    Field field = targetClass.getDeclaredField(target.getObjectName());
                    field.setAccessible(true);
                    Class<?> configClass = field.getType();
                    File configFile = new File(".", "config" + File.separator + mod.getModId() + ".cfg");
                    field.set(null, ConfigHandler.INSTANCE.registerConfig(mod, configFile, configClass.newInstance()));
                } catch (Exception e) {
                    LLibrary.LOGGER.fatal("Failed to inject config for mod container " + mod, e);
                }
            }
        }
    }

    private <T> T registerConfig(ModContainer mod, File file, T config) {
        this.configObjects.put(mod.getModId(), config);
        Configuration configuration = new Configuration(file);
        this.configurations.put(config, configuration);
        Map<String, Object> defaultValues = new HashMap<>();
        Map<String, IEntryAdapter<?>> adapters = new HashMap<>();
        Arrays.stream(config.getClass().getFields()).filter(field -> field.isAnnotationPresent(ConfigEntry.class)).forEach(field -> {
            try {
                ConfigEntry entry = field.getAnnotation(ConfigEntry.class);
                if (entry.side().isServer() || FMLCommonHandler.instance().getSide().isClient()) {
                    String name = entry.name().isEmpty() ? field.getName() : entry.name();
                    IEntryAdapter<?> entryAdapter = EntryAdapters.getBuiltinAdapter(field.getType());
                    if (entryAdapter == null) {
                        entryAdapter = this.entryAdapters.get(field.getType());
                    }
                    if (entryAdapter != null) {
                        field.setAccessible(true);
                        defaultValues.put(name, field.get(config));
                        adapters.put(name, entryAdapter);
                    } else {
                        LLibrary.LOGGER.error("Found unsupported config entry " + field.getName() + " for mod " + mod.getName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        this.defaultValues.put(configuration, defaultValues);
        this.adapters.put(configuration, adapters);
        this.loadConfigForID(mod.getModId());
        return config;
    }
}
