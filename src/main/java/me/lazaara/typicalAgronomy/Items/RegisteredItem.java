package me.lazaara.typicalAgronomy.Items;

import org.bukkit.inventory.ItemStack;

public class RegisteredItem {

    private final int id;
    private final ItemStack itemStack;
    private final ItemCategory category;

    public RegisteredItem(int id, ItemStack itemStack, ItemCategory category) {
        this.id = id;
        this.itemStack = itemStack;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemCategory getCategory() {
        return category;
    }

}
