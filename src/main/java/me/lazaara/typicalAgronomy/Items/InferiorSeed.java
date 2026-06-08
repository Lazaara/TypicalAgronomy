package me.lazaara.typicalAgronomy.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

public class InferiorSeed extends CustomItem {

    public InferiorSeed() {

        super(Material.WHEAT_SEEDS);
        ItemMeta meta = super.getItem().getItemMeta();

        List<String> lore = new LinkedList<>();
        lore.add(ChatColor.GRAY + "Bước khởi đầu cho mọi thứ");

        assert meta != null;
        meta.setLore(lore);
        super.getItem().setItemMeta(meta);

    }

    @Override
    protected String getDisplayName() {
        return ChatColor.GOLD + "Inferior Seed";
    }

}
