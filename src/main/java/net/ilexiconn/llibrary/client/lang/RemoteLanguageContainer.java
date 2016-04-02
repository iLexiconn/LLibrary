package net.ilexiconn.llibrary.client.lang;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author gegy1000
 * @since 1.1.0
 */
@SideOnly(Side.CLIENT)
public class RemoteLanguageContainer {
    public LangContainer[] languages;

    public class LangContainer {
        public String locale;
        public String downloadURL;
    }
}
