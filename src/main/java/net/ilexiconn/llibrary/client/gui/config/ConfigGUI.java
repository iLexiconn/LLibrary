package net.ilexiconn.llibrary.client.gui.config;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.gui.ElementGUI;
import net.ilexiconn.llibrary.client.gui.element.*;
import net.ilexiconn.llibrary.client.gui.element.color.ColorScheme;
import net.ilexiconn.llibrary.server.util.IValueAccess;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class ConfigGUI extends ElementGUI {
    public static final ColorScheme RETURN = ColorScheme.create(() -> LLibrary.CONFIG.getPrimaryColor(), () -> LLibrary.CONFIG.getSecondaryColor());
    public static final ColorScheme SIDEBAR = ColorScheme.create(() -> LLibrary.CONFIG.getPrimaryColor(), () -> LLibrary.CONFIG.getTertiaryColor());
    public static final ResourceLocation SETTINGS_ICON = new ResourceLocation("llibrary", "textures/gui/settings.png");

    protected GuiScreen parent;
    protected List<ConfigCategory> categories = new ArrayList<>();
    protected Map<ConfigProperty<?>, Element<ConfigGUI>> propertyElements = new HashMap<>();
    protected ConfigCategory selectedCategory;

    private Mod mod;

    public ConfigGUI(GuiScreen parent, Object mod, Configuration config) {
        this.parent = parent;
        if (!mod.getClass().isAnnotationPresent(Mod.class)) {
            throw new RuntimeException("@Mod annotation not found in class " + mod + "!");
        }
        this.mod = mod.getClass().getAnnotation(Mod.class);
        if (config != null) {
            this.categories.addAll(config.getCategoryNames().stream()
                    .map(config::getCategory)
                    .filter(category -> category.size() > 0)
                    .map(category -> {
                        Map<String, ConfigProperty<?>> propertyMap = new LinkedHashMap<>();
                        for (Map.Entry<String, Property> entry : category.entrySet()) {
                            ConfigProperty<?> configProperty = new ConfigProperty<>(new IValueAccess() {
                                @Override
                                public void accept(Object string) {
                                    entry.getValue().set(string.toString());
                                }

                                @Override
                                public Object get() {
                                    return entry.getValue().getString();
                                }
                            }, entry.getValue().getType());
                            propertyMap.put(entry.getKey(), configProperty);
                        }
                        return new ConfigCategory(category.getQualifiedName(), propertyMap);
                    })
                    .collect(Collectors.toList()));
        }
    }

    @Override
    public void initElements() {
        this.addElement(new ButtonElement<>(this, "<", 0, 0, 30, 20, button -> {
            this.mc.displayGuiScreen(this.parent);
            return true;
        }).withColorScheme(ConfigGUI.RETURN));
        this.addElement(new LabelElement<>(this, "Mod List", 35, 6));
        this.addElement(new LabelElement<>(this, this.mod.name().toUpperCase() + " SETTINGS", 35, 26));
        ListElement<ConfigGUI> categoryList = (ListElement<ConfigGUI>) new ListElement<>(this, 0, 40, 120, this.height - 40, this.categories.stream().map(ConfigCategory::getName).collect(Collectors.toList()), 20, list -> {
            this.selectedCategory = this.categories.get(list.getSelectedIndex());
            this.propertyElements.values().forEach(this::removeElement);
            this.propertyElements.clear();
            return true;
        }).withPersistence(true).withColorScheme(ConfigGUI.SIDEBAR);
        categoryList.setSelectedIndex(0);
        this.selectedCategory = this.categories.get(0);
        this.addElement(categoryList);
        this.propertyElements.clear();
    }

    @Override
    public void drawScreen(float mouseX, float mouseY, float partialTicks) {
        Gui.drawRect(0, 0, this.width, 40, LLibrary.CONFIG.getPrimaryColor());
        Gui.drawRect(120, 40, this.width, this.height, LLibrary.CONFIG.getColorMode().equals("dark") ? 0xFF191919 : 0xFFFFFFFF);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        int color = LLibrary.CONFIG.getAccentColor();
        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        GlStateManager.color(r, g, b, a);
        GlStateManager.scale(0.15F, 0.15F, 0.15F);
        this.mc.getTextureManager().bindTexture(ConfigGUI.SETTINGS_ICON);
        this.drawTexturedModalRect(40, 135, 0, 0, 128, 128);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        int x = 125;
        int y = 45;
        for (Map.Entry<String, ConfigProperty<?>> propertyEntry : this.selectedCategory.getProperties().entrySet()) {
            String name = propertyEntry.getKey();
            ConfigProperty<?> property = propertyEntry.getValue();
            fontRendererObj.drawString(name, x, y, LLibrary.CONFIG.getTextColor());
            Element<ConfigGUI> propertyElement = this.propertyElements.get(property);
            if (propertyElement == null) {
                propertyElement = this.createPropertyElement(property, x, y + 10);
                this.propertyElements.put(property, propertyElement);
                if (propertyElement != null) {
                    this.addElement(propertyElement);
                }
            }
            if (propertyElement != null) {
                y += propertyElement.getHeight() + 14;
            }
        }
    }

    protected Element createPropertyElement(ConfigProperty property, int x, int y) {
        Function<ConfigProperty.PropertyData, Element<ConfigGUI>> elementProvider = property.getElementProvider();
        if (elementProvider == null) {
            switch (property.getType()) {
                case BOOLEAN:
                    return new CheckboxElement<>(this, x, y, (checkbox) -> {
                        property.set(checkbox.isSelected());
                        return true;
                    }).withSelection(property.get() instanceof Boolean ? (Boolean) property.get() : (Boolean.parseBoolean((String) property.get())));
                case COLOR:
                    return new ColorElement<>(this, x, y, 195, 149, (color) -> {
                        property.set(color.getColor());
                        return true;
                    });
                case STRING:
                    return new InputElement<>(this, (String) property.get(), x, y, 192, (element) -> {
                        property.set(element.getText());
                    });
                case DOUBLE:
                    return new SliderElement<>(this, x, y, false, (slider) -> {
                        property.set(slider.doubleValue());
                        return true;
                    }).withValue(Float.valueOf(String.valueOf(property.get())));
                case INTEGER:
                    return new SliderElement<>(this, x, y, true, (slider) -> {
                        property.set(slider.intValue());
                        return true;
                    }).withValue((Integer.valueOf(String.valueOf(property.get()))));
            }
        } else {
            return elementProvider.apply(new ConfigProperty.PropertyData(this, property, x, y));
        }
        return null;
    }

    @Override
    public void onGuiClosed() {
        MinecraftForge.EVENT_BUS.post(new ConfigChangedEvent.OnConfigChangedEvent(this.mod.modid(), null, this.mc.theWorld != null, false));
        super.onGuiClosed();
    }

    public GuiScreen getParent() {
        return this.parent;
    }
}
