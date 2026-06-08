package me.lazaara.typicalAgronomy.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class MeliorEssence extends CustomItem {

    public MeliorEssence() {
        super(Material.GUNPOWDER);
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.GRAY + "Melior Essence";
    }

}
