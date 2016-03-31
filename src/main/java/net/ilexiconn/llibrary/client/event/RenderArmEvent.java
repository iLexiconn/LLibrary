package net.ilexiconn.llibrary.client.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;

@SideOnly(Side.CLIENT)
public class RenderArmEvent extends Event {
    public final EntityPlayer player;
    public final RenderPlayer renderPlayer;

    public RenderArmEvent(EntityPlayer player, RenderPlayer renderPlayer) {
        this.player = player;
        this.renderPlayer = renderPlayer;
    }

    @Cancelable
    public static class Pre extends RenderArmEvent {
        public Pre(EntityPlayer player, RenderPlayer renderPlayer) {
            super(player, renderPlayer);
        }
    }

    public static class Post extends RenderArmEvent {
        public Post(EntityPlayer player, RenderPlayer renderPlayer) {
            super(player, renderPlayer);
        }
    }
}
