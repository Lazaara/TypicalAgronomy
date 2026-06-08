package me.lazaara.typicalAgronomy.Crops;

import org.bukkit.Material;

public record MaterialCrop(String name, Material material, Material dyeMaterial, int tier) {

    public String getCropKey() {
        return name.toLowerCase().replace(" ", "_").replace("'", "") + "_seed";
    }

}
