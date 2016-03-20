package net.ilexiconn.llibrary.client.model.tabula;

import com.google.gson.Gson;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public enum TabulaModelHandler {
    INSTANCE;

    private Gson gson = new Gson();

    public TabulaModelContainer loadModel(String path) throws IOException {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        ZipInputStream in = new ZipInputStream(TabulaModelHandler.class.getResourceAsStream(path));
        ZipEntry entry;
        while ((entry = in.getNextEntry()) != null) {
            if (entry.getName().equals("model.json")) {
                return loadModel(in);
            }
        }
        return null;
    }

    public TabulaModelContainer loadModel(InputStream in) throws IOException {
        return gson.fromJson(new InputStreamReader(in), TabulaModelContainer.class);
    }
}
