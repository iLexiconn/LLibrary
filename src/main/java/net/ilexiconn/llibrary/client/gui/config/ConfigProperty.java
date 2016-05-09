package net.ilexiconn.llibrary.client.gui.config;

import net.ilexiconn.llibrary.server.util.IValueAccess;

public class ConfigProperty<T> {
    private IValueAccess<T> value;
    private ConfigPropertyType type;

    public ConfigProperty(IValueAccess<T> value, ConfigPropertyType type) {
        this.value = value;
        this.type = type;
    }

    public T get() {
        return this.value.get();
    }

    public void set(T value) {
        this.value.accept(value);
    }

    public ConfigPropertyType getType() {
        return this.type;
    }

    public enum ConfigPropertyType {
        COLOR_SELECTION, CHECK_BOX, INPUT
    }
}
