package net.ilexiconn.llibrary.client.event;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderArmEvent extends Event {
    public final AbstractClientPlayer player;
    public final RenderPlayer renderPlayer;

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
}
