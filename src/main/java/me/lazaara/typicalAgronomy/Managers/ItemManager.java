package me.lazaara.typicalAgronomy.Managers;

import me.lazaara.typicalAgronomy.Items.AltiorEssence;
import me.lazaara.typicalAgronomy.Items.AltiorSeed;
import me.lazaara.typicalAgronomy.Items.CondensedAltiorEssence;
import me.lazaara.typicalAgronomy.Items.CondensedInferiorEssence;
import me.lazaara.typicalAgronomy.Items.CondensedMediorEssence;
import me.lazaara.typicalAgronomy.Items.CondensedMeliorEssence;
import me.lazaara.typicalAgronomy.Items.CondensedSuperiorEssence;
import me.lazaara.typicalAgronomy.Items.CraftingStation;
import me.lazaara.typicalAgronomy.Items.InferiorEssence;
import me.lazaara.typicalAgronomy.Items.InferiorSeed;
import me.lazaara.typicalAgronomy.Items.ItemCategory;
import me.lazaara.typicalAgronomy.Items.MediorEssence;
import me.lazaara.typicalAgronomy.Items.MediorSeed;
import me.lazaara.typicalAgronomy.Items.MeliorEssence;
import me.lazaara.typicalAgronomy.Items.MeliorSeed;
import me.lazaara.typicalAgronomy.Items.RegisteredItem;
import me.lazaara.typicalAgronomy.Items.SuperiorEssence;
import me.lazaara.typicalAgronomy.Items.SuperiorSeed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class ItemManager {

    public static final HashMap<Integer, RegisteredItem> itemList = new HashMap<>();

    static {
        itemList.put(1, new RegisteredItem(1, new InferiorSeed().getItem(),              ItemCategory.SEEDS));
        itemList.put(2, new RegisteredItem(2, new InferiorEssence().getItem(),           ItemCategory.ESSENCES));
        itemList.put(3, new RegisteredItem(3, new CraftingStation().getItem(),           ItemCategory.STATIONS));
        itemList.put(4,  new RegisteredItem(4,  new CondensedInferiorEssence().getItem(),  ItemCategory.ESSENCES));
        itemList.put(5,  new RegisteredItem(5,  new MediorEssence().getItem(),             ItemCategory.ESSENCES));
        itemList.put(6,  new RegisteredItem(6,  new CondensedMediorEssence().getItem(),   ItemCategory.ESSENCES));
        itemList.put(7,  new RegisteredItem(7,  new MediorSeed().getItem(),               ItemCategory.SEEDS));
        itemList.put(8,  new RegisteredItem(8,  new MeliorEssence().getItem(),            ItemCategory.ESSENCES));
        itemList.put(9,  new RegisteredItem(9,  new CondensedMeliorEssence().getItem(),   ItemCategory.ESSENCES));
        itemList.put(10, new RegisteredItem(10, new MeliorSeed().getItem(),               ItemCategory.SEEDS));
        itemList.put(11, new RegisteredItem(11, new SuperiorEssence().getItem(),          ItemCategory.ESSENCES));
        itemList.put(12, new RegisteredItem(12, new CondensedSuperiorEssence().getItem(), ItemCategory.ESSENCES));
        itemList.put(13, new RegisteredItem(13, new SuperiorSeed().getItem(),             ItemCategory.SEEDS));
        itemList.put(14, new RegisteredItem(14, new AltiorEssence().getItem(),            ItemCategory.ESSENCES));
        itemList.put(15, new RegisteredItem(15, new CondensedAltiorEssence().getItem(),   ItemCategory.ESSENCES));
        itemList.put(16, new RegisteredItem(16, new AltiorSeed().getItem(),               ItemCategory.SEEDS));
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

}