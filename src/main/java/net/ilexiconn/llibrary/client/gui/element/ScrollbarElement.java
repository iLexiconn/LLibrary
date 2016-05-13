package net.ilexiconn.llibrary.client.gui.element;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
public class ScrollbarElement<T extends GuiScreen> extends Element<T> {
    private int maxScroll;
    private float scrollPerEntry;
    private Supplier<Integer> entryCount;
    private float entryHeight;
    private Supplier<Float> offsetX;
    private Supplier<Float> offsetY;
    private Supplier<Float> displayHeight;

    private int scroll;
    private int scrollYOffset;
    private boolean scrolling;

    public ScrollbarElement(Element<T> parent, Supplier<Float> posX, Supplier<Float> posY, Supplier<Float> displayHeight, int entryHeight, Supplier<Integer> entryCount) {
        super(parent.getGUI(), posX.get(), posY.get(), 4, 0);
        this.withParent(parent);
        this.offsetX = posX;
        this.offsetY = posY;
        this.entryHeight = entryHeight;
        this.entryCount = entryCount;
        this.displayHeight = displayHeight;
    }

    @Override
    public void render(float mouseX, float mouseY, float partialTicks) {
        if (this.maxScroll > 0) {
            this.drawRectangle(this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight() - 3, this.scrolling ? LLibrary.CONFIG.getAccentColor() : LLibrary.CONFIG.getSecondaryColor());
        }
    }

    @Override
    public boolean mouseClicked(float mouseX, float mouseY, int button) {
        if (this.maxScroll > 0 && button == 0) {
            if (this.isSelected(mouseX, mouseY)) {
                this.scrolling = true;
                this.scrollYOffset = (int) (mouseY - this.getPosY());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(float mouseX, float mouseY, int button, long timeSinceClick) {
        if (this.scrolling) {
            this.scroll = (int) Math.max(0, Math.min(this.maxScroll / this.scrollPerEntry, mouseY - this.scrollYOffset - (this.offsetY.get() + this.getParent().getPosY())));
        }
        return this.scrolling;
    }

    @Override
    public boolean mouseReleased(float mouseX, float mouseY, int button) {
        this.scrolling = false;
        return false;
    }

    @Override
    public void update() {
        this.setPosX(this.offsetX.get());
        this.setPosY(this.offsetY.get() + this.scroll);
        float parentHeight = this.getParent().getHeight();
        int maxDisplayEntries = (int) (parentHeight / entryHeight);
        int entryCount = this.entryCount.get();
        this.maxScroll = Math.max(0, entryCount - maxDisplayEntries);
        this.scrollPerEntry = (float) entryCount / parentHeight;
        this.setHeight((int) (parentHeight / ((float) entryCount / (float) maxDisplayEntries)));

        if (this.scroll > this.maxScroll / this.scrollPerEntry) {
            this.scroll = (int) (this.maxScroll / this.scrollPerEntry);
        }
    }

    public float getScrollOffset() {
        return this.scroll * (this.entryCount.get() / this.displayHeight.get()) * this.entryHeight;
    }

    public boolean isScrolling() {
        return scrolling;
    }

    public int getMaxScroll() {
        return maxScroll;
    }
}
