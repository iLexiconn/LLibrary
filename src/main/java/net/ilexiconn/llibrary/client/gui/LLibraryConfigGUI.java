package net.ilexiconn.llibrary.client.gui;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.gui.element.ButtonElement;
import net.ilexiconn.llibrary.client.gui.element.LabelElement;
import net.ilexiconn.llibrary.client.gui.element.ListElement;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LLibraryConfigGUI extends ElementGUI {
    public static final ColorScheme RETURN = ColorScheme.create(() -> LLibrary.CONFIG.getPrimaryColor(), () -> LLibrary.CONFIG.getSecondaryColor());
    public static final ColorScheme SIDEBAR = ColorScheme.create(() -> LLibrary.CONFIG.getPrimaryColor(), () -> LLibrary.CONFIG.getTertiaryColor());
    public static final ResourceLocation SETTINGS_ICON = new ResourceLocation("llibrary", "textures/gui/settings.png");

    private GuiScreen parent;

    public LLibraryConfigGUI(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initElements() {
        this.elementList.add(new ButtonElement<>(this, "<", 0, 0, 30, 20, button -> {
            this.mc.displayGuiScreen(this.parent);
            return true;
        }).withColorScheme(LLibraryConfigGUI.RETURN));
        this.elementList.add(new LabelElement<>(this, "Mod List", 35, 6));
        this.elementList.add(new LabelElement<>(this, "LLIBRARY SETTINGS", 35, 26));
        this.elementList.add(new ListElement<>(this, 0, 40, 120, this.height - 40, Lists.newArrayList("General", "Not so general"), 20, list -> {
            System.out.println(list.getSelectedEntry());
            return true;
        }).withColorScheme(LLibraryConfigGUI.SIDEBAR));
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
    }
}
