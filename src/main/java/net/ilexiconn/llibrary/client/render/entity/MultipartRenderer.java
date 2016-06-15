package net.ilexiconn.llibrary.client.render.entity;

import net.ilexiconn.llibrary.server.entity.multipart.IMultipartEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class MultipartRenderer<T extends EntityLiving & IMultipartEntity> extends RenderLiving<T> {
    public MultipartRenderer(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
        super(renderManager, modelBase, shadowSize);
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (this.renderManager.isDebugBoundingBox()) {
            GlStateManager.depthMask(false);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            for (Entity part : entity.getParts()) {
                AxisAlignedBB boundingBox = part.getEntityBoundingBox();
                AxisAlignedBB alignedBox = new AxisAlignedBB(boundingBox.minX - part.posX + x, boundingBox.minY - part.posY + y, boundingBox.minZ - part.posZ + z, boundingBox.maxX - part.posX + x, boundingBox.maxY - part.posY + y, boundingBox.maxZ - part.posZ + z);
                RenderGlobal.drawOutlinedBoundingBox(alignedBox, 255, 255, 255, 255);
            }
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
