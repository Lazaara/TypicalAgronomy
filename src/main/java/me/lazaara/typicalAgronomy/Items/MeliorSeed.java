package me.lazaara.typicalAgronomy.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class MeliorSeed extends CustomItem {

    public MeliorSeed() {
        super(Material.WHEAT_SEEDS);
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.GRAY + "Melior Seed";
    }

}
