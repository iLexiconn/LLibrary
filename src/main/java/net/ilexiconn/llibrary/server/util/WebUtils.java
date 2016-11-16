package net.ilexiconn.llibrary.server.util;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.Tuple;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

/**
 * Utilities to access data from the internet. All methods throw out an error to the console if something goes wrong.
 *
 * @author iLexiconn
 * @since 1.0.0
 */
public class WebUtils {
    public static final String PASTEBIN_URL_PREFIX = "http://pastebin.com/raw.php?i=";

    private static final Queue<Tuple<String, Consumer<String>>> DOWNLOAD_QUEUE = new LinkedBlockingDeque<>();

    static {
        Thread downloadThread = new Thread(() -> {
            while (true) {
                try {
                    if (!DOWNLOAD_QUEUE.isEmpty()) {
                        Tuple<String, Consumer<String>> request = DOWNLOAD_QUEUE.poll();
                        String url = request.getFirst();
                        Consumer<String> callback = request.getSecond();
                        String downloaded = WebUtils.readURL(url);
                        callback.accept(downloaded);
                    }
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        downloadThread.setName("LLibrary Download Thread");
        downloadThread.setDaemon(true);
        downloadThread.start();
    }

    /**
     * Get content from a pastebin file as String, split to multiple lines using newlines. Returns null if something goes wrong.
     *
     * @param pasteID the paste id to read data from
     * @return the content from a pastebin file or null
     */
    public static String readPastebin(String pasteID) {
        return readURL(WebUtils.PASTEBIN_URL_PREFIX + pasteID);
    }

    /**
     * Get content from a pastebin file as List, with every newline as a new entry. Returns null if something goes wrong.
     *
     * @param pasteID the paste id to read data from
     * @return the content from a pastebin file or null
     */
    public static List<String> readPastebinAsList(String pasteID) {
        return readURLAsList(WebUtils.PASTEBIN_URL_PREFIX + pasteID);
    }

    /**
     * Get content from a url as String, split to multiple lines using newlines. Returns null if something goes wrong.
     *
     * @param url the url to read data from
     * @return the content from the url or null
     */
    public static String readURL(String url) {
        try {
            String text = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                text += currentLine + "\r\n";
            }
            reader.close();

            return text;
        } catch (IOException e) {
            LLibrary.LOGGER.error(CrashReport.makeCrashReport(e, "Failed to receive data from URL: " + url).getCompleteReport());
            return null;
        }
    }

    /**
     * Get content from a url as List, with every newline as a new entry. Returns null if something goes wrong.
     *
     * @param url the url to read data from
     * @return the content from a url or null
     */
    public static List<String> readURLAsList(String url) {
        try {
            List<String> text = Lists.newArrayList();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                text.add(currentLine);
            }
            reader.close();

            return text;
        } catch (IOException e) {
            LLibrary.LOGGER.error(CrashReport.makeCrashReport(e, "Failed to receive data from URL: " + url).getCompleteReport());
            return null;
        }
    }

    /**
     * Download an image from an url. Returns null if something goes wrong.
     *
     * @param url the image url
     * @return the image object or null
     */
    public static BufferedImage downloadImage(String url) {
        try {
            return ImageIO.read(new BufferedInputStream(new URL(url).openStream()));
        } catch (IOException e) {
            LLibrary.LOGGER.error(CrashReport.makeCrashReport(e, "Failed to receive data from URL: " + url).getCompleteReport());
            return null;
        }
    }

    /**
     * Get content from a url as String, split to multiple lines using newlines, asynchronously. Returns null if something goes wrong.
     *
     * @param url the url to read data from
     * @param callback callback for when the data is retrieved
     */
    public static void readURLAsync(String url, Consumer<String> callback) {
        DOWNLOAD_QUEUE.add(new Tuple<>(url, callback));
    }

    /**
     * Get content from a url as List, with every newline as a new entry, asynchronously. Returns null if something goes wrong.
     *
     * @param url the url to read data from
     * @param callback callback for when the data is retrieved
     */
    public static void readURLAsListAsync(String url, Consumer<List<String>> callback) {
        DOWNLOAD_QUEUE.add(new Tuple<>(url, (text) -> {
            String[] lines = text.split("\r\n");
            callback.accept(Lists.newArrayList(lines));
        }));
    }

    /**
     * Get content from a pastebin file as String, split to multiple lines using newlines asynchronously. Returns null if something goes wrong.
     *
     * @param pasteID the paste id to read data from
     * @param callback callback for when the data is received
     */
    public static void readPastebinAsync(String pasteID, Consumer<String> callback) {
        readURLAsync(WebUtils.PASTEBIN_URL_PREFIX + pasteID, callback);
    }

    /**
     * Get content from a pastebin file as String, with every newline as a new entry, asynchronously. Returns null if something goes wrong.
     *
     * @param pasteID the paste id to read data from
     * @param callback callback for when the data is received
     */
    public static void readPastebinAsListAsync(String pasteID, Consumer<String> callback) {
        readURLAsync(WebUtils.PASTEBIN_URL_PREFIX + pasteID, callback);
    }
}
