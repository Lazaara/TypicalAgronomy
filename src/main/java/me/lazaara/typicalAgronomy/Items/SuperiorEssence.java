package me.lazaara.typicalAgronomy.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class SuperiorEssence extends CustomItem {

    public SuperiorEssence() {
        super(Material.BLAZE_POWDER);
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.YELLOW + "Superior Essence";
    }

}
