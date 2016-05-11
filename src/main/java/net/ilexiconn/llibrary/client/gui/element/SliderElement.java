package net.ilexiconn.llibrary.client.gui.element;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.ClientProxy;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class SliderElement<T extends GuiScreen> extends Element<T> {
    private float value;
    private Function<SliderElement<T>, Boolean> function;
    private boolean intValue;
    private DecimalFormat decimalFormat;
    private boolean hasSlider;
    private float sliderWidth;
    private float sliderOffset;
    private float minValue;
    private float maxValue;
    private boolean editable = true;
    private boolean dragging;
    private float newValue;

    public SliderElement(T gui, float posX, float posY, Function<SliderElement<T>, Boolean> function) {
        this(gui, posX, posY, false, function);
    }

    public SliderElement(T gui, float posX, float posY, boolean intValue, Function<SliderElement<T>, Boolean> function) {
        this(gui, posX, posY, intValue, 0.0F, -1.0F, -1.0F, function);
    }

    public SliderElement(T gui, float posX, float posY, boolean intValue, float sliderWidth, float minValue, float maxValue, Function<SliderElement<T>, Boolean> function) {
        super(gui, posX, posY, (int) (38 + sliderWidth), 12);
        this.function = function;
        this.intValue = intValue;
        this.decimalFormat = new DecimalFormat("#.#");
        this.hasSlider = sliderWidth > 0.0F;
        this.sliderWidth = sliderWidth;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void render(float mouseX, float mouseY, float partialTicks) {
        float posX = this.getPosX();
        float posY = this.getPosY();
        int width = this.getWidth();
        int height = this.getHeight();
        this.drawRectangle(posX, posY, width, height, this.editable ? LLibrary.CONFIG.getSecondaryColor() : LLibrary.CONFIG.getSecondarySubcolor());
        boolean selected = this.isSelected(mouseX, mouseY);
        boolean upperSelected = this.editable && !this.dragging && selected && mouseX >= posX + width - this.sliderWidth - 11 && mouseY < posY + 6 && mouseX < posX + width - this.sliderWidth;
        boolean lowerSelected = this.editable && !this.dragging && selected && mouseX >= posX + width - this.sliderWidth - 11 && mouseY > posY + 6 && mouseX < posX + width - this.sliderWidth;
        this.drawRectangle(posX + width - 11 - this.sliderWidth, posY, 11, 6, this.editable ? upperSelected ? LLibrary.CONFIG.getDarkAccentColor() : LLibrary.CONFIG.getAccentColor() : LLibrary.CONFIG.getTertiaryColor());
        this.drawRectangle(posX + width - 11 - this.sliderWidth, posY + 6, 11, 6, this.editable ? lowerSelected ? LLibrary.CONFIG.getDarkAccentColor() : LLibrary.CONFIG.getAccentColor() : LLibrary.CONFIG.getTertiaryColor());
        int textColor = LLibrary.CONFIG.getTextColor();
        this.drawRectangle(posX + width - 8 - this.sliderWidth, posY + 4, 5, 1, textColor);
        this.drawRectangle(posX + width - 7 - this.sliderWidth, posY + 3, 3, 1, textColor);
        this.drawRectangle(posX + width - 6 - this.sliderWidth, posY + 2, 1, 1, textColor);
        this.drawRectangle(posX + width - 8 - this.sliderWidth, posY + 7, 5, 1, textColor);
        this.drawRectangle(posX + width - 7 - this.sliderWidth, posY + 8, 3, 1, textColor);
        this.drawRectangle(posX + width - 6 - this.sliderWidth, posY + 9, 1, 1, textColor);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        float scaleFactor = new ScaledResolution(ClientProxy.MINECRAFT).getScaleFactor();
        GL11.glScissor((int) (posX * scaleFactor), (int) ((this.getGUI().height - (posY + height)) * scaleFactor), (int) ((width - 11) * scaleFactor), (int) (height * scaleFactor));
        String text = String.valueOf(this.intValue ? (int) this.value : Float.parseFloat(this.decimalFormat.format(this.value)) + 0.0F);
        this.getGUI().mc.fontRendererObj.drawString(text, posX + 2, posY + 3.0F, textColor, false);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        if (this.hasSlider) {
            float offsetX = ((this.sliderWidth - 4) * (this.value - this.minValue) / (this.maxValue - this.minValue));
            boolean indicatorSelected = this.dragging || this.editable && selected && mouseX >= this.getPosX() + 38 + offsetX && mouseX <= this.getPosX() + 38 + offsetX + 4;
            this.drawRectangle(this.getPosX() + 38 + offsetX, this.getPosY(), 4, this.getHeight(), this.editable ? indicatorSelected ? LLibrary.CONFIG.getDarkAccentColor() : LLibrary.CONFIG.getAccentColor() : LLibrary.CONFIG.getTertiaryColor());
        }
    }

    public SliderElement<T> setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }

    public SliderElement<T> withValue(float value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean mouseClicked(float mouseX, float mouseY, int button) {
        if (!this.editable) {
            return super.mouseClicked(mouseX, mouseY, button);
        }
        float offsetX = ((this.sliderWidth - 4) * (this.value - this.minValue) / (this.maxValue - this.minValue));
        boolean indicatorSelected = this.isSelected(mouseX, mouseY) && mouseX >= this.getPosX() + 38 + offsetX && mouseX <= this.getPosX() + 38 + offsetX + 4;
        boolean upperSelected = this.isSelected(mouseX, mouseY) && mouseX >= this.getPosX() + this.getWidth() - this.sliderWidth - 11 && mouseY < this.getPosY() + 6 && mouseX < this.getPosX() + this.getWidth() - this.sliderWidth;
        boolean lowerSelected = this.isSelected(mouseX, mouseY) && mouseX >= this.getPosX() + this.getWidth() - this.sliderWidth - 11 && mouseY > this.getPosY() + 6 && mouseX < this.getPosX() + this.getWidth() - this.sliderWidth;
        if (upperSelected) {
            float newValue = GuiScreen.isShiftKeyDown() ? this.intValue ? this.value + 10 : this.value + 1 : this.intValue ? this.value + 1 : this.value + 0.1F;
            if (this.maxValue == -1.0F || newValue <= this.maxValue) {
                this.newValue = newValue;
                if (this.function.apply(this)) {
                    this.getGUI().mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ui_button_click, 1.0F));
                    this.value = newValue;
                    return true;
                } else {
                    this.newValue = this.value;
                }
            }
        } else if (lowerSelected) {
            float newValue = GuiScreen.isShiftKeyDown() ? this.intValue ? this.value - 10 : this.value - 1 : this.intValue ? this.value - 1 : this.value - 0.1F;
            if (this.minValue == -1.0F || newValue >= this.minValue) {
                this.newValue = newValue;
                if (this.function.apply(this)) {
                    this.getGUI().mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ui_button_click, 1.0F));
                    this.value = newValue;
                    return true;
                } else {
                    this.newValue = this.value;
                }
            }
        } else if (indicatorSelected) {
            this.dragging = true;
            this.sliderOffset = mouseX - this.getPosX() - 38 - offsetX;
            this.getGUI().mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ui_button_click, 1.0F));
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(float mouseX, float mouseY, int button, long timeSinceClick) {
        if (this.dragging) {
            float newValue = Math.max(minValue, Math.min(maxValue, (((mouseX - sliderOffset - (this.getPosX() + 38.0F)) / ((this.getWidth() - 38.0F) - 4.0F)) * (maxValue - minValue)) + minValue));
            this.newValue = newValue;
            if (this.function.apply(this)) {
                this.value = newValue;
                return true;
            } else {
                this.newValue = this.value;
            }
        }
        return this.dragging;
    }

    @Override
    public boolean mouseReleased(float mouseX, float mouseY, int button) {
        if (this.dragging) {
            this.getGUI().mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ui_button_click, 1.0F));
        }
        this.dragging = false;
        return false;
    }

    public float getNewValue() {
        return newValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public float getValue() {
        return value;
    }
}

