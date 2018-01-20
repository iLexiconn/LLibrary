package net.ilexiconn.llibrary.server.core.api;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Handles calls from LLibrary Core to LLibrary
 */
public interface LLibraryCoreAPI {
    @SideOnly(Side.CLIENT)
    void addRemoteLocalizations(String language, Map<String, String> properties);

    @SideOnly(Side.CLIENT)
    void provideStackContext(@Nonnull ItemStack stack);

    @SideOnly(Side.CLIENT)
    void providePerspectiveContext(@Nonnull ItemCameraTransforms.TransformType transform);

    long getTickRate();

    class Fallback implements LLibraryCoreAPI {
        @Override
        public void addRemoteLocalizations(String language, Map<String, String> properties) {
        }

        @Override
        public void provideStackContext(@Nonnull ItemStack stack) {
        }

        @Override
        public void providePerspectiveContext(@Nonnull ItemCameraTransforms.TransformType transform) {
        }

        @Override
        public long getTickRate() {
            return 50;
        }
    }
}
