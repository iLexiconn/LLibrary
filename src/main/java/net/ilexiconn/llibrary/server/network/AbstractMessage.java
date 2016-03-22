package net.ilexiconn.llibrary.server.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public abstract class AbstractMessage<T extends AbstractMessage<T>> implements IMessage, IMessageHandler<T, IMessage> {
    @Override
    public IMessage onMessage(T message, MessageContext messageContext) {
        LLibrary.PROXY.handleMessage(message, messageContext);

        return null;
    }

    /**
     * Executes when the message is received on CLIENT side. Never use fields directly from the class you're in, but
     * use data from the 'message' argument instead.
     *
     * @param message The message instance with all variables.
     * @param player  The client player entity.
     */
    @SideOnly(Side.CLIENT)
    public abstract void onClientReceived(Minecraft client, T message, EntityPlayer player, MessageContext messageContext);

    /**
     * Executes when the message is received on SERVER side. Never use fields directly from the class you're in, but
     * use data from the 'message' argument instead.
     *
     * @param message The message instance with all variables.
     * @param player  The player who sent the message to the server.
     */
    public abstract void onServerReceived(MinecraftServer server, T message, EntityPlayer player, MessageContext messageContext);
}
