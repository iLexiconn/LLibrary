package testanim;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.ilexiconn.llibrary.client.model.tabula.animation.AnimationPlayerAnimator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * @author jglrxavpok
 */
public class RenderEntityTest extends Render<EntityTabulaAnimationTest> {

    public static final ResourceLocation BOAT_TEXTURE = new ResourceLocation("textures/entity/boat/boat_oak.png");

    private TabulaModel model;

    public RenderEntityTest(RenderManager renderManager) {
        super(renderManager);
        try {
            model = new TabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("assets/llibrary_animtest/models/testmodel.tbl"),
                    new AnimationPlayerAnimator()); // the animation player animator is the only thing you have to add to animate your model
        } catch (Exception e) {
            e.printStackTrace(); // don't do that, handle your errors properly. this is just a simple example
        }
    }

    @Override
    public void doRender(EntityTabulaAnimationTest entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ResourceLocation getEntityTexture(EntityTabulaAnimationTest entity) {
        return BOAT_TEXTURE;
    }
}
