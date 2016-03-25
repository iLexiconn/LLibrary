package net.ilexiconn.llibrary.client.model.tabula;

import com.google.gson.Gson;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author pau101
 * @since 1.0.0
 */
@SideOnly(Side.CLIENT)
public enum TabulaModelHandler {
    INSTANCE;

    private Gson gson = new Gson();

    /**
     * Load a {@link TabulaModelContainer} from the path. A slash will be added if it isn't in the path already.
     *
     * @param path the model path
     * @return the new {@link TabulaModelContainer} instance
     * @throws IOException if the file can't be found
     */
    public TabulaModelContainer loadTabulaModel(String path) throws IOException {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.endsWith(".tbl")) {
            path += ".tbl";
        }
        InputStream stream = TabulaModelHandler.class.getResourceAsStream(path);
        return TabulaModelHandler.INSTANCE.loadTabulaModel(this.getModelJsonStream(path, stream));
    }

    /**
     * Load a {@link TabulaModelContainer} from the model.json input stream.
     *
     * @param stream the model.json input stream
     * @return the new {@link TabulaModelContainer} instance
     */
    public TabulaModelContainer loadTabulaModel(InputStream stream) {
        return gson.fromJson(new InputStreamReader(stream), TabulaModelContainer.class);
    }

    private InputStream getModelJsonStream(String name, InputStream file) throws IOException {
        ZipInputStream zip = new ZipInputStream(file);
        ZipEntry entry;
        while ((entry = zip.getNextEntry()) != null) {
            if (entry.getName().equals("model.json")) {
                return zip;
            }
        }
        throw new RuntimeException("No model.json present in " + name);
    }
}
