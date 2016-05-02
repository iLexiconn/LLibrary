package net.ilexiconn.llibrary.client.gui;

import net.ilexiconn.llibrary.client.gui.config.LLibraryConfigGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

@SideOnly(Side.CLIENT)
public class LLibraryGUIFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft mc) {

    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return LLibraryConfigGUI.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }
}
