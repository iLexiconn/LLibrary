package net.ilexiconn.llibrary.server.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.entity.block.BlockPos;
import net.ilexiconn.llibrary.server.entity.block.ITrackableTile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

public class TileTrackMessage extends AbstractMessage<TileTrackMessage> {
    private NBTTagCompound compound;
    private BlockPos position;

    public TileTrackMessage() {

    }

    public TileTrackMessage(BlockPos pos, ITrackableTile tile) {
        this.compound = new NBTTagCompound();
        tile.saveTrackingData(this.compound);
        this.position = pos;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, TileTrackMessage message, EntityPlayer player, MessageContext messageContext) {
        handle(message, player);
    }

    @Override
    public void onServerReceived(MinecraftServer server, TileTrackMessage message, EntityPlayer player, MessageContext messageContext) {
        handle(message, player);
    }

    private void handle(TileTrackMessage message, EntityPlayer player) {
        TileEntity tile = player.worldObj.getTileEntity(message.position.getX(), message.position.getY(), message.position.getZ());
        if (tile != null && tile instanceof ITrackableTile) {
            ITrackableTile trackedTile = (ITrackableTile) tile;
            trackedTile.readTrackingData(message.compound);
        } else {
            LLibrary.LOGGER.warn("Invalid tile tracker packet! Expected ITrackableTile at position " + message.position + " but got " + tile + "!");
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.compound = ByteBufUtils.readTag(buf);
        this.position = BlockPos.read(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.compound);
        this.position.write(buf);
    }
}
