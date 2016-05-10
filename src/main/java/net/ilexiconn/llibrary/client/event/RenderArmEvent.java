package net.ilexiconn.llibrary.client.event;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderArmEvent extends Event {
    private AbstractClientPlayer player;
    private RenderPlayer renderPlayer;
    private ModelPlayer model;

    public RenderArmEvent(AbstractClientPlayer player, RenderPlayer renderPlayer, ModelPlayer model) {
        this.player = player;
        this.renderPlayer = renderPlayer;
        this.model = model;
    }

    public static class Left extends RenderArmEvent {
        public Left(AbstractClientPlayer player, RenderPlayer renderPlayer, ModelPlayer model) {
            super(player, renderPlayer, model);
        }

        @Cancelable
        public static class Pre extends Left {
            public Pre(AbstractClientPlayer player, RenderPlayer renderPlayer, ModelPlayer model) {
                super(player, renderPlayer, model);
            }
        }

        public static class Post extends Left {
            public Post(AbstractClientPlayer player, RenderPlayer renderPlayer, ModelPlayer model) {
                super(player, renderPlayer, model);
            }
        }
    }

    public static class Right extends RenderArmEvent {
        public Right(AbstractClientPlayer player, RenderPlayer renderPlayer, ModelPlayer model) {
            super(player, renderPlayer, model);
        }

        @Cancelable
        public static class Pre extends Right {
            public Pre(AbstractClientPlayer player, RenderPlayer renderPlayer, ModelPlayer model) {
                super(player, renderPlayer, model);
            }
        }

        public static class Post extends Right {
            public Post(AbstractClientPlayer player, RenderPlayer renderPlayer, ModelPlayer model) {
                super(player, renderPlayer, model);
            }
        }
    }

    public AbstractClientPlayer getPlayer() {
        return this.player;
    }

    public RenderPlayer getRenderPlayer() {
        return this.renderPlayer;
    }

    public ModelPlayer getModel() {
        return this.model;
    }
}
