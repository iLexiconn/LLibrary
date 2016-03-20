package net.ilexiconn.llibrary.server.config;

import net.minecraftforge.common.config.Configuration;

public enum EntryTypes {
    INTEGER,
    BOOLEAN,
    STRING;

    public Object getValue(Configuration config, ConfigEntry entry, Object defaultValue) {
        switch (this) {
            case INTEGER:
                int minValue = entry.minValue().isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(entry.minValue());
                int maxValue = entry.maxValue().isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(entry.maxValue());
                return config.getInt(entry.name(), entry.category(),(int) defaultValue, minValue, maxValue, entry.comment());
            case BOOLEAN:
                return config.getBoolean(entry.name(), entry.category(), (boolean) defaultValue, entry.comment());
            default:
                return config.getString(entry.name(), entry.category(), (String) defaultValue, entry.comment());
        }
    }
}
