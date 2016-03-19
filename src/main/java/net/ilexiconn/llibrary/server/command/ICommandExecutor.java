package net.ilexiconn.llibrary.server.command;

import net.ilexiconn.llibrary.server.command.argument.CommandArguments;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@FunctionalInterface
public interface ICommandExecutor {
    void execute(MinecraftServer server, ICommandSender sender, CommandArguments arguments) throws CommandException;
}
