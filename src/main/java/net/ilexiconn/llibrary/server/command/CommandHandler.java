package net.ilexiconn.llibrary.server.command;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public enum CommandHandler {
    INSTANCE;

    /**
     * Registers the given command, must be called from FMLServerStartingEvent
     *
     * @param event the FMLServerStartingEvent
     * @param command the command to register
     * @param executor a CommandExecuter to execute this command
     */
    public void registerCommand(FMLServerStartingEvent event, Command command, ICommandExecutor executor) {
        event.registerServerCommand(command.setExector(executor));
    }
}
