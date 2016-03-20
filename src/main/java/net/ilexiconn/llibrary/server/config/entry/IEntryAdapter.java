package net.ilexiconn.llibrary.server.config.entry;

import net.ilexiconn.llibrary.server.config.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

public interface IEntryAdapter<ENTRY> {
    ENTRY getValue(Configuration config, String name, ConfigEntry entry, Object defaultValue);
}
