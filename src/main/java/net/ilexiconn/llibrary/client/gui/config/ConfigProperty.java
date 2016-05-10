package net.ilexiconn.llibrary.client.gui.config;

import net.ilexiconn.llibrary.server.util.IValueAccess;
import net.minecraftforge.common.config.Property;

public class ConfigProperty<T> {
    private IValueAccess<T> value;
    private Property.Type type;

    public ConfigProperty(IValueAccess<T> value, Property.Type type) {
        this.value = value;
        this.type = type;
    }

    public T get() {
        return this.value.get();
    }

    public void set(T value) {
        this.value.accept(value);
    }

    public Property.Type getType() {
        return this.type;
    }
}
