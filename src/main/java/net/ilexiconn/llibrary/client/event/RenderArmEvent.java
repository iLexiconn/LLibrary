package net.ilexiconn.llibrary.client.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;

@SideOnly(Side.CLIENT)
public class RenderArmEvent extends Event {
    protected AbstractClientPlayer player;
    protected RenderPlayer renderPlayer;

    public RenderArmEvent(AbstractClientPlayer player, RenderPlayer renderPlayer) {
        this.player = player;
        this.renderPlayer = renderPlayer;
    }

    public static class Left extends RenderArmEvent {
        public Left(AbstractClientPlayer player, RenderPlayer renderPlayer) {
            super(player, renderPlayer);
        }

        @Cancelable
        public static class Pre extends Left {
            public Pre(AbstractClientPlayer player, RenderPlayer renderPlayer) {
                super(player, renderPlayer);
            }
        }

        public static class Post extends Left {
            public Post(AbstractClientPlayer player, RenderPlayer renderPlayer) {
                super(player, renderPlayer);
            }
        }
    }

    public static class Right extends RenderArmEvent {
        public Right(AbstractClientPlayer player, RenderPlayer renderPlayer) {
            super(player, renderPlayer);
        }

        @Cancelable
        public static class Pre extends Right {
            public Pre(AbstractClientPlayer player, RenderPlayer renderPlayer) {
                super(player, renderPlayer);
            }
        }

        public static class Post extends Right {
            public Post(AbstractClientPlayer player, RenderPlayer renderPlayer) {
                super(player, renderPlayer);
            }
        }
    }

    public AbstractClientPlayer getPlayer() {
        return this.player;
    }

    public RenderPlayer getRenderPlayer() {
        return this.renderPlayer;
    }
}
