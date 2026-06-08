package me.lazaara.typicalAgronomy.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class AltiorEssence extends CustomItem {

    public AltiorEssence() {
        super(Material.DRAGON_BREATH);
    }

    @Override
    protected String getDisplayName() {
        return ChatColor.LIGHT_PURPLE + "Altior Essence";
    }

}
