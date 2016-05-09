package net.ilexiconn.llibrary.client.gui.config;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.util.IValueAccess;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class LLibraryConfigGUI extends ConfigGUI {
    private static final Map<String, ConfigProperty<?>> GENERAL_PROPERTIES = new HashMap<>();
    private static final Map<String, ConfigProperty<?>> APPEARANCE_PROPERTIES = new HashMap<>();

    static {
        LLibraryConfigGUI.GENERAL_PROPERTIES.put("Patreon Effects", new ConfigProperty<>(new IValueAccess<Boolean>() {
            @Override
            public Boolean get() {
                return LLibrary.CONFIG.hasPatreonEffects();
            }

            @Override
            public void accept(Boolean patreonEffects) {
                LLibrary.CONFIG.setPatreonEffects(patreonEffects);
            }
        }, ConfigProperty.ConfigPropertyType.CHECK_BOX));
        LLibraryConfigGUI.GENERAL_PROPERTIES.put("Version Checker", new ConfigProperty<>(new IValueAccess<Boolean>() {
            @Override
            public Boolean get() {
                return LLibrary.CONFIG.hasVersionCheck();
            }

            @Override
            public void accept(Boolean versionCheck) {
                LLibrary.CONFIG.setVersionCheck(versionCheck);
            }
        }, ConfigProperty.ConfigPropertyType.CHECK_BOX));
        LLibraryConfigGUI.APPEARANCE_PROPERTIES.put("Accent Color", new ConfigProperty<>(new IValueAccess<Integer>() {
            @Override
            public Integer get() {
                return LLibrary.CONFIG.getAccentColor();
            }

            @Override
            public void accept(Integer accentColor) {
                LLibrary.CONFIG.setAccentColor(accentColor);
            }
        }, ConfigProperty.ConfigPropertyType.COLOR_SELECTION));
        LLibraryConfigGUI.APPEARANCE_PROPERTIES.put("Dark Mode", new ConfigProperty<>(new IValueAccess<Boolean>() {
            @Override
            public Boolean get() {
                return LLibrary.CONFIG.getColorMode().equals("dark");
            }

            @Override
            public void accept(Boolean colorMode) {
                LLibrary.CONFIG.setColorMode(colorMode ? "dark" : "light");
            }
        }, ConfigProperty.ConfigPropertyType.CHECK_BOX));
    }

    public LLibraryConfigGUI(GuiScreen parent) {
        super(parent, LLibrary.INSTANCE, null);
        this.categories.add(new ConfigCategory("General", LLibraryConfigGUI.GENERAL_PROPERTIES));
        this.categories.add(new ConfigCategory("Appearance", LLibraryConfigGUI.APPEARANCE_PROPERTIES));
    }
}
