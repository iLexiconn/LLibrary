package net.ilexiconn.llibrary.client.gui.config;

import net.ilexiconn.llibrary.LLibrary;

import java.lang.reflect.Field;

public class ConfigProperty {
    private Field field;
    private ConfigPropertyType type;

    public ConfigProperty(Field field, ConfigPropertyType type) {
        this.field = field;
        this.field.setAccessible(true);
        this.type = type;
    }

    public Object get() {
        try {
            return this.field.get(LLibrary.CONFIG);
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    public ConfigPropertyType getType() {
        return this.type;
    }

    public void set(Object value) {
        try {
            this.field.set(LLibrary.CONFIG, value);
        } catch (IllegalAccessException e) {
        }
    }

    public enum ConfigPropertyType {
        COLOR_SELECTION, COLOR_MODE, CHECK_BOX;
    }
}
