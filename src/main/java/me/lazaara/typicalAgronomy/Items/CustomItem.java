package me.lazaara.typicalAgronomy.Items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class CustomItem {

    protected ItemStack itemStack;

    public CustomItem(Material material) {
        this.itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName(getDisplayName());
            itemStack.setItemMeta(itemMeta);
        }
    }

    protected abstract String getDisplayName();

    public ItemStack getItem() {
        return itemStack;
    }

}
