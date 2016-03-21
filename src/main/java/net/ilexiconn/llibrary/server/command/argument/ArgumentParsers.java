package net.ilexiconn.llibrary.server.command.argument;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public enum ArgumentParsers implements IArgumentParser {
    STRING {
        @Override
        public Object parseArgument(MinecraftServer server, ICommandSender sender, String argument) throws CommandException {
            return argument;
        }

        @Override
        public List<String> getTabCompletion(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
            return Collections.emptyList();
        }
    },

    INTEGER {
        @Override
        public Object parseArgument(MinecraftServer server, ICommandSender sender, String argument) throws CommandException {
            return CommandBase.parseInt(argument);
        }

        @Override
        public List<String> getTabCompletion(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
            return Collections.emptyList();
        }
    },

    PLAYER {
        @Override
        public Object parseArgument(MinecraftServer server, ICommandSender sender, String argument) throws CommandException {
            return CommandBase.getPlayer(server, sender, argument);
        }

        @Override
        public List<String> getTabCompletion(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
            return CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
    },

    ITEMSTACK {
        @Override
        public Object parseArgument(MinecraftServer server, ICommandSender sender, String argument) throws CommandException {
            return new ItemStack(CommandBase.getItemByText(sender, argument));
        }

        @Override
        public List<String> getTabCompletion(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
            return CommandBase.getListOfStringsMatchingLastWord(args, Item.itemRegistry.getKeys());
        }
    },

    BOOLEAN {
        @Override
        public Object parseArgument(MinecraftServer server, ICommandSender sender, String argument) throws CommandException {
            return CommandBase.parseBoolean(argument);
        }

        @Override
        public List<String> getTabCompletion(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "true", "false");
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> IArgumentParser<T> getBuiltinParser(Class<T> type) {
        if (String.class.isAssignableFrom(type)) {
            return STRING;
        } else if (Integer.class.isAssignableFrom(type)) {
            return INTEGER;
        } else if (EntityPlayer.class.isAssignableFrom(type)) {
            return PLAYER;
        } else if (ItemStack.class.isAssignableFrom(type)) {
            return ITEMSTACK;
        } else if (Boolean.class.isAssignableFrom(type)) {
            return BOOLEAN;
        } else {
            return null;
        }
    }
}
