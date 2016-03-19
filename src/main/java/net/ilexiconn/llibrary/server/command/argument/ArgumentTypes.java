package net.ilexiconn.llibrary.server.command.argument;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;

public enum ArgumentTypes {
    STRING,
    INTEGER,
    PLAYER,
    ITEMSTACK,
    BOOLEAN;

    public Object getValue(MinecraftServer server, ICommandSender sender, String argument) throws CommandException {
        switch (this) {
            case INTEGER:
                return CommandBase.parseInt(argument);
            case PLAYER:
                return CommandBase.getPlayer(server, sender, argument);
            case ITEMSTACK:
                return new ItemStack(CommandBase.getItemByText(sender, argument));
            case BOOLEAN:
                return CommandBase.parseBoolean(argument);
            default:
                return argument;
        }
    }

    public List<String> getTabCompletion(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        switch (this) {
            case PLAYER:
                return CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
            case ITEMSTACK:
                return CommandBase.getListOfStringsMatchingLastWord(args, Item.itemRegistry.getKeys());
            case BOOLEAN:
                return CommandBase.getListOfStringsMatchingLastWord(args, "true", "false");
            default:
                return Collections.emptyList();
        }
    }
}
