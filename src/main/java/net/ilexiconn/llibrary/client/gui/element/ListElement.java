package net.ilexiconn.llibrary.client.gui.element;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.ClientProxy;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class ListElement<T extends GuiScreen> extends Element<T> {
    private List<String> entries;
    private Function<ListElement<T>, Boolean> function;

    private int scroll;
    private int maxDisplayEntries;
    private int maxScroll;
    private int scrollYOffset;
    private boolean scrolling;
    private float scrollPerEntry;
    private int entryHeight;

    private int selectedEntry;

    public ListElement(T gui, float posX, float posY, int width, int height, List<String> entries, Function<ListElement<T>, Boolean> function) {
        this(gui, posX, posY, width, height, entries, 13, function);
    }

    public ListElement(T gui, float posX, float posY, int width, int height, List<String> entries, int entryHeight, Function<ListElement<T>, Boolean> function) {
        super(gui, posX, posY, width, height);
        this.entries = entries;
        this.function = function;
        this.entryHeight = entryHeight;
        this.maxDisplayEntries = this.getHeight() / (entryHeight + 1);
        this.maxScroll = Math.max(0, (this.entries.size()) - this.maxDisplayEntries);
        this.scrollPerEntry = (float) (this.entries.size()) / (this.getHeight() - 1);
    }

    @Override
    public void render(float mouseX, float mouseY, float partialTicks) {
        this.drawRectangle(this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight(), this.getColorScheme().getSecondaryColor());
        FontRenderer fontRenderer = this.getGUI().mc.fontRendererObj;
        int y = (int) (-this.scroll * this.scrollPerEntry * this.entryHeight);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        ScaledResolution scaledResolution = new ScaledResolution(ClientProxy.MINECRAFT);
        int scaleFactor = scaledResolution.getScaleFactor();
        GL11.glScissor((int) (this.getPosX() * scaleFactor), (int) ((this.getGUI().height - (this.getPosY() + this.getHeight()) + 2) * scaleFactor), this.getWidth() * scaleFactor, (this.getHeight() - 4) * scaleFactor);
        int entryIndex = 0;
        for (String entry : this.entries) {
            float entryX = this.getPosX() + 2;
            float entryY = this.getPosY() + y + 2;
            float entryWidth = this.getWidth() - 4;
            boolean selected = this.isSelected(this.getPosX() + 2, this.getPosY() + y + 1, entryWidth, this.entryHeight, mouseX, mouseY) && !this.scrolling;
            boolean clickSelecting = selected && Mouse.isButtonDown(0);
            if (this.selectedEntry == entryIndex) {
                this.drawRectangle(entryX, entryY, entryWidth, this.entryHeight, clickSelecting ? LLibrary.CONFIG.getAccentColor() : LLibrary.CONFIG.getDarkAccentColor());
            } else {
                this.drawRectangle(entryX, entryY, entryWidth, this.entryHeight, selected ? LLibrary.CONFIG.getAccentColor() : this.getColorScheme().getSecondaryColor());
            }
            fontRenderer.drawString(entry, entryX + 2, (entryY - fontRenderer.FONT_HEIGHT / 2) + (this.entryHeight / 2), LLibrary.CONFIG.getTextColor(), false);
            y += this.entryHeight + 1;
            entryIndex++;
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        if (this.maxScroll > 0) {
            float scrollX = this.getPosX() + this.getWidth() - 8;
            float scrollY = this.getPosY() + this.scroll + 2;
            int height = (int) ((this.getHeight() - 2) / ((float) this.entries.size() / (float) this.maxDisplayEntries)) - 2;
            this.drawRectangle(scrollX, scrollY, 6, height, this.scrolling ? LLibrary.CONFIG.getAccentColor() : this.getColorScheme().getPrimaryColor());
        }
    }

    @Override
    public boolean mouseClicked(float mouseX, float mouseY, int button) {
        if (this.maxScroll > 0) {
            float scrollX = this.getPosX() + this.getWidth() - 8;
            float scrollY = this.getPosY() + (this.scroll);
            int height = (int) ((this.getHeight() - 2) / ((float) this.entries.size() / (float) this.maxDisplayEntries));
            if (mouseX >= scrollX && mouseX < scrollX + 6 && mouseY >= scrollY + 1 && mouseY < scrollY + height) {
                this.scrolling = true;
                this.scrollYOffset = (int) (mouseY - scrollY);
                return true;
            }
        }
        if (this.isSelected(mouseX, mouseY)) {
            int y = (int) (-this.scroll * this.scrollPerEntry * this.entryHeight);
            for (int entryIndex = 0; entryIndex < this.entries.size(); entryIndex++) {
                float entryX = this.getPosX() + 2;
                float entryY = this.getPosY() + y + 1;
                float entryWidth = this.getWidth() - this.entryHeight;
                if (this.isSelected(entryX, entryY, entryWidth, entryHeight, mouseX, mouseY)) {
                    int previousSelected = this.selectedEntry;
                    this.selectedEntry = entryIndex;
                    if (this.function.apply(this)) {
                        this.getGUI().mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ui_button_click, 1.0F));
                    } else {
                        this.selectedEntry = previousSelected;
                    }
                    return true;
                }
                y += this.entryHeight + 1;
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(float mouseX, float mouseY, int button, long timeSinceClick) {
        if (this.scrolling) {
            this.scroll = (int) Math.max(0, Math.min(this.maxScroll / this.scrollPerEntry, mouseY - this.getPosY() - 2 - this.scrollYOffset));
        }
        return this.scrolling;
    }

    @Override
    public boolean mouseReleased(float mouseX, float mouseY, int button) {
        this.scrolling = false;
        return false;
    }

    private boolean isSelected(float entryX, float entryY, float entryWidth, float entryHeight, float mouseX, float mouseY) {
        return ElementHandler.INSTANCE.isElementOnTop(this.getGUI(), this) && mouseX > entryX && mouseX < entryX + entryWidth && mouseY > entryY && mouseY < entryY + entryHeight;
    }

    public String getSelectedEntry() {
        return this.entries.get(selectedEntry);
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedEntry = selectedIndex;
    }
}
