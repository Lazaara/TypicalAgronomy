package me.lazaara.typicalAgronomy.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class CondensedSuperiorEssence extends CustomItem {

    public CondensedSuperiorEssence() {
        super(Material.YELLOW_CONCRETE);
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.YELLOW + "Condensed Superior Essence";
    }

}
