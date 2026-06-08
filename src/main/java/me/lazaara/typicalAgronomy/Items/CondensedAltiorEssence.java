package me.lazaara.typicalAgronomy.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class CondensedAltiorEssence extends CustomItem {

    public CondensedAltiorEssence() {
        super(Material.PURPLE_CONCRETE);
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.LIGHT_PURPLE + "Condensed Altior Essence";
    }

}
