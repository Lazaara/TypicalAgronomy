package me.lazaara.typicalAgronomy.Managers;

import me.lazaara.typicalAgronomy.Items.ItemCategory;
import me.lazaara.typicalAgronomy.Recipes.CustomRecipe;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RecipeManager {

    private static final List<CustomRecipe> recipes = new ArrayList<>();

    public static void registerRecipe(CustomRecipe recipe) {
        recipes.add(recipe);
    }

    // grid: 9-element array representing the 3x3 input in order (top-left to bottom-right)
    public static ItemStack match(ItemStack[] grid) {
        for (CustomRecipe recipe : recipes) {
            if (recipe.matches(grid)) return recipe.getResult();
        }
        return null;
    }

    public static CustomRecipe matchRecipe(ItemStack[] grid) {
        for (CustomRecipe recipe : recipes) {
            if (recipe.matches(grid)) return recipe;
        }
        return null;
    }

    public static List<CustomRecipe> getAll() {
        return recipes;
    }

    public static List<CustomRecipe> getByCategory(ItemCategory category) {
        return recipes.stream()
                .filter(r -> {
                    ItemCategory resultCategory = ItemManager.getCategoryForItem(r.getResult());
                    return resultCategory == category;
                })
                .toList();
    }

}