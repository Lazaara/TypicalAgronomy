package me.lazaara.typicalAgronomy.Recipes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class CustomRecipe {

    private final ItemStack result;
    private final StationType stationType;

    public CustomRecipe(ItemStack result, StationType stationType) {
        this.result = result;
        this.stationType = stationType;
    }

    public abstract boolean matches(ItemStack[] grid);

    public abstract ItemStack[] getDisplayGrid();

    public abstract String getRecipeTypeName();

    public ItemStack getResult() {
        return result.clone();
    }

    public StationType getStationType() {
        return stationType;
    }

    protected static boolean itemsMatch(ItemStack a, ItemStack b) {

        boolean aEmpty = a == null || a.getType() == Material.AIR;
        boolean bEmpty = b == null || b.getType() == Material.AIR;
        if (aEmpty && bEmpty) return true;
        if (aEmpty || bEmpty) return false;
        if (a.getType() != b.getType()) return false;

        ItemMeta metaA = a.getItemMeta();
        ItemMeta metaB = b.getItemMeta();

        if (metaA != null && metaA.hasDisplayName()) {
            if (metaB == null || !metaB.hasDisplayName()) return false;
            return metaA.getDisplayName().equals(metaB.getDisplayName());
        }

        return true;

    }

}