package net.ilexiconn.llibrary.client.gui.config;

import net.ilexiconn.llibrary.client.gui.element.Element;
import net.ilexiconn.llibrary.server.property.IBooleanProperty;
import net.ilexiconn.llibrary.server.property.IDoubleProperty;
import net.ilexiconn.llibrary.server.property.IDoubleRangeProperty;
import net.ilexiconn.llibrary.server.property.IFloatRangeProperty;
import net.ilexiconn.llibrary.server.util.IValueAccess;
import net.minecraftforge.common.config.Property;

import java.util.function.Function;

public abstract class ConfigProperty {
    public final String name;
    public final String description;

    public ConfigProperty(String name) {
        this(name, "");
    }

    public ConfigProperty(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract Element<ConfigGUI> provideElement(ConfigGUI gui, float x, float y);
}
