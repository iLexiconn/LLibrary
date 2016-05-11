package net.ilexiconn.llibrary.client.gui.element;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.util.ClientUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class InputElement<T extends GuiScreen> extends Element<T> {
    private String text;
    private boolean selected;
    private int lineScrollOffset;
    private int cursorPosition;
    private int selectionEnd;
    private int cursorCounter;
    private String newText;
    private Function<InputElement<T>, Boolean> function;

    public InputElement(T gui, String text, float posX, float posY, int width) {
        this(gui, text, posX, posY, width, null);
    }

    public InputElement(T gui, String text, float posX, float posY, int width, Function<InputElement<T>, Boolean> function) {
        super(gui, posX, posY, width, 12);
        this.text = text != null ? text : "";
        this.function = function;
    }

    @Override
    public void update() {
        this.cursorCounter++;
    }

    @Override
    public void render(float mouseX, float mouseY, float partialTicks) {
        this.drawRectangle(this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight(), this.isEnabled() ? LLibrary.CONFIG.getSecondaryColor() : LLibrary.CONFIG.getSecondarySubcolor());
        int cursor = this.cursorPosition - this.lineScrollOffset;
        int cursorEnd = this.selectionEnd - this.lineScrollOffset;
        String displayString = this.getGUI().mc.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
        boolean verticalCursor = cursor >= 0 && cursor <= displayString.length();
        boolean renderCursor = this.selected && this.cursorCounter / 6 % 2 == 0 && verticalCursor;
        float x = this.getPosX();
        float y = this.getPosY();
        float line = x;
        if (cursorEnd > displayString.length()) {
            cursorEnd = displayString.length();
        }
        if (!displayString.isEmpty()) {
            String s = verticalCursor ? displayString.substring(0, cursor) : displayString;
            line = this.drawString(s, x + 3, y + 1 + getHeight() / 2 - this.getGUI().mc.fontRenderer.FONT_HEIGHT / 2, LLibrary.CONFIG.getTextColor(), false);
        }
        boolean renderVerticalCursor = this.cursorPosition < this.text.length();
        float lineX = line;
        if (!verticalCursor) {
            lineX = cursor > 0 ? x + this.getWidth() : x;
        } else if (renderVerticalCursor) {
            lineX = line - 1;
            --line;
        }
        if (!displayString.isEmpty() && verticalCursor && cursor < displayString.length()) {
            this.drawString(displayString.substring(cursor), line + 1, y + 1 + this.getHeight() / 2 - this.getGUI().mc.fontRenderer.FONT_HEIGHT / 2, LLibrary.CONFIG.getTextColor(), false);
        }
        if (renderCursor) {
            if (renderVerticalCursor) {
                this.drawRectangle(lineX, y, 1, this.getHeight() / 2 - this.getGUI().mc.fontRenderer.FONT_HEIGHT / 2 + 1 + this.getGUI().mc.fontRenderer.FONT_HEIGHT, LLibrary.CONFIG.getPrimaryColor());
            } else {
                this.drawString("_", cursor == 0 ? lineX + 3 : lineX, y + 1 + this.getHeight() / 2 - this.getGUI().mc.fontRenderer.FONT_HEIGHT / 2, LLibrary.CONFIG.getPrimaryColor(), false);
            }
        }
        if (cursorEnd != cursor) {
            float selectionWidth = x + this.getGUI().mc.fontRenderer.getStringWidth(displayString.substring(0, cursorEnd));
            this.drawCursorVertical(lineX + (selectionEnd > cursorPosition ? 0 : 1), y, selectionWidth + (selectionEnd < cursorPosition ? 2 : 3), y + getHeight() / 2 - this.getGUI().mc.fontRenderer.FONT_HEIGHT / 2 + 1 + this.getGUI().mc.fontRenderer.FONT_HEIGHT);
        }
    }

    @Override
    public boolean mouseClicked(float mouseX, float mouseY, int button) {
        this.selected = this.isMouseSelecting(mouseX, mouseY);
        if (this.selected && button == 0 && this.isEnabled()) {
            int width = (int) (mouseX - this.getPosX() - 1);
            String displayString = this.getGUI().mc.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            this.setCursorPosition(this.getGUI().mc.fontRenderer.trimStringToWidth(displayString, width).length() + this.lineScrollOffset);
        }
        return false;
    }

    @Override
    public boolean keyPressed(char character, int key) {
        if (!this.selected) {
            return false;
        }
        if (ClientUtils.isKeyComboCtrlA(key)) {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
            return true;
        } else if (ClientUtils.isKeyComboCtrlC(key)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            return true;
        } else if (ClientUtils.isKeyComboCtrlV(key)) {
            this.writeText(GuiScreen.getClipboardString());
            return true;
        } else if (ClientUtils.isKeyComboCtrlX(key)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            this.writeText("");
            return true;
        } else {
            switch (key) {
                case Keyboard.KEY_BACK:
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.deleteWords(-1);
                    } else {
                        this.deleteFromCursor(-1);
                    }
                    return true;
                case Keyboard.KEY_HOME:
                    if (GuiScreen.isShiftKeyDown()) {
                        this.setSelectionPos(0);
                    } else {
                        this.setCursorPositionStart();
                    }
                    return true;
                case Keyboard.KEY_LEFT:
                    if (GuiScreen.isShiftKeyDown()) {
                        if (GuiScreen.isCtrlKeyDown()) {
                            this.setSelectionPos(this.getNthWordFromPos(-1, this.selectionEnd));
                        } else {
                            this.setSelectionPos(this.selectionEnd - 1);
                        }
                    } else if (GuiScreen.isCtrlKeyDown()) {
                        this.setCursorPosition(this.getNthWordFromCursor(-1));
                    } else {
                        this.moveCursorBy(-1);
                    }
                    return true;
                case Keyboard.KEY_RIGHT:
                    if (GuiScreen.isShiftKeyDown()) {
                        if (GuiScreen.isCtrlKeyDown()) {
                            this.setSelectionPos(this.getNthWordFromPos(1, this.selectionEnd));
                        } else {
                            this.setSelectionPos(this.selectionEnd + 1);
                        }
                    } else if (GuiScreen.isCtrlKeyDown()) {
                        this.setCursorPosition(this.getNthWordFromCursor(1));
                    } else {
                        this.moveCursorBy(1);
                    }
                    return true;
                case Keyboard.KEY_END:
                    if (GuiScreen.isShiftKeyDown()) {
                        this.setSelectionPos(this.text.length());
                    } else {
                        this.setCursorPositionEnd();
                    }
                    return true;
                case Keyboard.KEY_DELETE:
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.deleteWords(1);
                    } else {
                        this.deleteFromCursor(1);
                    }
                    return true;
                default:
                    if (ChatAllowedCharacters.isAllowedCharacter(character)) {
                        this.writeText(Character.toString(character));
                        return true;
                    } else {
                        return false;
                    }
            }
        }
    }

    private boolean isMouseSelecting(float mouseX, float mouseY) {
        return mouseX >= this.getPosX() && mouseX < this.getPosX() + this.getWidth() && mouseY >= this.getPosY() && mouseY < this.getPosY() + this.getHeight();
    }

    public String getText() {
        return this.text;
    }

    public String getNewText() {
        return this.newText;
    }

    public String getSelectedText() {
        int start = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int end = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(start, end);
    }

    public void writeText(String text) {
        String newText = "";
        String allowedText = ChatAllowedCharacters.filerAllowedCharacters(text);
        int start = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int end = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;

        if (!this.text.isEmpty()) {
            newText = newText + this.text.substring(0, start);
        }

        newText = newText + allowedText;

        if (!this.text.isEmpty() && end < this.text.length()) {
            newText = newText + this.text.substring(end);
        }

        this.newText = text;
        if (this.function == null || this.function.apply(this)) {
            this.text = newText;
            this.moveCursorBy(start - this.selectionEnd + allowedText.length());
        } else {
            this.newText = text;
        }
    }

    public void deleteWords(int amount) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                this.deleteFromCursor(this.getNthWordFromCursor(amount) - this.cursorPosition);
            }
        }
    }

    public void deleteFromCursor(int amount) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                boolean delete = amount < 0;
                int start = delete ? this.cursorPosition + amount : this.cursorPosition;
                int end = delete ? this.cursorPosition : this.cursorPosition + amount;
                String nextText = "";

                if (start >= 0) {
                    nextText = this.text.substring(0, start);
                }

                if (end < this.text.length()) {
                    nextText = nextText + this.text.substring(end);
                }

                this.newText = text;
                if (this.function == null || this.function.apply(this)) {
                    this.text = nextText;
                    if (delete) {
                        this.moveCursorBy(amount);
                    }
                } else {
                    this.newText = text;
                }
            }
        }
    }

    private int getNthWordFromCursor(int numWords) {
        return this.getNthWordFromPos(numWords, this.cursorPosition);
    }

    private int getNthWordFromPos(int n, int position) {
        return this.getNthWordFromPosWhitespace(n, position, true);
    }

    private int getNthWordFromPosWhitespace(int n, int position, boolean skipWhitespace) {
        int currentPos = position;
        boolean flag = n < 0;
        int j = Math.abs(n);
        for (int k = 0; k < j; ++k) {
            if (!flag) {
                int l = this.text.length();
                currentPos = this.text.indexOf(32, currentPos);
                if (currentPos == -1) {
                    currentPos = l;
                } else {
                    while (skipWhitespace && currentPos < l && this.text.charAt(currentPos) == 32) {
                        ++currentPos;
                    }
                }
            } else {
                while (skipWhitespace && currentPos > 0 && this.text.charAt(currentPos - 1) == 32) {
                    --currentPos;
                }

                while (currentPos > 0 && this.text.charAt(currentPos - 1) != 32) {
                    --currentPos;
                }
            }
        }

        return currentPos;
    }

    public void moveCursorBy(int amount) {
        this.setCursorPosition(this.selectionEnd + amount);
    }

    public void setCursorPosition(int position) {
        this.cursorPosition = position;
        this.cursorPosition = MathHelper.clamp_int(this.cursorPosition, 0, this.text.length());
        this.setSelectionPos(this.cursorPosition);
    }

    public void setCursorPositionStart() {
        this.setCursorPosition(0);
    }

    public void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }

    public void setSelectionPos(int position) {
        int textLength = this.text.length();

        if (position > textLength) {
            position = textLength;
        } else if (position < 0) {
            position = 0;
        }

        this.selectionEnd = position;

        if (this.lineScrollOffset > textLength) {
            this.lineScrollOffset = textLength;
        }

        String displayText = this.getGUI().mc.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
        int offset = displayText.length() + this.lineScrollOffset;

        if (position == this.lineScrollOffset) {
            this.lineScrollOffset -= this.getGUI().mc.fontRenderer.trimStringToWidth(this.text, this.getWidth(), true).length();
        }

        if (position > offset) {
            this.lineScrollOffset += position - offset;
        } else if (position <= this.lineScrollOffset) {
            this.lineScrollOffset -= this.lineScrollOffset - position;
        }

        this.lineScrollOffset = MathHelper.clamp_int(this.lineScrollOffset, 0, textLength);
    }

    public void clearText() {
        this.setCursorPositionStart();
        this.newText = "";
        if (this.function == null || this.function.apply(this)) {
            this.text = this.newText;
        } else {
            this.newText = text;
        }
    }

    private void drawCursorVertical(float startX, float startY, float endX, float endY) {
        if (startX < endX) {
            float prevStartX = startX;
            startX = endX;
            endX = prevStartX;
        }

        if (startY < endY) {
            float prevStartY = startY;
            startY = endY;
            endY = prevStartY;
        }

        if (endX > this.getPosX() + this.getWidth()) {
            endX = this.getPosY() + this.getWidth();
        }

        if (startX > this.getPosX() + this.getWidth()) {
            startX = this.getPosY() + this.getWidth();
        }

        Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glLogicOp(GL11.GL_OR_REVERSE);
        tessellator.startDrawingQuads();
        tessellator.addVertex((double)startX, (double)endY, 0.0D);
        tessellator.addVertex((double)endX, (double)endY, 0.0D);
        tessellator.addVertex((double)endX, (double)startY, 0.0D);
        tessellator.addVertex((double)startX, (double)startY, 0.0D);
        tessellator.draw();
        GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
