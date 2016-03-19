package net.ilexiconn.llibrary.server.command;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public enum CommandHandler {
    INSTANCE;

    public void registerCommand(FMLServerStartingEvent event, Command command, ICommandExecutor executor) {
        event.registerServerCommand(command.setExector(executor));
    }
}
