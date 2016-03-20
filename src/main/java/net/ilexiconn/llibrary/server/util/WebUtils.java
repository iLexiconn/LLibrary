package net.ilexiconn.llibrary.server.util;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.crash.CrashReport;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.List;

/**
 * Utilities to access data from the internet. All methods throw out an error to the console if something goes wrong.
 *
 * @author iLexiconn
 * @since 1.0.0
 */
public class WebUtils {
    public static final String PASTEBIN_URL_PREFIX = "http://pastebin.com/raw.php?i=";

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
     * Get content from a ur as List, with every newline as a new entry. Returns null if something goes wrong.
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
}
