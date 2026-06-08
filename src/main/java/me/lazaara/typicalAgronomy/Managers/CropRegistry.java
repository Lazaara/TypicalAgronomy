package me.lazaara.typicalAgronomy.Managers;

import me.lazaara.typicalAgronomy.Crops.CustomCrop;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CropRegistry {

    private static final List<CustomCrop> crops = new ArrayList<>();

    public static void registerCrop(CustomCrop crop) {
        crops.add(crop);
    }

    // Find a crop definition by matching the held item against its registered seed
    public static CustomCrop findBySeed(ItemStack item) {

        if (item == null) return null;
        ItemMeta heldMeta = item.getItemMeta();
        if (heldMeta == null || !heldMeta.hasDisplayName()) return null;

        for (CustomCrop crop : crops) {
            ItemStack seed = ItemManager.getItemStack(crop.getSeedItemID());
            if (seed == null) continue;
            ItemMeta seedMeta = seed.getItemMeta();
            if (seedMeta == null || !seedMeta.hasDisplayName()) continue;
            if (heldMeta.getDisplayName().equals(seedMeta.getDisplayName())
                    && item.getType() == seed.getType()) {
                return crop;
            }
        }

        return null;

    }

    // Find a crop definition by its seed item ID
    public static CustomCrop findByID(int seedItemID) {
        for (CustomCrop crop : crops) {
            if (crop.getSeedItemID() == seedItemID) return crop;
        }
        return null;
    }

}
