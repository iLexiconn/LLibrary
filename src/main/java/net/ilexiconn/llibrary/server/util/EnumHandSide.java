package net.ilexiconn.llibrary.server.util;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
