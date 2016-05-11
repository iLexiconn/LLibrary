package net.ilexiconn.llibrary.server.config;

import net.ilexiconn.llibrary.client.gui.element.color.ColorMode;
import net.ilexiconn.llibrary.server.nbt.NBTHandler;
import net.ilexiconn.llibrary.server.nbt.NBTMutatorProperty;
import net.ilexiconn.llibrary.server.nbt.NBTProperty;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.awt.*;
import java.io.File;

public class LLibraryConfig implements INBTSerializable<NBTTagCompound> {
    @NBTProperty
    private int accentColor = 0xFF038288;
    @NBTMutatorProperty(type = String.class)
    private ColorMode colorMode = ColorMode.LIGHT;
    @NBTProperty
    private boolean patreonEffects = true;
    @NBTProperty
    private boolean versionCheck = true;
    @NBTProperty
    private boolean tabsAlwaysVisible = false;
    @NBTProperty
    private boolean tabsLeftSide = true;

    public int getPrimaryColor() {
        return colorMode.getPrimaryColor();
    }

    public int getSecondaryColor() {
        return colorMode.getSecondaryColor();
    }

    public int getTertiaryColor() {
        return colorMode.getTertiaryColor();
    }

    public int getPrimarySubcolor() {
        return colorMode.getPrimarySubcolor();
    }

    public int getSecondarySubcolor() {
        return colorMode.getSecondarySubcolor();
    }

    public int getTextColor() {
        return colorMode.getTextColor();
    }

    public int getInvertedTextColor() {
        return colorMode.getInvertedTextColor();
    }

    public int getAccentColor() {
        return accentColor;
    }

    public int getDarkAccentColor() {
        int r = this.accentColor >> 16 & 255;
        int g = this.accentColor >> 8 & 255;
        int b = this.accentColor & 255;
        float[] hsb = Color.RGBtoHSB(r, g, b, null);
        Color newColor = Color.getHSBColor(hsb[0], hsb[1], hsb[2] * 0.85F);
        return (0xFF << 24) | ((newColor.getRed() & 0xFF) << 16) | ((newColor.getGreen() & 0xFF) << 8) | (newColor.getBlue() & 0xFF);
    }

    public String getColorMode() {
        return colorMode.getName();
    }

    public void setAccentColor(int accentColor) {
        this.accentColor = accentColor;
    }

    public void setColorMode(String colorMode) {
        this.colorMode = ColorMode.getColorMode(colorMode);
    }

    public boolean hasPatreonEffects() {
        return patreonEffects;
    }

    public void setPatreonEffects(boolean patreonEffects) {
        this.patreonEffects = patreonEffects;
    }

    public boolean hasVersionCheck() {
        return versionCheck;
    }

    public void setVersionCheck(boolean versionCheck) {
        this.versionCheck = versionCheck;
    }

    public boolean areTabsAlwaysVisible() {
        return tabsAlwaysVisible;
    }

    public void setTabsAlwaysVisible(boolean tabsAlwaysVisible) {
        this.tabsAlwaysVisible = tabsAlwaysVisible;
    }

    public boolean areTabsLeftSide() {
        return tabsLeftSide;
    }

    public void setTabsLeftSide(boolean tabsLeftSide) {
        this.tabsLeftSide = tabsLeftSide;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        NBTHandler.INSTANCE.saveNBTData(this, compound);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        NBTHandler.INSTANCE.loadNBTData(this, compound);
    }

    public void load() {
        try {
            this.deserializeNBT(CompressedStreamTools.read(new File(".", "llibrary" + File.separator + "config.dat")));
        } catch (Exception e) {
            if (!(e instanceof NullPointerException)) {
                e.printStackTrace();
            } else {
                this.save(); //Don't bother to check for the file first, just make it if the input is null... :^)
            }
        }
    }

    public void save() {
        try {
            CompressedStreamTools.write(this.serializeNBT(), new File(".", "llibrary" + File.separator + "config.dat"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
