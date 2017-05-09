package net.ilexiconn.llibrary.server.asm;

import java.util.HashMap;
import java.util.Map;

public class Descriptors {
    private static final Map<String, String> PRIMITIVE_NAMES = new HashMap<>();

    static {
        PRIMITIVE_NAMES.put("void", "V");
        PRIMITIVE_NAMES.put("boolean", "Z");
        PRIMITIVE_NAMES.put("char", "C");
        PRIMITIVE_NAMES.put("byte", "B");
        PRIMITIVE_NAMES.put("short", "S");
        PRIMITIVE_NAMES.put("int", "I");
        PRIMITIVE_NAMES.put("float", "F");
        PRIMITIVE_NAMES.put("long", "J");
        PRIMITIVE_NAMES.put("double", "D");
    }

    public static String method(Object... params) {
        if (params.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder("(");
        for (int i = 0; i < params.length - 1; i++) {
            Object obj = params[i];
            if (obj instanceof Integer) {
                String desc = field(params[++i]);
                for (int j = 0, k = (int) obj; j < k; j++) {
                    builder.append(desc);
                }
            } else {
                builder.append(field(obj));
            }
        }
        builder.append(")").append(field(params[params.length - 1]));
        return builder.toString();
    }

    public static String field(Object obj) {
        String result = "";
        String suffix = "";
        if (obj instanceof String) {
            String cls = MappingHandler.INSTANCE.getClassMapping((String) obj);
            result = PRIMITIVE_NAMES.get(cls);
            if (result == null) {
                result = "L" + cls;
                suffix = ";";
            }
        } else if (obj instanceof Class) {
            Class cls = (Class) obj;
            result = PRIMITIVE_NAMES.get(cls.getName());
            if (result == null) {
                if (!cls.isArray()) {
                    return "L" + cls.getName() + ";";
                } else {
                    return cls.getName();
                }
            }
        }
        while (result.endsWith("[]")) {
            result = "[" + result.substring(0, result.length() - 2);
        }
        return result + suffix;
    }
}
