package net.ilexiconn.llibrary.client.gui.element;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.ClientProxy;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class ListElement<T extends GuiScreen> extends Element<T> {
    private List<String> entries;
    private Function<ListElement<T>, Boolean> function;
    private ScrollbarElement<T> scrollbar;

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
    }

    @Override
    public void init() {
        this.scrollbar = new ScrollbarElement<>(this.getGUI(), this, () -> this.getWidth() - 8.0F, () -> 2.0F, () -> this.getHeight() - 2.0F, this.entryHeight, () -> this.entries.size());
    }

    @Override
    public void render(float mouseX, float mouseY, float partialTicks) {
        this.drawRectangle(this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight(), this.getColorScheme().getSecondaryColor());
        FontRenderer fontRenderer = this.getGUI().mc.fontRenderer;
        float y = -this.scrollbar.getScrollOffset();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        ScaledResolution scaledResolution = new ScaledResolution(ClientProxy.MINECRAFT, ClientProxy.MINECRAFT.displayWidth, ClientProxy.MINECRAFT.displayHeight);
        int scaleFactor = scaledResolution.getScaleFactor();
        GL11.glScissor((int) (this.getPosX() * scaleFactor), (int) ((this.getGUI().height - (this.getPosY() + this.getHeight()) + 2) * scaleFactor), this.getWidth() * scaleFactor, (this.getHeight() - 4) * scaleFactor);
        int entryIndex = 0;
        for (String entry : this.entries) {
            float entryX = this.getPosX() + 2;
            float entryY = this.getPosY() + y;
            float entryWidth = this.getWidth() - 6;
            boolean selected = this.isSelected(this.getPosX(), this.getPosY() + y, entryWidth, this.entryHeight, mouseX, mouseY) && !this.scrollbar.isScrolling();
            boolean clickSelecting = selected && Mouse.isButtonDown(0);
            if (this.selectedEntry == entryIndex) {
                this.drawRectangle(entryX, entryY, entryWidth, this.entryHeight, clickSelecting ? LLibrary.CONFIG.getAccentColor() : LLibrary.CONFIG.getDarkAccentColor());
            } else {
                this.drawRectangle(entryX, entryY, entryWidth, this.entryHeight, selected ? LLibrary.CONFIG.getAccentColor() : this.getColorScheme().getSecondaryColor());
            }
            this.drawString(entry, entryX + 2, (entryY - fontRenderer.FONT_HEIGHT / 2) + (this.entryHeight / 2), LLibrary.CONFIG.getTextColor(), false);
            y += this.entryHeight;
            entryIndex++;
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        this.drawRectangle(this.getPosX() + this.getWidth() - 9, this.getPosY() + 1, 6, this.getHeight() - 2, this.getColorScheme().getPrimaryColor());
    }

    @Override
    public boolean mouseClicked(float mouseX, float mouseY, int button) {
        if (this.isSelected(mouseX, mouseY)) {
            float y = -this.scrollbar.getScrollOffset();
            for (int entryIndex = 0; entryIndex < this.entries.size(); entryIndex++) {
                float entryX = this.getPosX() + 2;
                float entryY = this.getPosY() + y;
                float entryWidth = this.getWidth() - this.entryHeight;
                if (this.isSelected(entryX, entryY, entryWidth, entryHeight, mouseX, mouseY)) {
                    int previousSelected = this.selectedEntry;
                    this.selectedEntry = entryIndex;
                    if (this.function.apply(this)) {
                        this.getGUI().mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
                    } else {
                        this.selectedEntry = previousSelected;
                    }
                    return true;
                }
                y += this.entryHeight;
            }
        }
        return false;
    }

    private boolean isSelected(float entryX, float entryY, float entryWidth, float entryHeight, float mouseX, float mouseY) {
        return ElementHandler.INSTANCE.isElementOnTop(this.getGUI(), this) && mouseX > entryX && mouseX < entryX + entryWidth && mouseY > entryY && mouseY < entryY + entryHeight;
    }

    public int getSelectedIndex() {
        return this.selectedEntry;
    }

    public String getSelectedEntry() {
        return this.entries.get(selectedEntry);
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedEntry = selectedIndex;
    }
}
