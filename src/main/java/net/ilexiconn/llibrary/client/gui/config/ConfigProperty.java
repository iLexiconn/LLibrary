package net.ilexiconn.llibrary.client.gui.config;

import net.ilexiconn.llibrary.client.gui.element.Element;
import net.ilexiconn.llibrary.server.util.IValueAccess;
import net.minecraftforge.common.config.Property;

import java.util.function.Function;

public class ConfigProperty<T> {
    private IValueAccess<T> value;
    private Property.Type type;
    private Function<PropertyData, Element<ConfigGUI>> elementProvider;

    public ConfigProperty(IValueAccess<T> value, Property.Type type) {
        this(value, type, null);
    }

    public ConfigProperty(IValueAccess<T> value, Property.Type type, Function<PropertyData, Element<ConfigGUI>> elementProvider) {
        this.value = value;
        this.type = type;
        this.elementProvider = elementProvider;
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

    public Function<PropertyData, Element<ConfigGUI>> getElementProvider() {
        return this.elementProvider;
    }

    public static class PropertyData {
        private final ConfigGUI gui;
        private final ConfigProperty property;
        private final int x;
        private final int y;

        public PropertyData(ConfigGUI gui, ConfigProperty property, int x, int y) {
            this.gui = gui;
            this.property = property;
            this.x = x;
            this.y = y;
        }

        public ConfigProperty getProperty() {
            return this.property;
        }

        public int getX() {
            return  this.x;
        }

        public int getY() {
            return  this.y;
        }

        public ConfigGUI getGUI() {
            return this.gui;
        }
    }
}
