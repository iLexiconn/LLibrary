package net.ilexiconn.llibrary.server.asm;

import net.ilexiconn.llibrary.server.core.plugin.LLibraryPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public enum MappingHandler {
    INSTANCE;

    private Map<String, String> map;

    public void parseMappings(InputStream stream) throws IOException {
        this.map = new HashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split("=");
            this.map.put(split[0], split[1]);
        }
        reader.close();
    }

    public String getClassMapping(String cls) {
        return cls.replace(".", "/");
    }

    public String getMethodMapping(String cls, String method, String desc) {
        if (LLibraryPlugin.inDevelopment) {
            return method;
        }
        cls = this.getClassMapping(cls);
        for (Map.Entry<String, String> entry : this.map.entrySet()) {
            if (entry.getKey().contains("(")) {
                String[] entryParts = entry.getKey().split("\\(");
                int methodIndex = entryParts[0].lastIndexOf("/");
                String entryClass = entryParts[0].substring(0, methodIndex);
                String entryMethod = entryParts[0].substring(methodIndex + 1);
                String entryDesc = "(" + entryParts[1];
                if (cls.equals(entryClass) && method.equals(entryMethod) && desc.equals(entryDesc)) {
                    return entry.getValue();
                }
            }
        }
        return method;
    }

    public String getFieldMapping(String cls, String field) {
        if (LLibraryPlugin.inDevelopment) {
            return field;
        }
        cls = this.getClassMapping(cls);
        for (Map.Entry<String, String> entry : this.map.entrySet()) {
            if (!entry.getKey().contains("(")) {
                int fieldIndex = entry.getKey().lastIndexOf("/");
                String entryClass = entry.getKey().substring(0, fieldIndex);
                String entryField = entry.getKey().substring(fieldIndex + 1);
                if (cls.equals(entryClass) && field.equals(entryField)) {
                    return entry.getValue();
                }
            }
        }
        return field;
    }
}
