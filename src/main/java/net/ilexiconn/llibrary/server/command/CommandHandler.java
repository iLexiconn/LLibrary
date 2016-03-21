package net.ilexiconn.llibrary.server.command;

import net.ilexiconn.llibrary.server.command.argument.ArgumentParsers;
import net.ilexiconn.llibrary.server.command.argument.IArgumentParser;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public enum CommandHandler {
    INSTANCE;

    private Map<Class<?>, IArgumentParser<?>> argumentParserMap = new HashMap<>();

    /**
     * Register an argument parser.
     *
     * @param type           the argument type
     * @param argumentParser the argument parser
     * @param <T>            the argument type
     */
    public <T> void registerArgumentParser(Class<T> type, IArgumentParser<T> argumentParser) {
        this.argumentParserMap.put(type, argumentParser);
    }

    /**
     * Get the argument parser for a specefic type. Returns null if none can be found.
     *
     * @param type the argument type
     * @param <T> the argument type
     * @return the argument parser, null if it can't be found
     */
    public <T> IArgumentParser<T> getParserForType(Class<T> type) {
        IArgumentParser<T> argumentParser = ArgumentParsers.getBuiltinParser(type);
        if (argumentParser != null) {
            return argumentParser;
        } else if (this.argumentParserMap.containsKey(type)) {
            return (IArgumentParser<T>) this.argumentParserMap.get(type);
        } else {
            return null;
        }
    }

    /**
     * Registers the given command, must be called from FMLServerStartingEvent
     *
     * @param event    the FMLServerStartingEvent
     * @param command  the command to register
     * @param executor a CommandExecuter to execute this command
     */
    public void registerCommand(FMLServerStartingEvent event, Command command, ICommandExecutor executor) {
        event.registerServerCommand(command.setExector(executor));
    }
}
