package me.lazaara.typicalAgronomy.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class SuperiorSeed extends CustomItem {

    public SuperiorSeed() {
        super(Material.WHEAT_SEEDS);
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.YELLOW + "Superior Seed";
    }

}
