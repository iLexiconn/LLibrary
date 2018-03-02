package net.ilexiconn.llibrary.server.core.patcher;

import net.ilexiconn.llibrary.server.asm.InsnPredicate;
import net.ilexiconn.llibrary.server.asm.RuntimePatcher;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.Locale;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.ForgeHooksClient;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.List;
import java.util.Map;

public class LLibraryRuntimePatcher extends RuntimePatcher {
    @Override
    public void onInit() {
        this.patchClass(Locale.class)
                .patchMethod("loadLocaleDataFiles", IResourceManager.class, List.class, void.class)
                .apply(Patch.BEFORE, new InsnPredicate.Method(String.class, "format", String.class, Object[].class, String.class), method -> {
                    method.var(ALOAD, 4).var(ALOAD, 0);
                    method.method(INVOKESTATIC, LLibraryHooks.class, "addRemoteLocalizations", String.class, Map.class, void.class);
                }).pop();

        this.patchClass(ModelBiped.class)
                .patchMethod("setRotationAngles", 6, float.class, Entity.class, void.class)
                .apply(Patch.BEFORE, new InsnPredicate.Op().opcode(InsnPredicate.RETURNING), method -> {
                    method.var(ALOAD, 0).var(ALOAD, 7).var(FLOAD, 1, 6);
                    method.method(INVOKESTATIC, LLibraryHooks.class, "setRotationAngles", ModelBiped.class, Entity.class, 6, float.class, void.class);
                })
                .pop()
                .patchMethod("render", Entity.class, 6, float.class, void.class)
                .apply(Patch.BEFORE, new InsnPredicate.Op().opcode(InsnPredicate.RETURNING), method -> {
                    method.var(ALOAD, 0, 1).var(FLOAD, 2, 7);
                    method.method(INVOKESTATIC, LLibraryHooks.class, "renderModel", ModelBiped.class, Entity.class, 6, float.class, void.class);
                })
                .pop()
                .patchMethod("<init>", float.class, boolean.class, void.class)
                .apply(Patch.BEFORE, new InsnPredicate.Op().opcode(InsnPredicate.RETURNING), method -> {
                    method.var(ALOAD, 0);
                    method.method(INVOKESTATIC, LLibraryHooks.class, "constructModel", ModelBiped.class, void.class);
                }).pop();

        this.patchClass(RenderPlayer.class)
                .patchMethod("renderLeftArm", AbstractClientPlayer.class, void.class)
                .apply(Patch.AFTER, new InsnPredicate.Method(ModelPlayer.class, "setRotationAngles", 6, float.class, Entity.class, void.class), method -> {
                    method.var(ALOAD, 0, 1);
                    method.field(GETSTATIC, EnumHandSide.class, "LEFT", EnumHandSide.class);
                    method.method(INVOKESTATIC, LLibraryHooks.class, "renderArmPre", RenderPlayer.class, AbstractClientPlayer.class, EnumHandSide.class, void.class);
                })
                .apply(Patch.AFTER, new InsnPredicate.Method(GlStateManager.class, "disableBlend", void.class), method -> {
                    method.var(ALOAD, 0, 1);
                    method.field(GETSTATIC, EnumHandSide.class, "LEFT", EnumHandSide.class);
                    method.method(INVOKESTATIC, LLibraryHooks.class, "renderArmPost", RenderPlayer.class, AbstractClientPlayer.class, EnumHandSide.class, void.class);
                }).pop()
                .patchMethod("renderRightArm", AbstractClientPlayer.class, void.class)
                .apply(Patch.AFTER, new InsnPredicate.Method(ModelPlayer.class, "setRotationAngles", 6, float.class, Entity.class, void.class), method -> {
                    method.var(ALOAD, 0, 1);
                    method.field(GETSTATIC, EnumHandSide.class, "RIGHT", EnumHandSide.class);
                    method.method(INVOKESTATIC, LLibraryHooks.class, "renderArmPre", RenderPlayer.class, AbstractClientPlayer.class, EnumHandSide.class, void.class);
                })
                .apply(Patch.AFTER, new InsnPredicate.Method(GlStateManager.class, "disableBlend", void.class), method -> {
                    method.var(ALOAD, 0, 1);
                    method.field(GETSTATIC, EnumHandSide.class, "RIGHT", EnumHandSide.class);
                    method.method(INVOKESTATIC, LLibraryHooks.class, "renderArmPost", RenderPlayer.class, AbstractClientPlayer.class, EnumHandSide.class, void.class);
                }).pop()
                .patchMethod("<init>", RenderManager.class, boolean.class, void.class)
                .apply(Patch.BEFORE, new InsnPredicate.Op().opcode(InsnPredicate.RETURNING), method -> {
                    method.var(ALOAD, 0).var(ALOAD, 0).var(ALOAD, 0);
                    method.method(INVOKEVIRTUAL, RenderPlayer.class, "getMainModel", ModelPlayer.class);
                    method.var(ALOAD, 0);
                    method.field(GETFIELD, RenderPlayer.class, "smallArms", boolean.class);
                    method.method(INVOKESTATIC, LLibraryHooks.class, "assignModel", RenderPlayer.class, ModelBiped.class, boolean.class, ModelBiped.class);
                    method.field(PUTFIELD, RenderLivingBase.class, "mainModel", ModelBase.class);
                }).pop();

        this.patchClass(MinecraftServer.class)
                .patchMethod("run", void.class)
                .apply(Patch.REPLACE_NODE, new InsnPredicate.Ldc().cst(50L), method -> {
                    method.method(INVOKESTATIC, LLibraryHooks.class, "getTickRate", long.class);
                }).pop();

        this.patchClass(TileEntityItemStackRenderer.class)
                .patchMethod("renderByItem", ItemStack.class, void.class)
                .apply(Patch.BEFORE, data -> data.node.getPrevious() == null, method -> {
                    method.var(ALOAD, 1);
                    method.method(INVOKESTATIC, LLibraryHooks.class, "provideStackContext", ItemStack.class, void.class);
                }).pop();

        this.patchClass(ForgeHooksClient.class)
                .patchMethod("handleCameraTransforms", IBakedModel.class, ItemCameraTransforms.TransformType.class, boolean.class, IBakedModel.class)
                .apply(Patch.BEFORE, data -> data.node.getPrevious() == null, method -> {
                    method.var(ALOAD, 1);
                    method.method(INVOKESTATIC, LLibraryHooks.class, "providePerspectiveContext", ItemCameraTransforms.TransformType.class, void.class);
                }).pop();

        this.patchClass(EntityRenderer.class)
                .patchMethod("orientCamera", float.class, void.class)
                .apply(Patch.REPLACE_NODE, new InsnPredicate.Ldc().cst(4.0F), method -> {
                    method.var(ALOAD, 2);
                    method.var(FLOAD, 1);
                    method.method(INVOKESTATIC, LLibraryHooks.class, "getViewDistance", Entity.class, float.class, float.class);
                })
                .apply(Patch.AFTER, data -> data.node.getOpcode() == ASTORE && ((VarInsnNode) data.node).var == 2, method -> {
                    method.var(ALOAD, 0);
                    method.field(GETSTATIC, LLibraryHooks.class, "prevRenderViewDistance", float.class);
                    method.field(PUTFIELD, EntityRenderer.class, "thirdPersonDistancePrev", float.class);
                }).pop();

        this.patchClass(RenderLivingBase.class)
                .patchMethod("applyRotations", EntityLivingBase.class, float.class, float.class, float.class, void.class)
                .apply(Patch.AFTER, data -> data.node.getPrevious() == null, method -> method
                        .var(ALOAD, 0)
                        .var(ALOAD, 1)
                        .var(FLOAD, 4)
                        .method(INVOKESTATIC, LLibraryHooks.class, "applyRotationsPre", RenderLivingBase.class, EntityLivingBase.class, float.class, void.class))
                .apply(Patch.BEFORE, data -> data.node.getOpcode() == RETURN, method -> method
                        .var(ALOAD, 0)
                        .var(ALOAD, 1)
                        .var(FLOAD, 4)
                        .method(INVOKESTATIC, LLibraryHooks.class, "applyRotationsPost", RenderLivingBase.class, EntityLivingBase.class, float.class, void.class));
    }
}
