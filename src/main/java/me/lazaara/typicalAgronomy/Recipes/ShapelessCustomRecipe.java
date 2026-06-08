package me.lazaara.typicalAgronomy.Recipes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShapelessCustomRecipe extends CustomRecipe {

    private final List<ItemStack> ingredients;

    public ShapelessCustomRecipe(List<ItemStack> ingredients, ItemStack result, StationType stationType) {
        super(result, stationType);
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(ItemStack[] grid) {

        List<ItemStack> nonEmpty = Arrays.stream(grid)
                .filter(i -> i != null && i.getType() != Material.AIR)
                .toList();

        if (nonEmpty.size() != ingredients.size()) return false;

        List<ItemStack> remaining = new ArrayList<>(nonEmpty);

        for (ItemStack req : ingredients) {
            boolean found = false;
            for (int i = 0; i < remaining.size(); i++) {
                if (itemsMatch(req, remaining.get(i))) {
                    remaining.remove(i);
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }

        return true;

    }

    @Override
    public ItemStack[] getDisplayGrid() {
        ItemStack[] grid = new ItemStack[9];
        for (int i = 0; i < ingredients.size() && i < 9; i++) {
            grid[i] = ingredients.get(i);
        }
        return grid;
    }

    @Override
    public String getRecipeTypeName() {
        return "Shapeless";
    }

}
