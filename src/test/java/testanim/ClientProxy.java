package testanim;

import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * @author jglrxavpok
 */
public class ClientProxy extends TestProxy {

    @Override
    public void preInit() {
        // Register your render
        RenderingRegistry.registerEntityRenderingHandler(EntityTabulaAnimationTest.class, RenderEntityTest::new);
    }
}
