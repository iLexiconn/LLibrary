package net.ilexiconn.llibrary.server.model.tabula;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class TabulaModelLoader {
    private static final Gson GSON = new Gson();

    public static TabulaModelContainer load(String path) throws IOException {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        ZipInputStream in = new ZipInputStream(TabulaModelLoader.class.getResourceAsStream(path));
        ZipEntry entry;
        while ((entry = in.getNextEntry()) != null) {
            if (entry.getName().equals("model.json")) {
                return load(in);
            }
        }
        return null;
    }

    public static TabulaModelContainer load(InputStream in) throws IOException {
        return GSON.fromJson(new InputStreamReader(in), TabulaModelContainer.class);
    }
}
