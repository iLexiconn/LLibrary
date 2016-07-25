package net.ilexiconn.llibrary.server.core.patcher;

import net.ilexiconn.llibrary.client.lang.LanguageHandler;
import net.ilexiconn.llibrary.server.asm.MappingHandler;
import net.ilexiconn.llibrary.server.asm.RuntimePatcher;
import net.ilexiconn.llibrary.server.util.EnumHandSide;
import net.ilexiconn.llibrary.server.world.TickRateHandler;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.Locale;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import org.objectweb.asm.tree.FieldInsnNode;

import java.util.List;
import java.util.Map;

public class LLibraryRuntimePatcher extends RuntimePatcher {
    @Override
    public void onInit() {
        patchClass(Locale.class)
            .patchMethod("loadLocaleDataFiles", IResourceManager.class, List.class, void.class)
                .insertBefore(_method("format", String.class, Object[].class, String.class), method -> {
                    method.field(GETSTATIC, LanguageHandler.class, "INSTANCE", LanguageHandler.class);
                    method.var(ALOAD, 4).var(ALOAD, 0);
                    method.field(GETFIELD, Locale.class, "field_135032_a", Map.class);
                    method.method(INVOKEVIRTUAL, LanguageHandler.class, "addRemoteLocalizations", String.class, Map.class, void.class);
                }).pop();

        patchClass(ModelBiped.class)
            .patchMethod("setRotationAngles", 6, float.class, Entity.class, void.class)
                .insertBefore(_return(), method -> {
                    method.var(ALOAD, 0).var(ALOAD, 7).var(FLOAD, 1, 6);
                    method.method(INVOKESTATIC, LLibraryHooks.class, "setRotationAngles", ModelBiped.class, Entity.class, 6, float.class, void.class);
                }).pop()
            .patchMethod("render", Entity.class, 6, float.class, void.class)
                .insertBefore(_return(), method -> {
                    method.var(ALOAD, 0, 1).var(FLOAD, 2, 7);
                    method.method(INVOKESTATIC, LLibraryHooks.class, "renderModel", ModelBiped.class, Entity.class, 6, float.class, void.class);
                }).pop()
            .patchMethod("<init>", float.class, float.class, int.class, int.class, void.class)
                .insertBefore(_return(), method -> {
                    method.var(ALOAD, 0);
                    method.method(INVOKESTATIC, LLibraryHooks.class, "constructModel", ModelBiped.class, void.class);
                }).pop();

        patchClass(RenderPlayer.class)
            .patchMethod("renderFirstPersonArm", EntityPlayer.class, void.class)
                .set(method -> {
                    method.var(ALOAD, 0, 1);
                    method.field(GETSTATIC, EnumHandSide.class, "RIGHT", EnumHandSide.class);
                    method.method(INVOKESTATIC, LLibraryHooks.class, "renderArm", RenderPlayer.class, EntityPlayer.class, EnumHandSide.class, void.class);
                    method.node(RETURN);
                }).pop()
            .patchMethod("<init>", void.class)
                .insertBefore(_node(PUTFIELD), method -> {
                    if (((FieldInsnNode) method.getInsnNode()).name.equals(MappingHandler.INSTANCE.getFieldMapping(RenderPlayer.class, "modelBipedMain"))) {
                        method.var(ALOAD, 0);
                        method.method(INVOKESTATIC, LLibraryHooks.class, "assignModel", ModelBiped.class, RenderPlayer.class, ModelBiped.class);
                    }
                }).pop();

        patchClass(MinecraftServer.class)
            .patchMethod("run", void.class)
                .replace(_ldc(50L), method -> {
                    method.field(GETSTATIC, TickRateHandler.class, "INSTANCE", TickRateHandler.class);
                    method.method(INVOKEVIRTUAL, TickRateHandler.class, "getTickRate", long.class);
                }).pop();
    }
}
