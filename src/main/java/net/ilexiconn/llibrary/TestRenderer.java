package net.ilexiconn.llibrary;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TestRenderer extends RenderLiving<TestEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("missingno");

    public TestRenderer(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
        super(renderManager, modelBase, shadowSize);
    }

    @Override
    protected ResourceLocation getEntityTexture(TestEntity entity) {
        return TestRenderer.TEXTURE;
    }

    public static class Factory implements IRenderFactory<TestEntity> {
        @Override
        public Render<? super TestEntity> createRenderFor(RenderManager manager) {
            return new TestRenderer(manager, new ModelCreeper(), 0.5F);
        }
    }
}
