package net.ilexiconn.llibrary.server.config.entry;

import net.ilexiconn.llibrary.server.config.ConfigEntry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public interface IEntryAdapter<T> {
    /**
     * Parse the entry into the correct value.
     *
     * @param config       the config
     * @param name         the entry name
     * @param entry        the entry annotation
     * @param defaultValue the default value
     * @return the updated object from the config
     */
    T getValue(Configuration config, String name, ConfigEntry entry, Object defaultValue);

    default Property getProperty(Configuration config, String name, ConfigEntry entry, Object defaultValue) {
        return config.get(entry.category(), name, (String) defaultValue);
    }
}
