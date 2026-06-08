package me.lazaara.typicalAgronomy.Crops;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CustomCrop {

    private final int seedItemID;
    private final Material cropBlock;
    private final List<ItemStack> drops;

    public CustomCrop(int seedItemID, Material cropBlock, List<ItemStack> drops) {
        this.seedItemID = seedItemID;
        this.cropBlock = cropBlock;
        this.drops = drops;
    }

    public int getSeedItemID() {
        return seedItemID;
    }

    public Material getCropBlock() {
        return cropBlock;
    }

    public List<ItemStack> getDrops() {
        return drops;
    }

}
