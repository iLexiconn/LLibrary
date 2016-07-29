package net.ilexiconn.llibrary.server.core.plugin;

import net.ilexiconn.llibrary.server.asm.MappingHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@IFMLLoadingPlugin.Name("llibrary")
@IFMLLoadingPlugin.MCVersion("1.8.9")
@IFMLLoadingPlugin.SortingIndex(1001)
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
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        LLibraryPlugin.inDevelopment = !(Boolean) data.get("runtimeDeobfuscationEnabled");
        InputStream stream = null;
        try {
            stream = this.getClass().getResourceAsStream("/llibrary.mappings");
            MappingHandler.INSTANCE.parseMappings(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
