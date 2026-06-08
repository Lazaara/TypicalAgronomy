package me.lazaara.typicalAgronomy.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CraftingStation extends CustomItem {

    public CraftingStation() {

        super(Material.CRAFTING_TABLE);
        ItemMeta meta = super.getItem().getItemMeta();

        assert meta != null;
        meta.setLore(List.of(ChatColor.GRAY + "Đặt xuống để chế tạo"));
        super.getItem().setItemMeta(meta);

    }

    @Override
    protected String getDisplayName() {
        return ChatColor.GOLD + "Agronomy Station";
    }

}