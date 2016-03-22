package net.ilexiconn.llibrary.client.model.tabula.baked;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.List;

/**
 * @author pau101
 * @since 1.0.0
 */
@SideOnly(Side.CLIENT)
public class BakedTabulaModel implements IFlexibleBakedModel, IPerspectiveAwareModel {
    private ImmutableList<BakedQuad> quads;
    private TextureAtlasSprite particle;
    private VertexFormat format;
    private ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;

    public BakedTabulaModel(ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, VertexFormat format, ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms) {
        this.quads = quads;
        this.particle = particle;
        this.format = format;
        this.transforms = transforms;
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing facing) {
        return ImmutableList.of();
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return quads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.particle;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
        return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, transforms, type);
    }

    @Override
    public VertexFormat getFormat() {
        return this.format;
    }
}
