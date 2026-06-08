package me.lazaara.typicalAgronomy.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class CondensedMeliorEssence extends CustomItem {

    public CondensedMeliorEssence() {
        super(Material.GRAY_CONCRETE);
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.GRAY + "Condensed Melior Essence";
    }

}
