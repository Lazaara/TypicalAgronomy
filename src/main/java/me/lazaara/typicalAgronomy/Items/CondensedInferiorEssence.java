package me.lazaara.typicalAgronomy.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class CondensedInferiorEssence extends CustomItem {

    public CondensedInferiorEssence() {
        super(Material.GLOWSTONE);
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.GOLD + "Condensed Inferior Essence";
    }

}
