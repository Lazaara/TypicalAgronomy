package me.lazaara.typicalAgronomy.Listeners;

import me.lazaara.typicalAgronomy.Managers.CropManager;
import me.lazaara.typicalAgronomy.Managers.CropRegistry;
import me.lazaara.typicalAgronomy.Crops.CustomCrop;
import me.lazaara.typicalAgronomy.TypicalAgronomy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CropListener implements Listener {

    private final TypicalAgronomy plugin;
    private final File dataFile;

    public CropListener(TypicalAgronomy plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "crops.yml");
        loadCrops();
    }

    @EventHandler
    public void onPlant(PlayerInteractEvent event) {

        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.FARMLAND) return;

        CustomCrop crop = CropRegistry.findBySeed(event.getItem());
        if (crop == null) return;

        Location cropLocation = event.getClickedBlock().getLocation().add(0, 1, 0);
        CropManager.trackedCrops.put(cropLocation, crop.getSeedItemID());
        saveCrops();

    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        Location loc = event.getBlock().getLocation();
        Integer seedID = CropManager.trackedCrops.get(loc);
        if (seedID == null) return;

        CustomCrop crop = CropRegistry.findByID(seedID);
        if (crop == null || event.getBlock().getType() != crop.getCropBlock()) return;

        event.setDropItems(false);
        CropManager.trackedCrops.remove(loc);
        saveCrops();

        Ageable ageable = (Ageable) event.getBlock().getBlockData();
        if (ageable.getAge() != ageable.getMaximumAge()) return;

        for (ItemStack drop : crop.getDrops()) {
            event.getBlock().getWorld().dropItemNaturally(loc, drop);
        }

    }

    private void loadCrops() {

        if (!dataFile.exists()) return;

        YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
        var section = data.getConfigurationSection("crops");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            String[] parts = key.split(";");
            if (parts.length != 4) continue;

            World world = Bukkit.getWorld(parts[0]);
            if (world == null) continue;

            try {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                int z = Integer.parseInt(parts[3]);
                int id = section.getInt(key);
                CropManager.trackedCrops.put(new Location(world, x, y, z), id);
            } catch (NumberFormatException ignored) {}
        }

    }

    private void saveCrops() {

        YamlConfiguration data = new YamlConfiguration();

        for (Map.Entry<Location, Integer> entry : CropManager.trackedCrops.entrySet()) {
            Location loc = entry.getKey();
            if (loc.getWorld() == null) continue;
            String key = loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
            data.set("crops." + key, entry.getValue());
        }

        try {
            data.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save crops.yml: " + e.getMessage());
        }

    }

}