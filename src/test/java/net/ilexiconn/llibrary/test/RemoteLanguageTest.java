package net.ilexiconn.llibrary.test;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

@Mod(modid = "RemoteLanguageTest")
public class RemoteLanguageTest {
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        System.out.println(StatCollector.translateToLocal("remote.break.name"));
    }
}
