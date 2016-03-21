package net.ilexiconn.llibrary.server.command.argument;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public interface IArgumentParser<ARGUMENT> {
    /**
     * Parse the strng argument into an object.
     *
     * @param server   the server instance
     * @param sender   the command sender
     * @param argument the argument
     * @return the parsed argument
     * @throws CommandException if something goes wrong
     */
    ARGUMENT getValue(MinecraftServer server, ICommandSender sender, String argument) throws CommandException;

    /**
     * Get the tab completion for this argument.
     *
     * @param server the server instance
     * @param sender the command sender
     * @param args   the current typed-in arguments
     * @param pos    the blockpos from the sender
     * @return the tab completion options
     */
    List<String> getTabCompletion(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos);
}
