package net.ilexiconn.llibrary.server.command.argument;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public class CommandArguments {
    private List<Argument> arguments;
    private ICommandSender commandSender;

    public CommandArguments(List<Argument> arguments, ICommandSender commandSender) {
        this.arguments = arguments;
        this.commandSender = commandSender;
    }

    /**
     * @param name the argument name to check
     * @return true if the user filled in this argument
     */
    public boolean hasArgument(String name) {
        for (Argument argument : this.arguments) {
            if (argument.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the argument and parse it using a specific argument parser.
     *
     * @param name           the argument name
     * @param argumentParser the argument parser
     * @param <T>            the argument type
     * @return the argument value, null if it can't be found
     */
    public <T> T getArgument(String name, IArgumentParser<T> argumentParser) {
        for (Argument argument : this.arguments) {
            if (argument.getName().equals(name)) {
                try {
                    return argumentParser.getValue(this.commandSender.getServer(), this.commandSender, argument.getValue());
                } catch (CommandException e) {
                    this.commandSender.addChatMessage(new TextComponentString(e.getLocalizedMessage()).setChatStyle(new Style().setColor(TextFormatting.RED)));
                }
            }
        }
        return null;
    }

    /**
     * Get the argument with the class type, and parse it using the registered parser.
     *
     * @param name the argument name
     * @param type the argument type
     * @param <T>  the argument type
     * @return the argument value, null if it can't be found
     */
    public <T> T getArgument(String name, Class<T> type) {
        for (Argument argument : this.arguments) {
            if (argument.getName().equals(name)) {
                return type.cast(getArgument(name, argument.getArgumentParser()));
            }
        }
        return null;
    }

    /**
     * @param name the argument name to check
     * @return the argument type. Null if the argument can't be found.
     */
    public IArgumentParser<?> getArgumentType(String name) {
        for (Argument argument : this.arguments) {
            if (argument.getName().equals(name)) {
                return argument.getArgumentParser();
            }
        }
        return null;
    }

    public String getString(String argument) {
        return getArgument(argument, String.class);
    }

    public int getInteger(String argument) {
        return getArgument(argument, Integer.class);
    }

    public EntityPlayer getPlayer(String argument) {
        return getArgument(argument, EntityPlayer.class);
    }

    public ItemStack getItemStack(String argument) {
        return getArgument(argument, ItemStack.class);
    }

    public boolean getBoolean(String argument) {
        return getArgument(argument, Boolean.class);
    }
}
