package me.lazaara.typicalAgronomy.Crops;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CustomCrop {

    private final String cropKey;
    private final ItemStack seedItem;
    private final ItemStack displayItem;
    private final Material cropBlock;
    private final List<ItemStack> drops;

    public CustomCrop(String cropKey, ItemStack seedItem, ItemStack displayItem, Material cropBlock, List<ItemStack> drops) {
        this.cropKey = cropKey;
        this.seedItem = seedItem;
        this.displayItem = displayItem;
        this.cropBlock = cropBlock;
        this.drops = drops;
    }

    public String getCropKey() {
        return cropKey;
    }

    public ItemStack getSeedItem() {
        return seedItem;
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public Material getCropBlock() {
        return cropBlock;
    }

    public List<ItemStack> getDrops() {
        return drops;
    }

}
