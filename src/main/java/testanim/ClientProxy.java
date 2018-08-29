package testanim;

import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends TestProxy {

    @Override
    public void preInit() {
        RenderingRegistry.registerEntityRenderingHandler(EntityAnimationTest.class, RenderEntityTest::new);
    }
}
