package net.ilexiconn.llibrary.server.network;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class AnimationMessage extends AbstractMessage<AnimationMessage> {
    private int entityID;
    private int index;

    public AnimationMessage() {

    }

    public AnimationMessage(int entityID, int index) {
        this.entityID = entityID;
        this.index = index;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, AnimationMessage message, EntityPlayer player, MessageContext messageContext) {
        IAnimatedEntity entity = (IAnimatedEntity) player.worldObj.getEntityByID(message.entityID);
        if (entity != null) {
            if (message.index == -1) {
                entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
            } else {
                entity.setAnimation(entity.getAnimations()[message.index]);
            }
            entity.setAnimationTick(0);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, AnimationMessage message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.entityID = byteBuf.readInt();
        this.index = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(this.entityID);
        byteBuf.writeInt(this.index);
    }
}
