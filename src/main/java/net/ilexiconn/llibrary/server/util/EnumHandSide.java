package net.ilexiconn.llibrary.server.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public enum EnumHandSide {
    LEFT(new ChatComponentTranslation("options.mainHand.left")),
    RIGHT(new ChatComponentTranslation("options.mainHand.right"));

    private final IChatComponent handName;

    EnumHandSide(IChatComponent nameIn) {
        this.handName = nameIn;
    }

    @SideOnly(Side.CLIENT)
    public EnumHandSide opposite() {
        return this == LEFT ? RIGHT : LEFT;
    }

    public String toString() {
        return this.handName.getUnformattedText();
    }
}
