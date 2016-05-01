package net.ilexiconn.llibrary.client.gui;

import net.ilexiconn.llibrary.server.util.IGetter;

public class ColorScheme {
    private IGetter<Integer> primaryColor;
    private IGetter<Integer> secondaryColor;

    private ColorScheme(IGetter<Integer> primaryColor, IGetter<Integer> secondaryColor) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    public static ColorScheme create(IGetter<Integer> primaryColor, IGetter<Integer> secondaryColor) {
        return new ColorScheme(primaryColor, secondaryColor);
    }

    public int getPrimaryColor() {
        return primaryColor.get();
    }

    public int getSecondaryColor() {
        return secondaryColor.get();
    }
}
