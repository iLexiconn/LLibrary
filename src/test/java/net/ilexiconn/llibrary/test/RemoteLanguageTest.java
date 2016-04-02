package net.ilexiconn.llibrary.test;

import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "RemoteLanguageTest")
public class RemoteLanguageTest {
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void breakBlock(BlockEvent.BreakEvent event) {
        System.out.println(I18n.translateToLocal("remote.break.name"));
    }
}
