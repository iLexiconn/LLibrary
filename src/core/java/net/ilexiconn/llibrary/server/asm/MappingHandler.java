package net.ilexiconn.llibrary.server.asm;

import net.ilexiconn.llibrary.server.core.plugin.LLibraryPlugin;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum MappingHandler {
    INSTANCE;

    private final Map<String, String> fields = new HashMap<>();
    private final Map<String, String> methods = new HashMap<>();

    public void parseMappings(InputStream stream) throws IOException {
        if (stream == null) {
            throw new IOException("Could not find LLibrary mappings file!");
        }

        this.fields.clear();
        this.methods.clear();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            List<String> lines = IOUtils.readLines(reader);
            for (String line : lines) {
                String[] split = line.split("=");
                String key = split[0], value = split[1];
                if (key.contains("(")) {
                    this.methods.put(key, value);
                } else {
                    this.fields.put(key, value);
                }
            }
        }
    }

    public String getClassMapping(String cls) {
        return cls.replace(".", "/");
    }

    public String getClassMapping(Object obj) {
        if (obj instanceof String) {
            return this.getClassMapping((String) obj);
        } else if (obj instanceof Class) {
            return ((Class) obj).getName();
        }
        return "";
    }

    public String getMethodMapping(Object obj, String method, String desc) {
        if (LLibraryPlugin.inDevelopment) {
            return method;
        }
        String cls = this.getClassMapping(obj);
        String key = cls + "/" + method + desc;
        return this.methods.getOrDefault(key, method);
    }

    public String getFieldMapping(Object obj, String field) {
        if (LLibraryPlugin.inDevelopment) {
            return field;
        }
        String cls = this.getClassMapping(obj);
        String key = cls + "/" + field;
        return this.fields.getOrDefault(key, field);
    }
}
