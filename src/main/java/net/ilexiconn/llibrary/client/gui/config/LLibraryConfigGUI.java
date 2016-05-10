package net.ilexiconn.llibrary.client.gui.config;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.util.IValueAccess;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Property;
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
        }, Property.Type.BOOLEAN));
        LLibraryConfigGUI.GENERAL_PROPERTIES.put("Version Checker", new ConfigProperty<>(new IValueAccess<Boolean>() {
            @Override
            public Boolean get() {
                return LLibrary.CONFIG.hasVersionCheck();
            }

            @Override
            public void accept(Boolean versionCheck) {
                LLibrary.CONFIG.setVersionCheck(versionCheck);
            }
        }, Property.Type.BOOLEAN));
        LLibraryConfigGUI.GENERAL_PROPERTIES.put("Survival Tabs Always Visible", new ConfigProperty<>(new IValueAccess<Boolean>() {
            @Override
            public Boolean get() {
                return LLibrary.CONFIG.areTabsAlwaysVisible();
            }

            @Override
            public void accept(Boolean tabsAlwaysVisible) {
                LLibrary.CONFIG.setTabsAlwaysVisible(tabsAlwaysVisible);
            }
        }, Property.Type.BOOLEAN));
        LLibraryConfigGUI.GENERAL_PROPERTIES.put("Survival Tabs Left Side", new ConfigProperty<>(new IValueAccess<Boolean>() {
            @Override
            public void accept(Boolean tabsLeftSide) {
                LLibrary.CONFIG.setTabsLeftSide(tabsLeftSide);
            }

            @Override
            public Boolean get() {
                return LLibrary.CONFIG.areTabsLeftSide();
            }
        }, Property.Type.BOOLEAN));
        LLibraryConfigGUI.APPEARANCE_PROPERTIES.put("Accent Color", new ConfigProperty<>(new IValueAccess<Integer>() {
            @Override
            public Integer get() {
                return LLibrary.CONFIG.getAccentColor();
            }

            @Override
            public void accept(Integer accentColor) {
                LLibrary.CONFIG.setAccentColor(accentColor);
            }
        }, Property.Type.COLOR));
        LLibraryConfigGUI.APPEARANCE_PROPERTIES.put("Dark Mode", new ConfigProperty<>(new IValueAccess<Boolean>() {
            @Override
            public Boolean get() {
                return LLibrary.CONFIG.getColorMode().equals("dark");
            }

            @Override
            public void accept(Boolean colorMode) {
                LLibrary.CONFIG.setColorMode(colorMode ? "dark" : "light");
            }
        }, Property.Type.BOOLEAN));
    }

    public LLibraryConfigGUI(GuiScreen parent) {
        super(parent, LLibrary.INSTANCE, null);
        this.categories.add(new ConfigCategory("General", LLibraryConfigGUI.GENERAL_PROPERTIES));
        this.categories.add(new ConfigCategory("Appearance", LLibraryConfigGUI.APPEARANCE_PROPERTIES));
    }

    @Override
    public void onGuiClosed() {
        LLibrary.CONFIG.save();
        super.onGuiClosed();
    }
}
