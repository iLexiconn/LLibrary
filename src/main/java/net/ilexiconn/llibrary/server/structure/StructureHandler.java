package net.ilexiconn.llibrary.server.structure;

import java.util.HashMap;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public enum StructureHandler {
    INSTANCE;

    private HashMap<String, StructureGenerator> map = new HashMap<>();

    public void registerStructure(String name, StructureGenerator generator) {
        map.put(name, generator);
    }

    public StructureBuilder createStructure(String name) {
        StructureBuilder builder = new StructureBuilder();
        registerStructure(name, builder);
        return builder;
    }

    public StructureGenerator getStructure(String name) {
        return map.get(name);
    }
}

