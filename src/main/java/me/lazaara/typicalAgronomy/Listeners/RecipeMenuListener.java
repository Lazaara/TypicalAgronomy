package me.lazaara.typicalAgronomy.Listeners;

import me.lazaara.typicalAgronomy.Managers.RecipeManager;
import me.lazaara.typicalAgronomy.Items.ItemCategory;
import me.lazaara.typicalAgronomy.Recipes.CustomRecipe;
import me.lazaara.typicalAgronomy.Recipes.StationType;
import me.lazaara.typicalAgronomy.Managers.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RecipeMenuListener implements Listener {

    private static final String CATEGORY_TITLE = ChatColor.DARK_GREEN + "Recipe Browser";
    private static final String LIST_PREFIX    = ChatColor.DARK_GREEN + "Recipes: ";
    private static final String MOCK_TITLE     = ChatColor.DARK_GREEN + "Recipe View";

    private static final int BACK_SLOT    = 45;
    private static final int ARROW_SLOT   = 23;
    private static final int OUTPUT_SLOT  = 25;
    private static final int STATION_SLOT = 32;

    // Input slots ordered top-left to bottom-right (matches CraftingStationListener)
    private static final List<Integer> INPUT_SLOTS = List.of(10, 11, 12, 19, 20, 21, 28, 29, 30);

    private static final ItemCategory[] CATEGORY_SLOTS = new ItemCategory[54];

    // Tracks which category a player navigated from (for back button in mock view)
    private static final Map<UUID, ItemCategory> playerCategory = new HashMap<>();
    // Tracks which recipe a player is viewing (matched by result display name)
    private static final Map<UUID, CustomRecipe> playerRecipe = new HashMap<>();

    static {
        CATEGORY_SLOTS[20] = ItemCategory.SEEDS;
        CATEGORY_SLOTS[22] = ItemCategory.ESSENCES;
        CATEGORY_SLOTS[24] = ItemCategory.STATIONS;
    }

    public static void openCategoryMenu(Player player) {

        Inventory inv = player.getServer().createInventory(null, 54, CATEGORY_TITLE);
        ItemStack filler = ItemMenuListener.makeFiller();

        for (int i = 45; i < 54; i++) inv.setItem(i, filler);

        for (int i = 0; i < 45; i++) {
            ItemCategory cat = CATEGORY_SLOTS[i];
            if (cat != null) inv.setItem(i, makeCategoryIcon(cat));
        }

        player.openInventory(inv);

    }

    private static void openRecipeList(Player player, ItemCategory category) {

        playerCategory.put(player.getUniqueId(), category);

        String title = LIST_PREFIX + category.getDisplayName();
        Inventory inv = player.getServer().createInventory(null, 54, title);

        ItemStack filler = ItemMenuListener.makeFiller();
        for (int i = 45; i < 54; i++) inv.setItem(i, filler);
        inv.setItem(BACK_SLOT, ItemMenuListener.makeBackButton());

        List<CustomRecipe> recipes = RecipeManager.getByCategory(category);
        for (int i = 0; i < recipes.size() && i < 45; i++) {
            inv.setItem(i, recipes.get(i).getResult());
        }

        player.openInventory(inv);

    }

    private static void openMockView(Player player, CustomRecipe recipe, ItemCategory category) {

        playerRecipe.put(player.getUniqueId(), recipe);
        playerCategory.put(player.getUniqueId(), category);

        Inventory inv = player.getServer().createInventory(null, 54, MOCK_TITLE);

        ItemStack filler = ItemMenuListener.makeFiller();
        for (int i = 0; i < 54; i++) inv.setItem(i, filler);

        // Fill 3x3 input grid from recipe display
        ItemStack[] grid = recipe.getDisplayGrid();
        for (int i = 0; i < 9; i++) {
            if (grid[i] != null) inv.setItem(INPUT_SLOTS.get(i), grid[i].clone());
            else inv.setItem(INPUT_SLOTS.get(i), null);
        }

        // Arrow indicator
        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        assert arrowMeta != null;
        arrowMeta.setDisplayName(ChatColor.YELLOW + "➜");
        arrow.setItemMeta(arrowMeta);
        inv.setItem(ARROW_SLOT, arrow);

        // Output
        inv.setItem(OUTPUT_SLOT, recipe.getResult());

        // Station info (replaces the book slot)
        inv.setItem(STATION_SLOT, makeStationInfoItem(recipe));

        // Back button
        inv.setItem(BACK_SLOT, ItemMenuListener.makeBackButton());

        player.openInventory(inv);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) return;

        String title = event.getView().getTitle();
        boolean isCategoryMenu = CATEGORY_TITLE.equals(title);
        boolean isRecipeList   = title.startsWith(LIST_PREFIX);
        boolean isMockView     = MOCK_TITLE.equals(title);

        if (!isCategoryMenu && !isRecipeList && !isMockView) return;

        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot >= 54) return;

        if (isCategoryMenu) {
            ItemCategory cat = CATEGORY_SLOTS[slot];
            if (cat != null) openRecipeList(player, cat);
            return;
        }

        if (isRecipeList) {
            if (slot == BACK_SLOT) {
                openCategoryMenu(player);
                return;
            }
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR
                    || clicked.getType() == Material.GRAY_STAINED_GLASS_PANE) return;

            // Match clicked result to a recipe
            ItemCategory cat = playerCategory.get(player.getUniqueId());
            if (cat == null) return;
            for (CustomRecipe recipe : RecipeManager.getByCategory(cat)) {
                if (ItemManager.itemsDisplayMatch(recipe.getResult(), clicked)) {
                    openMockView(player, recipe, cat);
                    return;
                }
            }
            return;
        }

        // Mock view
        if (slot == BACK_SLOT) {
            ItemCategory cat = playerCategory.get(player.getUniqueId());
            if (cat != null) openRecipeList(player, cat);
            else openCategoryMenu(player);
        }

    }

    private static ItemStack makeStationInfoItem(CustomRecipe recipe) {

        StationType station = recipe.getStationType();
        ItemStack icon = new ItemStack(station.getIcon());
        ItemMeta meta = icon.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + station.getDisplayName());
        meta.setLore(List.of(
                ChatColor.GRAY + "Type: " + ChatColor.WHITE + recipe.getRecipeTypeName()
        ));
        icon.setItemMeta(meta);
        return icon;

    }

    private static ItemStack makeCategoryIcon(ItemCategory category) {

        ItemStack icon = new ItemStack(category.getIcon());
        ItemMeta meta = icon.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GREEN + category.getDisplayName());
        icon.setItemMeta(meta);
        return icon;

    }

}