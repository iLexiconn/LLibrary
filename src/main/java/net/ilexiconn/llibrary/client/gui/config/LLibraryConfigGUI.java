package net.ilexiconn.llibrary.client.gui.config;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.gui.element.CheckboxElement;
import net.ilexiconn.llibrary.client.gui.element.ColorElement;
import net.ilexiconn.llibrary.client.gui.element.Element;
import net.ilexiconn.llibrary.server.property.IBooleanProperty;
import net.ilexiconn.llibrary.server.property.IIntProperty;
import net.ilexiconn.llibrary.server.util.IValueAccess;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class LLibraryConfigGUI extends ConfigGUI {
    private static final Map<String, ConfigProperty> GENERAL_PROPERTIES = new HashMap<>();
    private static final Map<String, ConfigProperty> APPEARANCE_PROPERTIES = new HashMap<>();

    static {
        LLibraryConfigGUI.GENERAL_PROPERTIES.put("Patreon Effects", new ConfigProperty() {
            @Override
            public Element<ConfigGUI> provideElement(ConfigGUI gui, float x, float y) {
                return new CheckboxElement<>(gui, x, y, new IBooleanProperty() {
                    @Override
                    public boolean getBoolean() {
                        return LLibrary.CONFIG.hasPatreonEffects();
                    }

                    @Override
                    public void setBoolean(boolean value) {
                        LLibrary.CONFIG.setPatreonEffects(value);
                    }

                    @Override
                    public boolean isValidBoolean(boolean value) {
                        return true;
                    }
                });
            }
        });
        LLibraryConfigGUI.GENERAL_PROPERTIES.put("Version Checker", new ConfigProperty() {
            @Override
            public Element<ConfigGUI> provideElement(ConfigGUI gui, float x, float y) {
                return new CheckboxElement<>(gui, x, y, new IBooleanProperty() {
                    @Override
                    public boolean getBoolean() {
                        return LLibrary.CONFIG.hasVersionCheck();
                    }

                    @Override
                    public void setBoolean(boolean value) {
                        LLibrary.CONFIG.setVersionCheck(value);
                    }

                    @Override
                    public boolean isValidBoolean(boolean value) {
                        return true;
                    }
                });
            }
        });
        LLibraryConfigGUI.GENERAL_PROPERTIES.put("Survival Tabs Always Visible", new ConfigProperty() {
            @Override
            public Element<ConfigGUI> provideElement(ConfigGUI gui, float x, float y) {
                return new CheckboxElement<>(gui, x, y, new IBooleanProperty() {
                    @Override
                    public boolean getBoolean() {
                        return LLibrary.CONFIG.areTabsAlwaysVisible();
                    }

                    @Override
                    public void setBoolean(boolean value) {
                        LLibrary.CONFIG.setTabsAlwaysVisible(value);
                    }

                    @Override
                    public boolean isValidBoolean(boolean value) {
                        return true;
                    }
                });
            }
        });
        LLibraryConfigGUI.GENERAL_PROPERTIES.put("Survival Tabs Left Side", new ConfigProperty() {
            @Override
            public Element<ConfigGUI> provideElement(ConfigGUI gui, float x, float y) {
                return new CheckboxElement<>(gui, x, y, new IBooleanProperty() {
                    @Override
                    public boolean getBoolean() {
                        return LLibrary.CONFIG.areTabsLeftSide();
                    }

                    @Override
                    public void setBoolean(boolean value) {
                        LLibrary.CONFIG.setTabsLeftSide(value);
                    }

                    @Override
                    public boolean isValidBoolean(boolean value) {
                        return true;
                    }
                });
            }
        });
        LLibraryConfigGUI.APPEARANCE_PROPERTIES.put("Accent Color", new ConfigProperty() {
            @Override
            public Element<ConfigGUI> provideElement(ConfigGUI gui, float x, float y) {
                return new ColorElement<>(gui, x, y, 195, 149, new IIntProperty() {
                    @Override
                    public int getInt() {
                        return LLibrary.CONFIG.getAccentColor();
                    }

                    @Override
                    public void setInt(int value) {
                        LLibrary.CONFIG.setAccentColor(value);
                    }

                    @Override
                    public boolean isValidInt(int value) {
                        return true;
                    }
                });
            }
        });
        LLibraryConfigGUI.APPEARANCE_PROPERTIES.put("Dark Mode", new ConfigProperty() {
            @Override
            public Element<ConfigGUI> provideElement(ConfigGUI gui, float x, float y) {
                return new CheckboxElement<>(gui, x, y, new IBooleanProperty() {
                    @Override
                    public boolean getBoolean() {
                        return LLibrary.CONFIG.getColorMode().equals("dark");
                    }

                    @Override
                    public void setBoolean(boolean value) {
                        LLibrary.CONFIG.setColorMode(value ? "dark" : "light");
                    }

                    @Override
                    public boolean isValidBoolean(boolean value) {
                        return true;
                    }
                });
            }
        });
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
