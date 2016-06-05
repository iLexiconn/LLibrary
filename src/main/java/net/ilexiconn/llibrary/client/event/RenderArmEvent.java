package net.ilexiconn.llibrary.client.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.server.util.EnumHandSide;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;

@SideOnly(Side.CLIENT)
public class RenderArmEvent extends Event {
    private AbstractClientPlayer player;
    private RenderPlayer renderPlayer;
    private ModelBiped model;
    private EnumHandSide side;

    RenderArmEvent(AbstractClientPlayer player, RenderPlayer renderPlayer, ModelBiped model, EnumHandSide side) {
        this.player = player;
        this.renderPlayer = renderPlayer;
        this.model = model;
        this.side = side;
    }

    public AbstractClientPlayer getPlayer() {
        return this.player;
    }

    public RenderPlayer getRenderPlayer() {
        return this.renderPlayer;
    }

    public ModelBiped getModel() {
        return this.model;
    }

    public EnumHandSide getSide() {
        return side;
    }

    @Cancelable
    public static class Pre extends RenderArmEvent {
        public Pre(AbstractClientPlayer player, RenderPlayer renderPlayer, ModelBiped model, EnumHandSide side) {
            super(player, renderPlayer, model, side);
        }
    }

    public static class Post extends RenderArmEvent {
        public Post(AbstractClientPlayer player, RenderPlayer renderPlayer, ModelBiped model, EnumHandSide side) {
            super(player, renderPlayer, model, side);
        }
    }

    @Deprecated
    public static class Left extends RenderArmEvent {
        Left(AbstractClientPlayer player, RenderPlayer renderPlayer, ModelBiped model) {
            super(player, renderPlayer, model, EnumHandSide.LEFT);
        }

        @Deprecated
        @Cancelable
        public static class Pre extends Left {
            public Pre(AbstractClientPlayer player, RenderPlayer renderPlayer, ModelBiped model) {
                super(player, renderPlayer, model);
            }
        }

        @Deprecated
        public static class Post extends Left {
            public Post(AbstractClientPlayer player, RenderPlayer renderPlayer, ModelBiped model) {
                super(player, renderPlayer, model);
            }
        }
    }

    @Deprecated
    public static class Right extends RenderArmEvent {
        Right(AbstractClientPlayer player, RenderPlayer renderPlayer, ModelBiped model) {
            super(player, renderPlayer, model, EnumHandSide.RIGHT);
        }

        @Deprecated
        @Cancelable
        public static class Pre extends Right {
            public Pre(AbstractClientPlayer player, RenderPlayer renderPlayer, ModelBiped model) {
                super(player, renderPlayer, model);
            }
        }

        @Deprecated
        public static class Post extends Right {
            public Post(AbstractClientPlayer player, RenderPlayer renderPlayer, ModelBiped model) {
                super(player, renderPlayer, model);
            }
        }
    }
}
