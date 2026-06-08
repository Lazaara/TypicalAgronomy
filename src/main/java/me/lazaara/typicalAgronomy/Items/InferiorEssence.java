package me.lazaara.typicalAgronomy.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public class InferiorEssence extends CustomItem {

    public InferiorEssence() {

        super(Material.GLOWSTONE_DUST);
        ItemMeta meta = super.getItem().getItemMeta();

        assert meta != null;
        super.getItem().setItemMeta(meta);

    }

    @Override
    protected String getDisplayName() {
        return ChatColor.GOLD + "Inferior Essence";
    }

}