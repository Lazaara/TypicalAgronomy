package me.lazaara.typicalAgronomy.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class MediorEssence extends CustomItem {

    public MediorEssence() {
        super(Material.REDSTONE);
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.RED + "Medior Essence";
    }

}
