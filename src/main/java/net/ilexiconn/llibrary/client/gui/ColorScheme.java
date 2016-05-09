package net.ilexiconn.llibrary.client.gui;

import java.util.function.Supplier;

public class ColorScheme {
    private Supplier<Integer> primaryColor;
    private Supplier<Integer> secondaryColor;

    private ColorScheme(Supplier<Integer> primaryColor, Supplier<Integer> secondaryColor) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    public static ColorScheme create(Supplier<Integer> primaryColor, Supplier<Integer> secondaryColor) {
        return new ColorScheme(primaryColor, secondaryColor);
    }

    public int getPrimaryColor() {
        return primaryColor.get();
    }

    public int getSecondaryColor() {
        return secondaryColor.get();
    }
}
