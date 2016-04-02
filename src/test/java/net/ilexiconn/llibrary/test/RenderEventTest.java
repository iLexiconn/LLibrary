package net.ilexiconn.llibrary.test;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.ilexiconn.llibrary.client.event.PlayerModelEvent;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "RenderEventTest")
public class RenderEventTest {
    private ModelRenderer head;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerModelConstruct(PlayerModelEvent.Construct event) {
        this.head = new ModelRenderer(event.getModel(), 0, 0);
        this.head.addBox(-4.0F, -16.0F, -4.0F, 8, 8, 8);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    @SubscribeEvent
    public void onPlayerModelRender(PlayerModelEvent.Render event) {
        this.head.render(event.getScale());
    }

    @SubscribeEvent
    public void onSetRotationAngles(PlayerModelEvent.SetRotationAngles event) {
        event.getModel().bipedRightArm.rotateAngleX = (float) Math.toRadians(180.0F);
    }
}
