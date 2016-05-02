package net.ilexiconn.llibrary.client.gui.config;

import java.util.Map;

public class ConfigCategory {
    private String name;
    private Map<String, ConfigProperty> properties;

    public ConfigCategory(String name, Map<String, ConfigProperty> properties) {
        this.name = name;
        this.properties = properties;
    }

    public String getName() {
        return this.name;
    }

    public Map<String, ConfigProperty> getProperties() {
        return this.properties;
    }
}
