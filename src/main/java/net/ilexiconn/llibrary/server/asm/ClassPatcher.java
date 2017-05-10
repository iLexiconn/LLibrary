package net.ilexiconn.llibrary.server.asm;

import net.minecraftforge.fml.relauncher.FMLRelaunchLog;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.*;
import java.util.function.Consumer;

public class ClassPatcher {

    private String cls;
    private Map<String, MethodPatcher> patcherMap = new HashMap<>();
    private Map<String, Consumer<Method>> creationMap = new HashMap<>();
    private List<String> removeList = new ArrayList<>();

    public ClassPatcher(String cls) {
        this.cls = cls;
    }

    void handlePatches(ClassNode classNode) {
        FMLRelaunchLog.info("Patching class " + this.cls);
        for (Map.Entry<String, Consumer<Method>> entry : this.creationMap.entrySet()) {
            FMLRelaunchLog.info("   Adding method " + entry.getKey());
            String method = entry.getKey().substring(0, entry.getKey().indexOf("("));
            String desc = entry.getKey().substring(method.length());
            MethodNode methodNode = new MethodNode(Opcodes.ACC_PUBLIC, method, desc, null, null);
            Method m = new Method(this, null);
            entry.getValue().accept(m);
            methodNode.instructions.add(m.insnList);
            classNode.methods.add(methodNode);
        }
        for (MethodNode methodNode : new ArrayList<>(classNode.methods)) {
            String method = methodNode.name + methodNode.desc;
            if (this.removeList.contains(method)) {
                FMLRelaunchLog.info("   Removing method " + method);
                classNode.methods.remove(methodNode);
                continue;
            }
            MethodPatcher patcher = this.patcherMap.get(method);
            if (patcher != null) {
                patcher.handlePatches(methodNode);
            }
        }
    }

    public MethodPatcher patchMethod(String method, Object... params) {
        String desc = MappingHandler.INSTANCE.getClassMapping(Descriptors.method(params));
        method = MappingHandler.INSTANCE.getMethodMapping(this.cls, method, desc) + desc;
        MethodPatcher patcher = new MethodPatcher(this, this.cls, method);
        this.patcherMap.put(method, patcher);
        return patcher;
    }

    public ClassPatcher createMethod(String method, Object[] params, Consumer<Method> consumer) {
        String desc = MappingHandler.INSTANCE.getClassMapping(Descriptors.method(params));
        method = MappingHandler.INSTANCE.getMethodMapping(this.cls, method, desc) + desc;
        this.creationMap.put(method, consumer);
        return this;
    }

    public ClassPatcher removeMethod(String method, Object... params) {
        String desc = MappingHandler.INSTANCE.getClassMapping(Descriptors.method(params));
        method = MappingHandler.INSTANCE.getMethodMapping(this.cls, method, desc) + desc;
        this.removeList.add(method);
        return this;
    }

    @Override
    public String toString() {
        return "class:" + this.cls + this.patcherMap.values();
    }
}
