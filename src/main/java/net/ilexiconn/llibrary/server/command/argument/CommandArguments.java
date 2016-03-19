package net.ilexiconn.llibrary.server.command.argument;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class CommandArguments {
    private List<Argument> arguments;
    private ICommandSender commandSender;

    public CommandArguments(List<Argument> arguments, ICommandSender commandSender) {
        this.arguments = arguments;
        this.commandSender = commandSender;
    }

    public boolean hasArgument(String name) {
        for (Argument argument : this.arguments) {
            if (argument.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public <T> T getArgument(String name) {
        for (Argument argument : this.arguments) {
            if (argument.getName().equals(name)) {
                try {
                    return (T) argument.getType().getValue(this.commandSender.getServer(), this.commandSender, argument.getValue());
                } catch (CommandException e) {
                    this.commandSender.addChatMessage(new TextComponentString(e.getLocalizedMessage()).setChatStyle(new Style().setColor(TextFormatting.RED)));
                }
            }
        }
        return null;
    }

    public ArgumentTypes getArgumentType(String name) {
        for (Argument argument : this.arguments) {
            if (argument.getName().equals(name)) {
                return argument.getType();
            }
        }
        return null;
    }

    public String getString(String argument) {
        return getArgument(argument);
    }

    public int getInteger(String argument) {
        return (Integer) getArgument(argument);
    }

    public EntityPlayer getPlayer(String argument) {
        return (EntityPlayer) getArgument(argument);
    }

    public ItemStack getItemStack(String argument) {
        return (ItemStack) getArgument(argument);
    }

    public boolean getBoolean(String argument) {
        return (Boolean) getArgument(argument);
    }
}
