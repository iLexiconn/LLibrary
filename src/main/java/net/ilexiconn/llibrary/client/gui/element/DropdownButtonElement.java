package net.ilexiconn.llibrary.client.gui.element;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class DropdownButtonElement<T extends GuiScreen> extends Element<T> {
    private String text;
    private boolean dropped;
    private Function<DropdownButtonElement<T>, Boolean> function;
    private final List<String> entries;
    private int dropdownWidth;
    private String selected;

    public DropdownButtonElement(T gui, String text, float posX, float posY, int width, int height, List<String> entries, Function<DropdownButtonElement<T>, Boolean> function) {
        super(gui, posX, posY, width, height);
        this.text = text;
        this.function = function;
        this.entries = entries;

        for (String entry : entries) {
            int entryWidth = gui.mc.fontRenderer.getStringWidth(entry) + 5;
            if (entryWidth > dropdownWidth) {
                this.dropdownWidth = entryWidth;
            }
        }
    }

    @Override
    public void render(float mouseX, float mouseY, float partialTicks) {
        this.drawRectangle(this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight(), this.isEnabled() && (this.isSelected(mouseX, mouseY) || this.dropped) ? this.getColorScheme().getPrimaryColor() : this.getColorScheme().getSecondaryColor());
        FontRenderer fontRenderer = this.getGUI().mc.fontRenderer;
        this.drawString(this.text, this.getPosX() + (this.getWidth() / 2) - (fontRenderer.getStringWidth(this.text) / 2), this.getPosY() + (this.getHeight() / 2) - (fontRenderer.FONT_HEIGHT / 2), LLibrary.CONFIG.getTextColor(), false);
        if (this.dropped) {
            this.drawRectangle(this.getPosX(), this.getPosY() + this.getHeight(), this.dropdownWidth, this.entries.size() * 12, this.getColorScheme().getSecondaryColor());
            float y = this.getPosY() + this.getHeight() + 2;
            for (String entry : this.entries) {
                if (this.isEntrySelected(mouseX, mouseY, y)) {
                    this.drawRectangle(this.getPosX(), y - 2, this.dropdownWidth, 12, this.getColorScheme().getPrimaryColor());
                }
                this.drawString(entry, this.getPosX() + 3, y, LLibrary.CONFIG.getTextColor(), false);
                y += 12.0F;
            }
        }
    }

    @Override
    public boolean mouseClicked(float mouseX, float mouseY, int button) {
        this.selected = null;
        if (this.isSelected(mouseX, mouseY)) {
            this.dropped = !this.dropped;
            this.getGUI().mc.getSoundHandler().playSound(PositionedSoundRecord.func_147673_a(new ResourceLocation("gui.button.press")));
            return true;
        } else {
            if (this.dropped) {
                float y = this.getPosY() + this.getHeight() + 2;
                for (String entry : this.entries) {
                    if (this.isEntrySelected(mouseX, mouseY, y)) {
                        this.selected = entry;
                        if (this.function.apply(this)) {
                            this.getGUI().mc.getSoundHandler().playSound(PositionedSoundRecord.func_147673_a(new ResourceLocation("gui.button.press")));
                            break;
                        }
                    }
                    y += 12.0F;
                }
            }
            this.dropped = false;
            return false;
        }
    }

    public String getSelected() {
        return this.selected;
    }

    private boolean isEntrySelected(float mouseX, float mouseY, float y) {
        float x = this.getPosX() + 3;
        return mouseX >= x && mouseX <= x + this.dropdownWidth && mouseY >= y && mouseY <= y + 12;
    }
}
