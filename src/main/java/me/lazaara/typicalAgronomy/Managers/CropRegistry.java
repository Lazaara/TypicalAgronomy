package me.lazaara.typicalAgronomy.Managers;

import me.lazaara.typicalAgronomy.Crops.CustomCrop;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class CropRegistry {

    private static final Map<String, CustomCrop> crops = new HashMap<>();

    public static void registerCrop(CustomCrop crop) {
        crops.put(crop.getCropKey(), crop);
    }

    // Find a crop definition by matching the held item against its registered seed
    public static CustomCrop findBySeed(ItemStack item) {

        if (item == null) return null;
        ItemMeta heldMeta = item.getItemMeta();
        if (heldMeta == null || !heldMeta.hasDisplayName()) return null;

        for (CustomCrop crop : crops.values()) {
            ItemStack seed = crop.getSeedItem();
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

    // Find a crop definition by its string key
    public static CustomCrop findByKey(String cropKey) {
        return crops.get(cropKey);
    }

}
