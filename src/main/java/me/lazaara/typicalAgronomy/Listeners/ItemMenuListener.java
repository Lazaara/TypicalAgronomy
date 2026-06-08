package me.lazaara.typicalAgronomy.Listeners;

import me.lazaara.typicalAgronomy.Managers.ItemManager;
import me.lazaara.typicalAgronomy.Items.ItemCategory;
import me.lazaara.typicalAgronomy.Items.RegisteredItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemMenuListener implements Listener {

    private static final String CATEGORY_TITLE = ChatColor.DARK_GREEN + "Item Categories";
    private static final String LIST_PREFIX    = ChatColor.DARK_GREEN + "Items: ";
    private static final int BACK_SLOT = 45;

    // Category icon slots in the 54-slot menu (centered in rows 1-3)
    private static final ItemCategory[] CATEGORY_SLOTS = new ItemCategory[54];

    static {
        CATEGORY_SLOTS[19] = ItemCategory.SEEDS;
        CATEGORY_SLOTS[21] = ItemCategory.ESSENCES;
        CATEGORY_SLOTS[23] = ItemCategory.STATIONS;
        CATEGORY_SLOTS[25] = ItemCategory.MACHINES;
    }

    public static void openCategoryMenu(Player player) {

        Inventory inv = player.getServer().createInventory(null, 54, CATEGORY_TITLE);
        ItemStack filler = makeFiller();

        for (int i = 45; i < 54; i++) inv.setItem(i, filler);

        for (int i = 0; i < 45; i++) {
            ItemCategory cat = CATEGORY_SLOTS[i];
            if (cat != null) inv.setItem(i, makeCategoryIcon(cat));
        }

        player.openInventory(inv);

    }

    private static void openItemList(Player player, ItemCategory category) {

        String title = LIST_PREFIX + category.getDisplayName();
        Inventory inv = player.getServer().createInventory(null, 54, title);

        ItemStack filler = makeFiller();
        for (int i = 45; i < 54; i++) inv.setItem(i, filler);
        inv.setItem(BACK_SLOT, makeBackButton());

        List<RegisteredItem> items = ItemManager.getByCategory(category);
        for (int i = 0; i < items.size() && i < 45; i++) {
            inv.setItem(i, items.get(i).getItemStack().clone());
        }

        player.openInventory(inv);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) return;

        String title = event.getView().getTitle();
        boolean isCategoryMenu = CATEGORY_TITLE.equals(title);
        boolean isItemList = title.startsWith(LIST_PREFIX);

        if (!isCategoryMenu && !isItemList) return;

        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot >= 54) return;

        if (isCategoryMenu) {
            ItemCategory cat = CATEGORY_SLOTS[slot];
            if (cat != null) openItemList(player, cat);
            return;
        }

        // Item list
        if (slot == BACK_SLOT) {
            openCategoryMenu(player);
            return;
        }

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR
                || clicked.getType() == Material.GRAY_STAINED_GLASS_PANE) return;

        player.getInventory().addItem(clicked.clone());

    }

    private static ItemStack makeCategoryIcon(ItemCategory category) {

        ItemStack icon = new ItemStack(category.getIcon());
        ItemMeta meta = icon.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GREEN + category.getDisplayName());
        icon.setItemMeta(meta);
        return icon;

    }

    static ItemStack makeBackButton() {

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta meta = back.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "← Back");
        back.setItemMeta(meta);
        return back;

    }

    static ItemStack makeFiller() {

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = filler.getItemMeta();
        assert meta != null;
        meta.setDisplayName(" ");
        filler.setItemMeta(meta);
        return filler;

    }

}
