package me.lazaara.typicalAgronomy.Managers;

import me.lazaara.typicalAgronomy.Items.CraftingStation;
import me.lazaara.typicalAgronomy.Items.InferiorEssence;
import me.lazaara.typicalAgronomy.Items.InferiorSeed;
import me.lazaara.typicalAgronomy.Items.ItemCategory;
import me.lazaara.typicalAgronomy.Items.RegisteredItem;
import me.lazaara.typicalAgronomy.TypicalAgronomy;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class ItemManager {

    public static final HashMap<Integer, RegisteredItem> itemList = new HashMap<>();

    static {
        itemList.put(1, new RegisteredItem(1, new InferiorSeed().getItem(),    ItemCategory.SEEDS));
        itemList.put(2, new RegisteredItem(2, new InferiorEssence().getItem(), ItemCategory.ESSENCES));
        itemList.put(3, new RegisteredItem(3, new CraftingStation().getItem(), ItemCategory.STATIONS));
    }

    public static ItemStack getItemStack(int id) {
        RegisteredItem item = itemList.get(id);
        return item != null ? item.getItemStack() : null;
    }

    public static List<RegisteredItem> getByCategory(ItemCategory category) {
        return itemList.values().stream()
                .filter(ri -> ri.getCategory() == category)
                .toList();
    }

    public static boolean itemsDisplayMatch(ItemStack a, ItemStack b) {
        if (a == null || b == null) return false;
        if (a.getType() != b.getType()) return false;
        ItemMeta metaA = a.getItemMeta();
        ItemMeta metaB = b.getItemMeta();
        if (metaA == null || metaB == null) return false;
        if (!metaA.hasDisplayName() || !metaB.hasDisplayName()) return false;
        return metaA.getDisplayName().equals(metaB.getDisplayName());
    }

    public static ItemCategory getCategoryForItem(ItemStack item) {
        if (item == null) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return null;
        String displayName = meta.getDisplayName();
        for (RegisteredItem ri : itemList.values()) {
            ItemMeta riMeta = ri.getItemStack().getItemMeta();
            if (riMeta != null && riMeta.hasDisplayName()
                    && riMeta.getDisplayName().equals(displayName)
                    && ri.getItemStack().getType() == item.getType()) {
                return ri.getCategory();
            }
        }
        return null;
    }

    public static void GiveItem(int id, Player player) {

        RegisteredItem registeredItem = itemList.get(id);

        if (registeredItem == null) {
            player.sendMessage(TypicalAgronomy.getPlugin(TypicalAgronomy.class).getLang("messages.getitem.item-not-exist"));
            return;
        }

        player.getInventory().addItem(registeredItem.getItemStack().clone());

    }

}