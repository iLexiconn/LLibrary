package net.ilexiconn.llibrary.client.util;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.Collections;
import java.util.List;

/**
 * Some helper methods taken from Minecraft 1.8.
 *
 * @author iLexiconn
 * @since 1.0.0
 */
public class FontUtils {
    /**
     * Split the text into the width, with every newline as a new list entry.
     *
     * @param chatComponent  the start component
     * @param width          the max width
     * @param fontRenderer   the font renderer instance
     * @param stripSpaces    true if spaces on the start of newlines should be removed
     * @param keepFormatting true if the text formatting shouldn't be removed
     * @return the list with the splitted chat component
     */
    public static List<IChatComponent> splitText(IChatComponent chatComponent, int width, FontRenderer fontRenderer, boolean stripSpaces, boolean keepFormatting) {
        int i = 0;
        IChatComponent chatComponent0 = new ChatComponentText("");
        List<IChatComponent> list = Lists.newArrayList();
        List<IChatComponent> list1 = Collections.singletonList(chatComponent);

        for (int j = 0; j < ((List) list1).size(); ++j) {
            IChatComponent chatComponent1 = list1.get(j);
            String s = chatComponent1.getUnformattedTextForChat();
            boolean flag = false;

            if (s.contains("\n")) {
                int k = s.indexOf(10);
                String s1 = s.substring(k + 1);
                s = s.substring(0, k + 1);
                ChatComponentText chatComponentText = new ChatComponentText(s1);
                chatComponentText.setChatStyle(chatComponent1.getChatStyle().createShallowCopy());
                list1.add(j + 1, chatComponentText);
                flag = true;
            }

            String s4 = parseFormatting(chatComponent1.getChatStyle().getFormattingCode() + s, keepFormatting);
            String s5 = s4.endsWith("\n") ? s4.substring(0, s4.length() - 1) : s4;
            int i1 = fontRenderer.getStringWidth(s5);
            ChatComponentText chatComponentText = new ChatComponentText(s5);
            chatComponentText.setChatStyle(chatComponent1.getChatStyle().createShallowCopy());

            if (i + i1 > width) {
                String s2 = fontRenderer.trimStringToWidth(s4, width - i, false);
                String s3 = s2.length() < s4.length() ? s4.substring(s2.length()) : null;

                if (s3 != null && !s3.isEmpty()) {
                    int l = s2.lastIndexOf(" ");

                    if (l >= 0 && fontRenderer.getStringWidth(s4.substring(0, l)) > 0) {
                        s2 = s4.substring(0, l);

                        if (stripSpaces) {
                            ++l;
                        }

                        s3 = s4.substring(l);
                    } else if (i > 0 && !s4.contains(" ")) {
                        s2 = "";
                        s3 = s4;
                    }

                    s3 = getFormatFromString(s2) + s3;
                    ChatComponentText chatComponentText1 = new ChatComponentText(s3);
                    chatComponentText1.setChatStyle(chatComponent1.getChatStyle().createShallowCopy());
                    list1.add(j + 1, chatComponentText1);
                }

                i1 = fontRenderer.getStringWidth(s2);
                chatComponentText = new ChatComponentText(s2);
                chatComponentText.setChatStyle(chatComponent1.getChatStyle().createShallowCopy());
                flag = true;
            }

            if (i + i1 <= width) {
                i += i1;
                chatComponent0.appendSibling(chatComponentText);
            } else {
                flag = true;
            }

            if (flag) {
                list.add(chatComponent0);
                i = 0;
                chatComponent0 = new ChatComponentText("");
            }
        }

        list.add(chatComponent0);
        return list;
    }

    /**
     * Get if the input test should keep its formatting. This method checks the Minecraft settings, but it can be
     * overridden with the keepFormatting parameter.
     *
     * @param text           the input text
     * @param keepFormatting false if if the input shouldn't keep the formatting
     * @return the parsed text
     */
    public static String parseFormatting(String text, boolean keepFormatting) {
        return !keepFormatting && !Minecraft.getMinecraft().gameSettings.chatColours ? EnumChatFormatting.getTextWithoutFormattingCodes(text) : text;
    }

    /**
     * Return the last format usd on the input text.
     *
     * @param text the input text
     * @return the last used format code
     */
    public static String getFormatFromString(String text) {
        String result = "";
        int index = -1;
        int length = text.length();

        while ((index = text.indexOf(167, index + 1)) != -1) {
            if (index < length - 1) {
                char character = text.charAt(index + 1);

                if (isFormatColor(character)) {
                    result = "\u00a7" + character;
                } else if (isFormatSpecial(character)) {
                    result = result + "\u00a7" + character;
                }
            }
        }

        return result;
    }

    /**
     * @param character the character to check
     * @return true if this character is a color code
     */
    public static boolean isFormatColor(char character) {
        return character >= 48 && character <= 57 || character >= 97 && character <= 102 || character >= 65 && character <= 70;
    }

    /**
     * @param character the character to check
     * @return true if this is a special character
     */
    public static boolean isFormatSpecial(char character) {
        return character >= 107 && character <= 111 || character >= 75 && character <= 79 || character == 114 || character == 82;
    }
}
