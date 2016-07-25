package net.ilexiconn.llibrary.server.core.plugin;

import cpw.mods.fml.relauncher.IFMLCallHook;
import net.ilexiconn.llibrary.server.asm.MappingHandler;
import org.apache.commons.compress.utils.IOUtils;

import java.io.InputStream;
import java.util.Map;

public class LLibraryCallHook implements IFMLCallHook {
    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public Void call() throws Exception {
        InputStream stream = null;
        try {
            stream = this.getClass().getResourceAsStream("/llibrary_mappings");
            MappingHandler.INSTANCE.parseMappings(stream);
        } finally {
            IOUtils.closeQuietly(stream);
        }
        return null;
    }
}
