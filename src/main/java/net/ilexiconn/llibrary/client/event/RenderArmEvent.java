package net.ilexiconn.llibrary.client.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;

@SideOnly(Side.CLIENT)
public class RenderArmEvent extends Event {
    private EntityPlayer player;
    private RenderPlayer renderPlayer;
    private ModelBiped model;

    RenderArmEvent(EntityPlayer player, RenderPlayer renderPlayer, ModelBiped model) {
        this.player = player;
        this.renderPlayer = renderPlayer;
        this.model = model;
    }

    @Cancelable
    public static class Pre extends RenderArmEvent {
        public Pre(EntityPlayer player, RenderPlayer renderPlayer, ModelBiped model) {
            super(player, renderPlayer, model);
        }
    }

    public static class Post extends RenderArmEvent {
        public Post(EntityPlayer player, RenderPlayer renderPlayer, ModelBiped model) {
            super(player, renderPlayer, model);
        }
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }

    public RenderPlayer getRenderPlayer() {
        return this.renderPlayer;
    }

    public ModelBiped getModel() {
        return this.model;
    }
}