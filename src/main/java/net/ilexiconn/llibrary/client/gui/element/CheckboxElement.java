package net.ilexiconn.llibrary.client.gui.element;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class CheckboxElement<T extends GuiScreen> extends Element<T> {
    private boolean selected;
    private Function<CheckboxElement<T>, Boolean> function;

    public CheckboxElement(T gui, float posX, float posY) {
        this(gui, posX, posY, null);
    }

    public CheckboxElement(T gui, float posX, float posY, Function<CheckboxElement<T>, Boolean> function) {
        super(gui, posX, posY, 12, 12);
        this.function = function;
    }

    @Override
    public void render(float mouseX, float mouseY, float partialTicks) {
        if (this.isEnabled()) {
            this.drawRectangle(this.getPosX() + 1, this.getPosY() + 1, this.getWidth() - 1, this.getHeight() - 1, LLibrary.CONFIG.getSecondaryColor());
        } else {
            this.drawRectangle(this.getPosX() + 1, this.getPosY() + 1, this.getWidth() - 1, this.getHeight() - 1, LLibrary.CONFIG.getTertiaryColor());
        }
        if (this.selected) {
            this.drawRectangle(this.getPosX() + 3, this.getPosY() + 3, this.getWidth() - 5, this.getHeight() - 5, LLibrary.CONFIG.getTextColor());
        }
    }

    @Override
    public boolean mouseClicked(float mouseX, float mouseY, int button) {
        if (button == 0 && super.isSelected(mouseX, mouseY)) {
            this.selected = !this.selected;
            if (this.function != null && this.function.apply(this)) {
                this.getGUI().mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ui_button_click, 1.0F));
            } else {
                this.selected = !this.selected;
            }
            return true;
        }
        return false;
    }

    public CheckboxElement<T> withSelection(boolean selected) {
        this.selected = selected;
        return this;
    }

    public boolean isSelected() {
        return selected;
    }
}

