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

    @SideOnly(Side.CLIENT)
    public abstract void onClientReceived(Minecraft client, MESSAGE message, EntityPlayer player, MessageContext messageContext);

    public abstract void onServerReceived(MinecraftServer server, MESSAGE message, EntityPlayer player, MessageContext messageContext);
}
