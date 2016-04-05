package net.ilexiconn.llibrary.server.config;

import cpw.mods.fml.relauncher.Side;

public class LLibraryConfig {
    @ConfigEntry(category = "client", side = Side.CLIENT)
    public boolean patreonEffects = true;

    @ConfigEntry
    public boolean versionCheck = true;
}
