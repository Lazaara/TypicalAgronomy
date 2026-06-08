package me.lazaara.typicalAgronomy.Items;

import org.bukkit.Material;

public enum ItemCategory {

    SEEDS("Seeds", Material.WHEAT_SEEDS),
    ESSENCES("Essences", Material.GLOWSTONE_DUST),
    STATIONS("Stations", Material.CRAFTING_TABLE);

    private final String displayName;
    private final Material icon;

    ItemCategory(String displayName, Material icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getIcon() {
        return icon;
    }

}
