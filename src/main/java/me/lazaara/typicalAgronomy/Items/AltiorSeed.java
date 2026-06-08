package me.lazaara.typicalAgronomy.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class AltiorSeed extends CustomItem {

    public AltiorSeed() {
        super(Material.WHEAT_SEEDS);
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.LIGHT_PURPLE + "Altior Seed";
    }

}
