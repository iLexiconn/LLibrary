package net.ilexiconn.llibrary.server.network;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AbstractMessage<MESSAGE extends AbstractMessage<MESSAGE>> implements IMessage, IMessageHandler<MESSAGE, IMessage> {
    @Override
    public IMessage onMessage(MESSAGE message, MessageContext messageContext) {
        LLibrary.PROXY.handleMessage(message, messageContext);

        return null;
    }

    /**
     * Executes when message received on CLIENT side. Never use fields directly from the class you're in, but use data from the 'message' field instead.
     *
     * @param message The message instance with all variables.
     * @param player  The client player entity.
     */
    @SideOnly(Side.CLIENT)
    public abstract void onClientReceived(Minecraft client, MESSAGE message, EntityPlayer player, MessageContext messageContext);

    /**
     * Executes when message received on SERVER side. Never use fields directly from the class you're in, but use data from the 'message' field instead.
     *
     * @param message The message instance with all variables.
     * @param player  The player who sent the message to the server.
     */
    public abstract void onServerReceived(MinecraftServer server, MESSAGE message, EntityPlayer player, MessageContext messageContext);
}
