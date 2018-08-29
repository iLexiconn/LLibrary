package testanim;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.ilexiconn.llibrary.client.model.tabula.animation.AnimationPlayerAnimator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.io.IOException;

public class RenderEntityTest extends Render<EntityAnimationTest> {

    public static final ResourceLocation BOAT_TEXTURE = new ResourceLocation("textures/entity/boat/boat_oak.png");

    private TabulaModel model;

    public RenderEntityTest(RenderManager renderManager) {
        super(renderManager);
        try {
            model = new TabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("assets/llibrary_animtest/models/testmodel.tbl"), new AnimationPlayerAnimator());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doRender(EntityAnimationTest entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        bindEntityTexture(entity);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y+0.5, z);
        GlStateManager.rotate(180f, 1f, 0f, 0f);
        float scale = 1f/16f;
        model.render(entity, 0f, 0f, entity.ticksExisted+partialTicks, 0f, 0f, scale);
        GlStateManager.popMatrix();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityAnimationTest entity) {
        return BOAT_TEXTURE;
    }
}
