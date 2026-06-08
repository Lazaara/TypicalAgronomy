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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemMenuListener implements Listener {

    private static final String CATEGORY_TITLE = ChatColor.DARK_GREEN + "Item Categories";
    private static final String LIST_PREFIX    = ChatColor.DARK_GREEN + "Items: ";
    private static final int BACK_SLOT = 45;
    private static final int PREV_SLOT = 51;
    private static final int PAGE_SLOT = 52;
    private static final int NEXT_SLOT = 53;

    private static final Map<UUID, Integer> playerPage = new HashMap<>();

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

    static void openItemList(Player player, ItemCategory category, int page) {

        playerPage.put(player.getUniqueId(), page);

        String title = LIST_PREFIX + category.getDisplayName();
        Inventory inv = player.getServer().createInventory(null, 54, title);

        ItemStack filler = makeFiller();
        for (int i = 45; i < 54; i++) inv.setItem(i, filler);
        inv.setItem(BACK_SLOT, makeBackButton());

        List<RegisteredItem> items = ItemManager.getByCategory(category);
        int totalPages = Math.max(1, (int) Math.ceil(items.size() / 45.0));
        int start = page * 45;
        for (int i = start; i < Math.min(start + 45, items.size()); i++) {
            inv.setItem(i - start, items.get(i).getItemStack().clone());
        }

        if (page > 0)              inv.setItem(PREV_SLOT, makePrevButton());
        inv.setItem(PAGE_SLOT, makePageIndicator(page + 1, totalPages));
        if (page < totalPages - 1) inv.setItem(NEXT_SLOT, makeNextButton());

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
            if (cat != null) openItemList(player, cat, 0);
            return;
        }

        // Item list
        if (slot == BACK_SLOT) {
            openCategoryMenu(player);
            return;
        }

        if (slot == PREV_SLOT) {
            int page = playerPage.getOrDefault(player.getUniqueId(), 0);
            if (page > 0) openItemList(player, resolveCategory(title), page - 1);
            return;
        }

        if (slot == NEXT_SLOT) {
            ItemStack item = event.getCurrentItem();
            if (item == null || item.getType() == Material.GRAY_STAINED_GLASS_PANE) return;
            int page = playerPage.getOrDefault(player.getUniqueId(), 0);
            openItemList(player, resolveCategory(title), page + 1);
            return;
        }

        if (slot == PAGE_SLOT) return;

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR
                || clicked.getType() == Material.GRAY_STAINED_GLASS_PANE) return;

        player.getInventory().addItem(clicked.clone());

    }

    private static ItemCategory resolveCategory(String title) {
        String name = title.substring(LIST_PREFIX.length());
        for (ItemCategory cat : ItemCategory.values()) {
            if (cat.getDisplayName().equals(name)) return cat;
        }
        return null;
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

    static ItemStack makePrevButton() {

        ItemStack item = new ItemStack(Material.SPECTRAL_ARROW);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.YELLOW + "← Previous");
        item.setItemMeta(meta);
        return item;

    }

    static ItemStack makeNextButton() {

        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.YELLOW + "Next →");
        item.setItemMeta(meta);
        return item;

    }

    static ItemStack makePageIndicator(int page, int total) {

        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.YELLOW + "Page " + page + " / " + total);
        item.setItemMeta(meta);
        return item;

    }

}
