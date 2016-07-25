package net.ilexiconn.llibrary.server.core.plugin;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.Name("llibrary-core")
@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions("net.ilexiconn.llibrary.server.asm")
public class LLibraryPlugin implements IFMLLoadingPlugin {
    public static boolean inDevelopment;

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"net.ilexiconn.llibrary.server.core.plugin.LLibraryTransformer", "net.ilexiconn.llibrary.server.core.patcher.LLibraryRuntimePatcher"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return "net.ilexiconn.llibrary.server.core.plugin.LLibraryCallHook";
    }

    @Override
    public void injectData(Map<String, Object> data) {
        LLibraryPlugin.inDevelopment = !(Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
