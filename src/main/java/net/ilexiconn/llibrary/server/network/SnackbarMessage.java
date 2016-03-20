package net.ilexiconn.llibrary.server.network;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.snackbar.Snackbar;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SnackbarMessage extends AbstractMessage<SnackbarMessage> {
    private Snackbar snackbar;

    public SnackbarMessage() {

    }

    public SnackbarMessage(Snackbar snackbar) {
        this.snackbar = snackbar;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, SnackbarMessage message, EntityPlayer player, MessageContext messageContext) {
        LLibrary.PROXY.showSnackbar(message.snackbar);
    }

    @Override
    public void onServerReceived(MinecraftServer server, SnackbarMessage message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.snackbar = Snackbar.create(ByteBufUtils.readUTF8String(byteBuf)).setDuration(byteBuf.readInt());
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, this.snackbar.getMessage());
        byteBuf.writeInt(this.snackbar.getDuration());
    }
}
