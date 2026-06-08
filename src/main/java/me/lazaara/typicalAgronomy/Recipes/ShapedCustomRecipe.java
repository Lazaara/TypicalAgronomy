package me.lazaara.typicalAgronomy.Recipes;

import org.bukkit.inventory.ItemStack;

public class ShapedCustomRecipe extends CustomRecipe {

    // 9-element array representing a 3x3 grid (top-left to bottom-right), null = empty slot
    private final ItemStack[] pattern;

    public ShapedCustomRecipe(ItemStack[] pattern, ItemStack result, StationType stationType) {
        super(result, stationType);
        if (pattern.length != 9) throw new IllegalArgumentException("Shaped recipe pattern must have exactly 9 elements");
        this.pattern = pattern;
    }

    @Override
    public boolean matches(ItemStack[] grid) {
        for (int i = 0; i < 9; i++) {
            if (!itemsMatch(pattern[i], grid[i])) return false;
        }
        return true;
    }

    @Override
    public ItemStack[] getDisplayGrid() {
        return pattern.clone();
    }

    @Override
    public String getRecipeTypeName() {
        return "Shaped";
    }

}