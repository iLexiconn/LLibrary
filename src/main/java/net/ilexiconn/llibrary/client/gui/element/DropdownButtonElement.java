package net.ilexiconn.llibrary.client.gui.element;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.property.IStringSelectionProperty;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DropdownButtonElement<T extends IElementGUI> extends Element<IElementGUI> {
    private IStringSelectionProperty selected;
    private String text;
    private boolean dropped;
    private int dropdownWidth;

    public DropdownButtonElement(T gui, float posX, float posY, int width, int height, IStringSelectionProperty selected) {
        super(gui, posX, posY, width, height);
        this.selected = selected;
        this.text = selected.getString();

        for (String value : this.selected.getValidStringValues()) {
            int entryWidth = gui.getFontRenderer().getStringWidth(value) + 5;
            if (entryWidth > this.dropdownWidth) {
                this.dropdownWidth = entryWidth;
            }
        }
    }

    @Override
    public void render(float mouseX, float mouseY, float partialTicks) {
        this.drawRectangle(this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight(), this.isEnabled() && (this.isSelected(mouseX, mouseY) || this.dropped) ? this.getColorScheme().getPrimaryColor() : this.getColorScheme().getSecondaryColor());
        FontRenderer fontRenderer = this.gui.getFontRenderer();
        fontRenderer.drawString(this.text, this.getPosX() + (this.getWidth() / 2) - (fontRenderer.getStringWidth(this.text) / 2), this.getPosY() + (this.getHeight() / 2) - (fontRenderer.FONT_HEIGHT / 2), LLibrary.CONFIG.getTextColor(), false);
        if (this.dropped) {
            this.drawRectangle(this.getPosX(), this.getPosY() + this.getHeight(), this.dropdownWidth, this.selected.getValidStringValues().size() * 12, this.getColorScheme().getSecondaryColor());
            float y = this.getPosY() + this.getHeight() + 2;
            for (String entry : this.selected.getValidStringValues()) {
                if (this.isEntrySelected(mouseX, mouseY, y)) {
                    this.drawRectangle(this.getPosX(), y - 2, this.dropdownWidth, 12, this.getColorScheme().getPrimaryColor());
                }
                fontRenderer.drawString(entry, this.getPosX() + 3, y, LLibrary.CONFIG.getTextColor(), false);
                y += 12.0F;
            }
        }
    }

    @Override
    public boolean mouseClicked(float mouseX, float mouseY, int button) {
        this.selected = null;
        if (this.isSelected(mouseX, mouseY)) {
            this.dropped = !this.dropped;
            this.gui.playClickSound();
            return true;
        } else {
            if (this.dropped) {
                float y = this.getPosY() + this.getHeight() + 2;
                for (String value : this.selected.getValidStringValues()) {
                    if (this.isEntrySelected(mouseX, mouseY, y)) {
                        if (this.selected.trySetString(value)) {
                            this.gui.playClickSound();
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
        return this.selected.getString();
    }

    private boolean isEntrySelected(float mouseX, float mouseY, float y) {
        float x = this.getPosX() + 3;
        return mouseX >= x && mouseX <= x + this.dropdownWidth && mouseY >= y && mouseY <= y + 12;
    }
}
