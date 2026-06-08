package me.lazaara.typicalAgronomy.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class MediorSeed extends CustomItem {

    public MediorSeed() {
        super(Material.WHEAT_SEEDS);
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.RED + "Medior Seed";
    }

}
