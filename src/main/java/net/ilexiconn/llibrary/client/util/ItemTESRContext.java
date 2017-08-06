package net.ilexiconn.llibrary.client.util;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Handles context for Item TESRs, such as the ItemStack and viewed perspective
 *
 * @author gegy1000
 * @since 1.7.7
 */
public enum ItemTESRContext {
    INSTANCE;

    @Nullable
    private ItemStack currentStack = null;

    @Nonnull
    private ItemCameraTransforms.TransformType currentTransform = ItemCameraTransforms.TransformType.GROUND;

    public void provideStackContext(@Nonnull ItemStack stack) {
        this.currentStack = stack;
    }

    public void providePerspectiveContext(@Nonnull ItemCameraTransforms.TransformType transform) {
        this.currentTransform = transform;
    }

    /**
     * @return the stack currently being rendered
     */
    @Nullable
    public ItemStack getCurrentStack() {
        return this.currentStack;
    }

    /**
     * @return the current camera transform context
     */
    @Nonnull
    public ItemCameraTransforms.TransformType getCurrentTransform() {
        return this.currentTransform;
    }
}
