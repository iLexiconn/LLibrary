package testanim;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

/**
 * @author jglrxavpok
 */
@Mod(modid = "llibrary_animtest", name = "LLibrary Animation Test")
@Mod.EventBusSubscriber
public class ModTestAnimation {

    @SidedProxy(clientSide = "testanim.ClientProxy", serverSide = "testanim.TestProxy")
    private static TestProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        EntityEntry entry = EntityEntryBuilder.create()
                .entity(EntityTabulaAnimationTest.class)
                .name("entity_test")
                .egg(0, 0xFFFFFF)
                .factory(EntityTabulaAnimationTest::new)
                .id(new ResourceLocation("llibrary_animtest", "entity_test"), 0)
                .tracker(64, 1, false)
                .build();
        event.getRegistry().register(entry);
    }
}
