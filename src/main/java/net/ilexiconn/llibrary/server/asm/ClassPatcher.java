package net.ilexiconn.llibrary.server.asm;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.HashMap;
import java.util.Map;

public class ClassPatcher {
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

    private String cls;
    private Map<String, MethodPatcher> patcherMap = new HashMap<>();

    public ClassPatcher(String cls) {
        this.cls = cls;
    }

    void handlePatches(ClassNode classNode) {
        FMLRelaunchLog.info("Patching class " + this.cls);
        for (MethodNode methodNode : classNode.methods) {
            MethodPatcher patcher = this.patcherMap.get(methodNode.name + methodNode.desc);
            if (patcher != null) {
                patcher.handlePatches(methodNode);
            }
        }
    }

    public MethodPatcher patchMethod(String method, Object... params) {
        String desc = MappingHandler.INSTANCE.getClassMapping(this.methodDesc(params));
        method = MappingHandler.INSTANCE.getMethodMapping(this.cls, method, desc) + desc;
        MethodPatcher patcher = new MethodPatcher(this, this.cls, method);
        this.patcherMap.put(method, patcher);
        return patcher;
    }

    String methodDesc(Object... params) {
        if (params.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder("(");
        for (int i = 0; i < params.length - 1; i++) {
            Object obj = params[i];
            if (obj instanceof Integer) {
                String desc = this.fieldDesc(params[++i]);
                for (int j = 0, k = (int) obj; j < k; j++) {
                    builder.append(desc);
                }
            } else {
                builder.append(this.fieldDesc(obj));
            }
        }
        builder.append(")").append(this.fieldDesc(params[params.length - 1]));
        return builder.toString();
    }

    String fieldDesc(Object obj) {
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

    @Override
    public String toString() {
        return "class:" + this.cls + this.patcherMap.values();
    }
}
