package me.lazaara.typicalAgronomy.Recipes;

import org.bukkit.Material;

public enum StationType {

    AGRONOMY_STATION("Agronomy Station", Material.CRAFTING_TABLE);

    private final String displayName;
    private final Material icon;

    StationType(String displayName, Material icon) {
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