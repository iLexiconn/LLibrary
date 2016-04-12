package net.ilexiconn.llibrary.server.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.entity.block.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class BlockEntityMessage extends AbstractMessage<BlockEntityMessage> {
    private int x;
    private int y;
    private int z;
    private NBTTagCompound compound;

    public BlockEntityMessage() {

    }

    public BlockEntityMessage(BlockEntity entity) {
        this.x = entity.xCoord;
        this.y = entity.yCoord;
        this.z = entity.zCoord;
        this.compound = new NBTTagCompound();
        entity.saveTrackingSensitiveData(this.compound);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, BlockEntityMessage message, EntityPlayer player, MessageContext messageContext) {
        BlockEntity blockEntity = (BlockEntity) player.worldObj.getTileEntity(message.x, message.y, message.z);
        blockEntity.loadTrackingSensitiveData(message.compound);
    }

    @Override
    public void onServerReceived(MinecraftServer server, BlockEntityMessage message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.compound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        ByteBufUtils.writeTag(buf, this.compound);
    }
}
