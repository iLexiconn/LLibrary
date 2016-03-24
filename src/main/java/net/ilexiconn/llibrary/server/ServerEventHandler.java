package net.ilexiconn.llibrary.server;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.ilexiconn.llibrary.server.config.ConfigHandler;

public enum ServerEventHandler {
    INSTANCE;

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (ConfigHandler.INSTANCE.hasConfigForID(event.modID)) {
            ConfigHandler.INSTANCE.saveConfigForID(event.modID);
        }
    }
}
