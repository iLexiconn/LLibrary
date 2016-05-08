package net.ilexiconn.llibrary.client.gui.config;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.gui.ColorMode;
import net.ilexiconn.llibrary.client.gui.ColorScheme;
import net.ilexiconn.llibrary.client.gui.ElementGUI;
import net.ilexiconn.llibrary.client.gui.element.*;
import net.ilexiconn.llibrary.server.util.IValueAccess;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class LLibraryConfigGUI extends ElementGUI {
    public static final ColorScheme RETURN = ColorScheme.create(() -> LLibrary.CONFIG.getPrimaryColor(), () -> LLibrary.CONFIG.getSecondaryColor());
    public static final ColorScheme SIDEBAR = ColorScheme.create(() -> LLibrary.CONFIG.getPrimaryColor(), () -> LLibrary.CONFIG.getTertiaryColor());
    public static final ResourceLocation SETTINGS_ICON = new ResourceLocation("llibrary", "textures/gui/settings.png");

    private GuiScreen parent;
    private List<ConfigCategory> categories = new ArrayList<>();

    private static final List<String> CATEGORY_NAMES = new ArrayList<>();

    private static final Map<String, ConfigProperty> GENERAL_PROPERTIES = new HashMap<>();
    private static final Map<String, ConfigProperty> APPEARANCE_PROPERTIES = new HashMap<>();

    private Map<ConfigProperty, Element<LLibraryConfigGUI>> propertyElements = new HashMap<>();

    private ConfigCategory selectedCategory;

    static {
        try {
            GENERAL_PROPERTIES.put("Patreon Effects", new ConfigProperty<>(new IValueAccess<Boolean>() {
                @Override
                public Boolean get() {
                    return LLibrary.CONFIG.hasPatreonEffects();
                }

                @Override
                public void set(Boolean patreonEffects) {
                    LLibrary.CONFIG.setPatreonEffects(patreonEffects);
                }
            }, ConfigProperty.ConfigPropertyType.CHECK_BOX));
            GENERAL_PROPERTIES.put("Version Checker", new ConfigProperty<>(new IValueAccess<Boolean>() {
                @Override
                public Boolean get() {
                    return LLibrary.CONFIG.hasVersionCheck();
                }

                @Override
                public void set(Boolean versionCheck) {
                    LLibrary.CONFIG.setVersionCheck(versionCheck);
                }
            }, ConfigProperty.ConfigPropertyType.CHECK_BOX));
            APPEARANCE_PROPERTIES.put("Accent Color", new ConfigProperty<>(new IValueAccess<Integer>() {
                @Override
                public Integer get() {
                    return LLibrary.CONFIG.getAccentColor();
                }

                @Override
                public void set(Integer accentColor) {
                    LLibrary.CONFIG.setAccentColor(accentColor);
                }
            }, ConfigProperty.ConfigPropertyType.COLOR_SELECTION));
            APPEARANCE_PROPERTIES.put("Dark Mode", new ConfigProperty<>(new IValueAccess<Boolean>() {
                @Override
                public Boolean get() {
                    return LLibrary.CONFIG.getColorMode().equals("dark");
                }

                @Override
                public void set(Boolean colorMode) {
                    LLibrary.CONFIG.setColorMode(colorMode ? "dark" : "light");
                }
            }, ConfigProperty.ConfigPropertyType.CHECK_BOX));
            CATEGORY_NAMES.add("General");
            CATEGORY_NAMES.add("Appearance");
        } catch (Exception e) {
        }
    }

    public LLibraryConfigGUI(GuiScreen parent) {
        this.parent = parent;
        this.categories.add(new ConfigCategory(CATEGORY_NAMES.get(0), GENERAL_PROPERTIES));
        this.categories.add(new ConfigCategory(CATEGORY_NAMES.get(1), APPEARANCE_PROPERTIES));
    }

    @Override
    public void initElements() {
        this.elementList.add(new ButtonElement<>(this, "<", 0, 0, 30, 20, button -> {
            this.mc.displayGuiScreen(this.parent);
            return true;
        }).withColorScheme(LLibraryConfigGUI.RETURN));
        this.elementList.add(new LabelElement<>(this, "Mod List", 35, 6));
        this.elementList.add(new LabelElement<>(this, "LLIBRARY SETTINGS", 35, 26));
        ListElement<LLibraryConfigGUI> categoryList = (ListElement<LLibraryConfigGUI>) new ListElement<>(this, 0, 40, 120, this.height - 40, CATEGORY_NAMES, 20, list -> {
            this.selectedCategory = this.categories.get(CATEGORY_NAMES.indexOf(list.getSelectedEntry()));
            for (Map.Entry<ConfigProperty, Element<LLibraryConfigGUI>> element : this.propertyElements.entrySet()) {
                ElementHandler.INSTANCE.removeElement(this, element.getValue());
            }
            this.propertyElements.clear();
            return true;
        }).withColorScheme(LLibraryConfigGUI.SIDEBAR);
        categoryList.setSelectedIndex(0);
        this.selectedCategory = this.categories.get(0);
        this.elementList.add(categoryList);
        this.propertyElements.clear();
    }

    @Override
    public void drawScreen(float mouseX, float mouseY, float partialTicks) {
        Gui.drawRect(0, 0, this.width, 40, LLibrary.CONFIG.getPrimaryColor());
        Gui.drawRect(120, 40, this.width, this.height, LLibrary.CONFIG.getInvertedTextColor());
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        int color = LLibrary.CONFIG.getAccentColor();
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        GlStateManager.color(r, g, b, a);
        GlStateManager.scale(0.15F, 0.15F, 0.15F);
        this.mc.getTextureManager().bindTexture(LLibraryConfigGUI.SETTINGS_ICON);
        this.drawTexturedModalRect(40, 135, 0, 0, 128, 128);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        int x = 125;
        int y = 45;
        for (Map.Entry<String, ConfigProperty> propertyEntry : this.selectedCategory.getProperties().entrySet()) {
            String name = propertyEntry.getKey();
            ConfigProperty<?> property = propertyEntry.getValue();
            fontRendererObj.drawString(name, x, y, LLibrary.CONFIG.getTextColor());
            Element<LLibraryConfigGUI> propertyElement = this.propertyElements.get(property);
            if (propertyElement == null) {
                propertyElement = this.createPropertyElement(property, x, y + 10);
                this.propertyElements.put(property, propertyElement);
                if (propertyElement != null) {
                    this.elementList.add(propertyElement);
                    ElementHandler.INSTANCE.addElement(this, propertyElement);
                }
            }
            if (propertyElement != null) {
                y += propertyElement.getHeight() + 14;
            }
        }
    }

    private Element<LLibraryConfigGUI> createPropertyElement(ConfigProperty property, int x, int y) {
        switch (property.getType()) {
            case CHECK_BOX:
                return new CheckboxElement<>(this, x, y, (checkbox) -> {
                    property.set(checkbox.isSelected());
                    return true;
                }).withSelection((Boolean) property.get());
            case COLOR_SELECTION:
                return new ColorElement<>(this, x, y, 195, 149, (color) -> {
                    property.set(color.getColor());
                   return true;
                });
        }
        return null;
    }
}
