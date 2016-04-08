package net.ilexiconn.llibrary.server.network;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.entity.block.ITrackableTile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
        TileEntity tile = player.worldObj.getTileEntity(message.position);
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
        this.position = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.compound);
        buf.writeLong(this.position.toLong());
    }
}
