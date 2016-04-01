package net.ilexiconn.llibrary.test;

import net.ilexiconn.llibrary.client.event.PlayerModelEvent;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
        this.head.render(event.scale);
    }

    @SubscribeEvent
    public void onSetRotationAngles(PlayerModelEvent.SetRotationAngles event) {
        event.getModel().bipedRightArm.rotateAngleX = (float) Math.toRadians(180.0F);
        event.getModel().bipedRightArmwear.rotateAngleX = (float) Math.toRadians(180.0F);
    }
}
