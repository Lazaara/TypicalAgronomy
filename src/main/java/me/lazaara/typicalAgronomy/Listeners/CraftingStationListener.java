package me.lazaara.typicalAgronomy.Listeners;

import me.lazaara.typicalAgronomy.Managers.ItemManager;
import me.lazaara.typicalAgronomy.Managers.RecipeManager;
import me.lazaara.typicalAgronomy.Listeners.RecipeMenuListener;
import me.lazaara.typicalAgronomy.Items.CraftingStation;
import me.lazaara.typicalAgronomy.TypicalAgronomy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CraftingStationListener implements Listener {

    private static final String GUI_TITLE = ChatColor.GOLD + "Agronomy Station";
    private static final List<Integer> INPUT_SLOTS_ORDERED = List.of(10, 11, 12, 19, 20, 21, 28, 29, 30);
    private static final Set<Integer> INPUT_SLOTS = new HashSet<>(INPUT_SLOTS_ORDERED);
    private static final int ARROW_SLOT = 23;
    private static final int OUTPUT_SLOT = 25;
    private static final int RECIPE_BOOK_SLOT = 32;

    private static final Set<Location> placedStations = new HashSet<>();

    private final TypicalAgronomy plugin;
    private final File dataFile;

    public CraftingStationListener(TypicalAgronomy plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "stations.yml");
        loadStations();
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        if (!isCraftingStation(event.getItemInHand())) return;

        placedStations.add(event.getBlock().getLocation());
        saveStations();

    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        Location loc = event.getBlock().getLocation();
        if (!placedStations.contains(loc)) return;

        placedStations.remove(loc);
        saveStations();
        event.setDropItems(false);
        event.getBlock().getWorld().dropItemNaturally(loc, new CraftingStation().getItem());

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getClickedBlock() == null) return;
        if (!placedStations.contains(event.getClickedBlock().getLocation())) return;

        event.setCancelled(true);
        openGUI(event.getPlayer());

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!GUI_TITLE.equals(event.getView().getTitle())) return;

        int slot = event.getRawSlot();

        // Shift-click from player inventory into GUI — recalculate output after it resolves
        if (slot >= 54) {
            if (event.isShiftClick()) {
                plugin.getServer().getScheduler().runTaskLater(plugin,
                        () -> updateOutput(event.getInventory()), 1L);
            }
            return;
        }

        if (slot == RECIPE_BOOK_SLOT) {
            event.setCancelled(true);
            RecipeMenuListener.openCategoryMenu(player);
            return;
        }

        if (!INPUT_SLOTS.contains(slot) && slot != OUTPUT_SLOT) {
            event.setCancelled(true);
            return;
        }

        if (slot == OUTPUT_SLOT) {
            event.setCancelled(true);
            ItemStack output = event.getInventory().getItem(OUTPUT_SLOT);
            if (output == null || output.getType() == Material.AIR) return;
            if (RecipeManager.match(getGrid(event.getInventory())) == null) return;

            INPUT_SLOTS.forEach(s -> event.getInventory().setItem(s, null));
            player.getInventory().addItem(output.clone());
            event.getInventory().setItem(OUTPUT_SLOT, null);
            return;
        }

        plugin.getServer().getScheduler().runTaskLater(plugin,
                () -> updateOutput(event.getInventory()), 1L);

    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {

        if (!GUI_TITLE.equals(event.getView().getTitle())) return;

        Inventory inv = event.getInventory();
        Player player = (Player) event.getPlayer();

        INPUT_SLOTS.forEach(slot -> {
            ItemStack item = inv.getItem(slot);
            if (item != null && item.getType() != Material.AIR) {
                player.getInventory().addItem(item);
                inv.setItem(slot, null);
            }
        });

    }

    private void loadStations() {

        if (!dataFile.exists()) return;

        YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
        List<?> raw = data.getList("stations");
        if (raw == null) return;

        for (Object entry : raw) {
            if (!(entry instanceof Location loc)) continue;
            placedStations.add(loc);
        }

    }

    private void saveStations() {

        YamlConfiguration data = new YamlConfiguration();
        data.set("stations", placedStations.stream().toList());

        try {
            data.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save stations.yml: " + e.getMessage());
        }

    }

    private void openGUI(Player player) {

        Inventory inv = player.getServer().createInventory(null, 54, GUI_TITLE);

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = filler.getItemMeta();
        assert meta != null;
        meta.setDisplayName(" ");
        filler.setItemMeta(meta);

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        assert arrowMeta != null;
        arrowMeta.setDisplayName(ChatColor.YELLOW + "➜");
        arrow.setItemMeta(arrowMeta);

        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta bookMeta = book.getItemMeta();
        assert bookMeta != null;
        bookMeta.setDisplayName(ChatColor.GREEN + "Recipe Browser");
        book.setItemMeta(bookMeta);

        for (int i = 0; i < 54; i++) {
            if (i == ARROW_SLOT) {
                inv.setItem(i, arrow);
            } else if (i == RECIPE_BOOK_SLOT) {
                inv.setItem(i, book);
            } else if (!INPUT_SLOTS.contains(i) && i != OUTPUT_SLOT) {
                inv.setItem(i, filler);
            }
        }

        player.openInventory(inv);

    }

    private void updateOutput(Inventory inv) {
        inv.setItem(OUTPUT_SLOT, RecipeManager.match(getGrid(inv)));
    }

    private ItemStack[] getGrid(Inventory inv) {
        return INPUT_SLOTS_ORDERED.stream()
                .map(inv::getItem)
                .toArray(ItemStack[]::new);
    }

    private boolean isCraftingStation(ItemStack item) {

        if (item == null) return false;

        ItemStack station = ItemManager.getItemStack(3);
        if (station == null) return false;

        ItemMeta meta = item.getItemMeta();
        ItemMeta stationMeta = station.getItemMeta();
        if (meta == null || stationMeta == null) return false;

        return meta.getDisplayName().equals(stationMeta.getDisplayName());

    }

}
