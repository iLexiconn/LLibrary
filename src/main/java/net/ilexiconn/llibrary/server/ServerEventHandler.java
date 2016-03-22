package net.ilexiconn.llibrary.server;

import net.ilexiconn.llibrary.server.config.ConfigHandler;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ServerEventHandler {
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (ConfigHandler.INSTANCE.hasConfigForID(event.modID)) {
            ConfigHandler.INSTANCE.saveConfigForID(event.modID);
        }
    }
}
