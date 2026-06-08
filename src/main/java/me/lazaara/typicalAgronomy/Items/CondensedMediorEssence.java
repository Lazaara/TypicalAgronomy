package me.lazaara.typicalAgronomy.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class CondensedMediorEssence extends CustomItem {

    public CondensedMediorEssence() {
        super(Material.REDSTONE_BLOCK);
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.RED + "Condensed Medior Essence";
    }

}
