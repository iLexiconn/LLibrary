package net.ilexiconn.llibrary.client.gui.element;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.ClientProxy;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class SliderElement<T extends IElementGUI> extends Element<T> {
    private Function<Float, Boolean> onEnter;
    private Function<Integer, Boolean> allowKey;
    private boolean isInteger;
    private DecimalFormat decimalFormat;
    private boolean hasSlider;
    private float sliderWidth;
    private float minValue;
    private float maxValue;
    private boolean editable = true;
    private boolean dragging;
    private InputElement<T> value;
    private Float nextValue;

    public SliderElement(T gui, float posX, float posY, Function<Float, Boolean> onEnter) {
        this(gui, posX, posY, false, onEnter);
    }

    public SliderElement(T gui, float posX, float posY, boolean isInteger, Function<Float, Boolean> onEnter) {
        this(gui, posX, posY, isInteger, 0.0F, -1.0F, -1.0F, onEnter, (key) -> true);
    }

    public SliderElement(T gui, float posX, float posY, boolean isInteger, float sliderWidth, float minValue, float maxValue, Function<Float, Boolean> onEnter, Function<Integer, Boolean> allowKey) {
        super(gui, posX, posY, (int) (38 + sliderWidth), 12);
        this.onEnter = onEnter;
        this.isInteger = isInteger;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        this.decimalFormat = new DecimalFormat("#.#", symbols);
        this.hasSlider = sliderWidth > 0.0F;
        this.sliderWidth = sliderWidth;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.allowKey = allowKey;
    }

    @Override
    public void init() {
        this.value = (InputElement<T>) new InputElement<>(this.gui, "0.0", -1.0F, 0.0F, 28, true, (input) -> {
            float value1 = 0.0F;
            String text = input.getText();
            if (!this.isInteger) {
                if (text.endsWith("\\.")) {
                    text += "0";
                } else if (text.startsWith("\\.")) {
                    text = "0" + text;
                }
            }
            if (text.length() == 0) {
                text = "0";
            }
            value1 = Float.parseFloat(text);
            this.withValue(value1);
            this.onEnter.apply(value1);
        }, this.allowKey).withParent((Element<IElementGUI>) this);
        if (this.nextValue != null) {
            this.withValue(this.nextValue);
        }
    }

    @Override
    public void render(float mouseX, float mouseY, float partialTicks) {
        float posX = this.getPosX();
        float posY = this.getPosY();
        int width = this.getWidth();
        int height = this.getHeight();
        this.drawRectangle(posX, posY, width, height, this.editable ? LLibrary.CONFIG.getSecondaryColor() : LLibrary.CONFIG.getSecondarySubcolor());
        boolean selected = this.isSelected(mouseX, mouseY);
        boolean upperSelected = this.editable && selected && mouseX >= posX + width - this.sliderWidth - 11 && mouseY < posY + 6 && mouseX < posX + width - this.sliderWidth;
        boolean lowerSelected = this.editable && selected && mouseX >= posX + width - this.sliderWidth - 11 && mouseY > posY + 6 && mouseX < posX + width - this.sliderWidth;
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
        GL11.glScissor((int) (posX * scaleFactor), (int) ((this.gui.getHeight() - (posY + height)) * scaleFactor), (int) ((width - 11) * scaleFactor), (int) (height * scaleFactor));
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        if (this.hasSlider) {
            float offsetX = ((this.sliderWidth - 4) * (this.getValue() - this.minValue) / (this.maxValue - this.minValue));
            boolean indicatorSelected = this.editable && selected && mouseX >= this.getPosX() + 38 + offsetX && mouseX <= this.getPosX() + 38 + offsetX + 4;
            this.drawRectangle(this.getPosX() + 38 + offsetX, this.getPosY(), 4, this.getHeight(), this.editable ? indicatorSelected ? LLibrary.CONFIG.getDarkAccentColor() : LLibrary.CONFIG.getAccentColor() : LLibrary.CONFIG.getTertiaryColor());
        }
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        this.value.setEditable(editable);
    }

    public SliderElement<T> withValue(float value) {
        if (this.value == null) {
            nextValue = value;
        } else {
            this.value.clearText();
            if (this.isInteger) {
                this.value.writeText(String.valueOf((int) value));
            } else {
                this.value.writeText(String.valueOf(Float.parseFloat(this.decimalFormat.format(value)) + 0.0F));
            }
        }
        return this;
    }

    @Override
    public boolean mouseClicked(float mouseX, float mouseY, int button) {
        if (!this.editable) {
            return super.mouseClicked(mouseX, mouseY, button);
        }
        float offsetX = ((this.sliderWidth - 4) * (this.getValue() - this.minValue) / (this.maxValue - this.minValue));
        boolean selected = this.isSelected(mouseX, mouseY);
        boolean indicatorSelected = selected && mouseX >= this.getPosX() + 38 + offsetX && mouseX <= this.getPosX() + 38 + offsetX + 4;
        boolean upperSelected = selected && mouseX >= this.getPosX() + this.getWidth() - this.sliderWidth - 11 && mouseY < this.getPosY() + 6 && mouseX < this.getPosX() + this.getWidth() - this.sliderWidth;
        boolean lowerSelected = selected && mouseX >= this.getPosX() + this.getWidth() - this.sliderWidth - 11 && mouseY > this.getPosY() + 6 && mouseX < this.getPosX() + this.getWidth() - this.sliderWidth;
        if (upperSelected) {
            float newValue = GuiScreen.isShiftKeyDown() ? this.isInteger ? this.getValue() + 10 : this.getValue() + 1 : this.isInteger ? this.getValue() + 1 : this.getValue() + 0.1F;
            if (this.maxValue == -1.0F || newValue <= this.maxValue) {
                if (this.onEnter.apply(newValue)) {
                    this.gui.playClickSound();
                    this.withValue(newValue);
                    return true;
                }
            }
        } else if (lowerSelected) {
            float newValue = GuiScreen.isShiftKeyDown() ? this.isInteger ? this.getValue() - 10 : this.getValue() - 1 : this.isInteger ? this.getValue() - 1 : this.getValue() - 0.1F;
            if (this.minValue == -1.0F || newValue >= this.minValue) {
                if (this.onEnter.apply(newValue)) {
                    this.gui.playClickSound();
                    this.withValue(newValue);
                    return true;
                }
            }
        } else if (indicatorSelected) {
            this.dragging = true;
            this.gui.playClickSound();
            return true;
        }
        return false;
    }

    private float getValue() {
        return this.value.getText().length() > 0 ? Float.parseFloat(this.value.getText()) : 0.0F;
    }

    @Override
    public boolean mouseDragged(float mouseX, float mouseY, int button, long timeSinceClick) {
        if (this.dragging) {
            float newValue = Math.max(minValue, Math.min(maxValue, (((mouseX - (this.getPosX() + 38.0F)) / ((this.getWidth() - 38.0F) - 4.0F)) * (maxValue - minValue)) + minValue));
            if (this.onEnter.apply(newValue)) {
                this.withValue(newValue);
                return true;
            }
        }
        return this.dragging;
    }

    @Override
    public boolean mouseReleased(float mouseX, float mouseY, int button) {
        if (this.dragging) {
            this.gui.playClickSound();
        }
        this.dragging = false;
        return false;
    }

    public InputElement getValueInput() {
        return value;
    }
}
