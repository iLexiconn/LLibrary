package net.ilexiconn.llibrary.server.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PropertiesMessage extends AbstractMessage<PropertiesMessage> {
    private String propertyID;
    private NBTTagCompound compound;
    private int entityID;

    public PropertiesMessage() {

    }

    public PropertiesMessage(EntityProperties<?> properties, Entity entity) {
        this.propertyID = properties.getID();
        NBTTagCompound compound = new NBTTagCompound();
        properties.saveTrackingSensitiveData(compound);
        this.compound = compound;
        this.entityID = entity.getEntityId();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, PropertiesMessage message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.worldObj.getEntityByID(message.entityID);
        if (entity != null) {
            IExtendedEntityProperties extendedProperties = entity.getExtendedProperties(message.propertyID);
            if (extendedProperties instanceof EntityProperties) {
                EntityProperties<?> properties = (EntityProperties) extendedProperties;
                properties.loadTrackingSensitiveData(message.compound);
                properties.onSync();
            }
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, PropertiesMessage message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.propertyID = ByteBufUtils.readUTF8String(buf);
        this.compound = ByteBufUtils.readTag(buf);
        this.entityID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.propertyID);
        ByteBufUtils.writeTag(buf, this.compound);
        buf.writeInt(this.entityID);
    }
}
