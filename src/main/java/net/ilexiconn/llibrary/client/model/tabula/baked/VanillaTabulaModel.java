package net.ilexiconn.llibrary.client.model.tabula.baked;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaCubeContainer;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.ilexiconn.llibrary.client.util.Matrix;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Point2f;
import javax.vecmath.Point2i;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.util.Collection;
import java.util.List;

/**
 * @author pau101
 * @since 1.0.0
 */
@SideOnly(Side.CLIENT)
public class VanillaTabulaModel implements IModel {
    private TabulaModelContainer model;
    private ResourceLocation particle;
    private ImmutableList<ResourceLocation> textures;
    private ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;

    public VanillaTabulaModel(TabulaModelContainer model, ResourceLocation particle, ImmutableList<ResourceLocation> textures, ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms) {
        this.model = model;
        this.particle = particle;
        this.textures = textures;
        this.transforms = transforms;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return ImmutableList.of();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return this.textures;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        TextureAtlasSprite sprite = bakedTextureGetter.apply(this.textures.isEmpty() ? new ResourceLocation("missingno") : this.textures.get(0));
        TextureAtlasSprite particleSprite = this.particle == null ? sprite : bakedTextureGetter.apply(this.particle);
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        Matrix matrix = new Matrix();
        TRSRTransformation transformation = state.apply(Optional.absent()).or(TRSRTransformation.identity());
        matrix.multiply(transformation.getMatrix());
        matrix.translate(0.5F, 1.5F, 0.5F);
        matrix.scale(-0.0625F, -0.0625F, 0.0625F);
        this.build(matrix, builder, format, this.model.getCubes(), sprite);
        ImmutableList<BakedQuad> leQuads = builder.build();
        return new BakedTabulaModel(leQuads, particleSprite, this.transforms);
    }

    private void build(Matrix mat, Builder<BakedQuad> builder, VertexFormat format, List<TabulaCubeContainer> cubeContainerList, TextureAtlasSprite sprite) {
        for (TabulaCubeContainer cube : cubeContainerList) {
            int[] dimensions = cube.getDimensions();
            double[] position = cube.getPosition();
            double[] offset = cube.getOffset();
            double[] rotation = cube.getRotation();
            double[] glScale = cube.getScale();
            int[] txOffset = cube.getTextureOffset();
            mat.push();
            mat.translate(position[0], position[1], position[2]);
            if (glScale[0] != 1 || glScale[1] != 1 || glScale[2] != 1) {
                mat.scale(glScale[0], glScale[1], glScale[2]);
            }
            if (rotation[2] != 0) {
                mat.rotate(rotation[2], 0, 0, 1);
            }
            if (rotation[1] != 0) {
                mat.rotate(rotation[1], 0, 1, 0);
            }
            if (rotation[0] != 0) {
                mat.rotate(rotation[0], 1, 0, 0);
            }
            float x = (float) offset[0], y = (float) offset[1], z = (float) offset[2];
            float s = (float) cube.getMCScale();
            int w = dimensions[0], h = dimensions[1], d = dimensions[2];
            float x0 = (x - s);
            float y0 = (y - s);
            float z0 = (z - s);
            float x1 = (x + s + w);
            float y1 = (y + s + h);
            float z1 = (z + s + d);
            boolean isTxMirror = cube.isTextureMirrorEnabled();
            if (isTxMirror) {
                float x1_ = x1;
                x1 = x0;
                x0 = x1_;
            }
            Point3f vertex000 = new Point3f(x0, y0, z0);
            Point3f vertex100 = new Point3f(x1, y0, z0);
            Point3f vertex110 = new Point3f(x1, y1, z0);
            Point3f vertex010 = new Point3f(x0, y1, z0);
            Point3f vertex001 = new Point3f(x0, y0, z1);
            Point3f vertex101 = new Point3f(x1, y0, z1);
            Point3f vertex111 = new Point3f(x1, y1, z1);
            Point3f vertex011 = new Point3f(x0, y1, z1);
            mat.transform(vertex000);
            mat.transform(vertex100);
            mat.transform(vertex110);
            mat.transform(vertex010);
            mat.transform(vertex001);
            mat.transform(vertex101);
            mat.transform(vertex111);
            mat.transform(vertex011);
            int u = txOffset[0], v = txOffset[1];
            Point2i rightMinUV = new Point2i(u + d + w, v + d);
            Point2i rightMaxUV = new Point2i(u + d + w + d, v + d + h);
            Point2i leftMinUV = new Point2i(u, v + d);
            Point2i leftMaxUV = new Point2i(u + d, v + d + h);
            Point2i topMinUV = new Point2i(u + d, v);
            Point2i topMaxUV = new Point2i(u + d + w + w, v);
            Point2i frontMinUV = new Point2i(u + d, v + d);
            Point2i frontMaxUV = new Point2i(u + d + w, v + d + h);
            Point2i backMinUV = new Point2i(u + d + w + d, v + d);
            Point2i backMaxUV = new Point2i(u + d + w + d + w, v + d + h);
            this.buildQuad(builder, format, isTxMirror, vertex101, vertex100, vertex110, vertex111, rightMinUV, rightMaxUV, sprite);
            this.buildQuad(builder, format, isTxMirror, vertex000, vertex001, vertex011, vertex010, leftMinUV, leftMaxUV, sprite);
            this.buildQuad(builder, format, isTxMirror, vertex101, vertex001, vertex000, vertex100, topMinUV, rightMinUV, sprite);
            this.buildQuad(builder, format, isTxMirror, vertex110, vertex010, vertex011, vertex111, rightMinUV, topMaxUV, sprite);
            this.buildQuad(builder, format, isTxMirror, vertex100, vertex000, vertex010, vertex110, frontMinUV, frontMaxUV, sprite);
            this.buildQuad(builder, format, isTxMirror, vertex001, vertex101, vertex111, vertex011, backMinUV, backMaxUV, sprite);
            this.build(mat, builder, format, cube.getChildren(), sprite);
            mat.pop();
        }
    }

