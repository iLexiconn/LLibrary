package net.ilexiconn.llibrary.server.asm;

import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.Name(value = "llibrary")
@IFMLLoadingPlugin.MCVersion(value = "1.8.9")
@IFMLLoadingPlugin.TransformerExclusions(value = "net.ilexiconn.llibrary.server.plugin")
public class LLibraryPlugin implements IFMLLoadingPlugin, IFMLCallHook {
    private static boolean isObfuscated;

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { LLibraryClassTransformer.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return getClass().getName();
    }

    @Override
    public void injectData(Map<String, Object> data) {
        LLibraryPlugin.isObfuscated = (boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public Void call() throws Exception {
        return null;
    }

    public static boolean isObfuscated() {
        return isObfuscated;
    }
}
