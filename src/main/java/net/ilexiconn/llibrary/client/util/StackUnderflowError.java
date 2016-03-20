package net.ilexiconn.llibrary.client.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class StackUnderflowError extends Error {
    public StackUnderflowError() {
        super();
    }

    public StackUnderflowError(String s) {
        super(s);
    }
}