    private void buildQuad(ImmutableList.Builder<BakedQuad> builder, VertexFormat format, boolean isTxMirror, Point3f vert0, Point3f vert1, Point3f vert2, Point3f vert3, Point2i minUV, Point2i maxUV, TextureAtlasSprite sprite) {
        Point3f[] vertices = {vert0, vert1, vert2, vert3};
        if (this.isQuadOneDimensional(vertices)) {
            return;
        }
        Point2i[] uvs = {new Point2i(maxUV.x, minUV.y), new Point2i(minUV.x, minUV.y), new Point2i(minUV.x, maxUV.y), new Point2i(maxUV.x, maxUV.y)};
        if (isTxMirror) {
            Point3f[] verticesMirrored = new Point3f[vertices.length];
            Point2i[] uvsMirrored = new Point2i[vertices.length];
            for (int i = 0, j = vertices.length - 1; i < vertices.length; i++, j--) {
                verticesMirrored[i] = vertices[j];
                uvsMirrored[i] = uvs[j];
            }
            vertices = verticesMirrored;
            uvs = uvsMirrored;
        }
        Vector3f v01 = new Vector3f(), v21 = new Vector3f(), normal = new Vector3f();
        v01.sub(vertices[0], vertices[1]);
        v21.sub(vertices[2], vertices[1]);
        normal.cross(v21, v01);
        normal.normalize();
        UnpackedBakedQuad.Builder quadBuilder = new UnpackedBakedQuad.Builder(format);
        quadBuilder.setQuadOrientation(EnumFacing.getFacingFromVector(normal.x, normal.y, normal.z));
        float width = this.model.getTextureWidth();
        float height = this.model.getTextureHeight();
        for (int i = 0; i < vertices.length; i++) {
            Point2i uvi = uvs[i];
            Point2f uv = new Point2f(sprite.getInterpolatedU(uvi.x / width * 16), sprite.getInterpolatedV(uvi.y / height * 16));
            this.putVertexData(quadBuilder, format, vertices[i], normal, uv);
        }
        builder.add(quadBuilder.build());
    }

    private boolean isQuadOneDimensional(Point3f[] vertices) {
        for (int i = 0; i < vertices.length; i++) {
            Point3f vertex = vertices[i];
            for (int n = i + 1; n < vertices.length; n++) {
                float epsilon = 1e-4F;
                if (vertex.epsilonEquals(vertices[n], epsilon)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void putVertexData(UnpackedBakedQuad.Builder builder, VertexFormat format, Point3f vert, Vector3f normal, Point2f uv) {
        for (int e = 0; e < format.getElementCount(); e++) {
            switch (format.getElement(e).getUsage()) {
                case POSITION:
                    builder.put(e, vert.x, vert.y, vert.z);
                    break;
                case COLOR:
                    builder.put(e, 1, 1, 1, 1);
                    break;
                case UV:
                    builder.put(e, uv.x, uv.y, 0, 1);
                    break;
                case NORMAL:
                    builder.put(e, normal.x, normal.y, normal.z);
                    break;
                default:
                    builder.put(e);
            }
        }
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }
}