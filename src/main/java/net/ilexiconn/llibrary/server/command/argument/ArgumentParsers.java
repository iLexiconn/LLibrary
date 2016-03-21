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
        public Object getValue(MinecraftServer server, ICommandSender sender, String argument) throws CommandException {
            return argument;
        }

        @Override
        public List<String> getTabCompletion(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
            return Collections.emptyList();
        }
    },

    INTEGER {
        @Override
        public Object getValue(MinecraftServer server, ICommandSender sender, String argument) throws CommandException {
            return CommandBase.parseInt(argument);
        }

        @Override
        public List<String> getTabCompletion(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
            return Collections.emptyList();
        }
    },

    PLAYER {
        @Override
        public Object getValue(MinecraftServer server, ICommandSender sender, String argument) throws CommandException {
            return CommandBase.getPlayer(server, sender, argument);
        }

        @Override
        public List<String> getTabCompletion(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
            return CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
    },

    ITEMSTACK {
        @Override
        public Object getValue(MinecraftServer server, ICommandSender sender, String argument) throws CommandException {
            return new ItemStack(CommandBase.getItemByText(sender, argument));
        }

        @Override
        public List<String> getTabCompletion(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
            return CommandBase.getListOfStringsMatchingLastWord(args, Item.itemRegistry.getKeys());
        }
    },

    BOOLEAN {
        @Override
        public Object getValue(MinecraftServer server, ICommandSender sender, String argument) throws CommandException {
            return CommandBase.parseBoolean(argument);
        }

        @Override
        public List<String> getTabCompletion(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "true", "false");
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> IArgumentParser<T> getBuiltinParser(Class<T> type) {
        if (type.isAssignableFrom(String.class)) {
            return STRING;
        } else if (type.isAssignableFrom(Integer.class)) {
            return INTEGER;
        } else if (type.isAssignableFrom(EntityPlayer.class)) {
            return PLAYER;
        } else if (type.isAssignableFrom(ItemStack.class)) {
            return ITEMSTACK;
        } else if (type.isAssignableFrom(Boolean.class)) {
            return BOOLEAN;
        } else {
            return null;
        }
    }
}
