package net.ilexiconn.llibrary.server.util;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.crash.CrashReport;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.List;

public class WebUtils {
    public static final String PASTEBIN_URL_PREFIX = "http://pastebin.com/raw.php?i=";

    public static String readPastebin(String pasteID) {
        return readURL(WebUtils.PASTEBIN_URL_PREFIX + pasteID);
    }

    public static List<String> readPastebinAsList(String pasteID) {
        return readURLAsList(WebUtils.PASTEBIN_URL_PREFIX + pasteID);
    }

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

    public static BufferedImage downloadImage(String url) {
        try {
            return ImageIO.read(new BufferedInputStream(new URL(url).openStream()));
        } catch (IOException e) {
            LLibrary.LOGGER.error(CrashReport.makeCrashReport(e, "Failed to receive data from URL: " + url).getCompleteReport());
            return null;
        }
    }
}
