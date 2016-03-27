package net.ilexiconn.llibrary.server.util;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.text.WordUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public class ModUtils {
    private static Map<String, String> resourceIDToNameMap = new HashMap<>();

    static {
        Map<String, ModContainer> map = Loader.instance().getIndexedModList();
        for (Map.Entry<String, ModContainer> modEntry : map.entrySet()) {
            String resourceID = modEntry.getKey().toLowerCase(Locale.ENGLISH);
            String name = modEntry.getValue().getName();
            resourceIDToNameMap.put(resourceID, name);
        }
    }

    /**
     * @param stack the item stack
     * @return the mod name of the specified item stack
     */
    public static String getModNameForStack(ItemStack stack) {
        return ModUtils.getModNameForItem(stack.getItem());
    }

    /**
     * @param block the block
     * @return the mod name of the specified block
     */
    public static String getModNameForBlock(Block block) {
        return ModUtils.getModNameForItem(Item.getItemFromBlock(block));
    }

    /**
     * @param item the item
     * @return the mod name of the specified item
     */
    public static String getModNameForItem(Item item) {
        String objectName = GameData.getItemRegistry().getNameForObject(item);
        String modID = objectName.split(":")[0];
        String resourceID = modID.toLowerCase(Locale.ENGLISH);
        String name = getNameForResourceID(resourceID);
        if (name == null) {
            name = WordUtils.capitalize(modID);
            resourceIDToNameMap.put(resourceID, name);
        }
        return name;
    }

    /**
     * @param resourceID the resource domain
     * @return the mod name
     */
    public static String getNameForResourceID(String resourceID) {
        return ModUtils.resourceIDToNameMap.get(resourceID);
    }
}
