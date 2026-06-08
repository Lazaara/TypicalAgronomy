package me.lazaara.typicalAgronomy.Listeners;

import me.lazaara.typicalAgronomy.Items.ItemCategory;
import me.lazaara.typicalAgronomy.Managers.CropManager;
import me.lazaara.typicalAgronomy.Managers.CropRegistry;
import me.lazaara.typicalAgronomy.Managers.ItemManager;
import me.lazaara.typicalAgronomy.Crops.CustomCrop;
import me.lazaara.typicalAgronomy.TypicalAgronomy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class CropListener implements Listener {

    private static final Set<Material> VANILLA_CROPS = Set.of(
            Material.WHEAT, Material.CARROTS, Material.POTATOES,
            Material.BEETROOTS, Material.NETHER_WART, Material.COCOA
    );

    private final TypicalAgronomy plugin;
    private final File dataFile;
    private final NamespacedKey CROP_LOC_KEY;

    public CropListener(TypicalAgronomy plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "crops.yml");
        this.CROP_LOC_KEY = new NamespacedKey(plugin, "crop_loc");
        loadCrops();
    }

    private double yOffset(int age, int maxAge) {
        return 0.15 + (0.85 * ((double) age / maxAge));
    }

    private void spawnDisplay(Location blockLoc, CustomCrop crop, int age, int maxAge) {
        String tag = blockLoc.getWorld().getName() + ";" + blockLoc.getBlockX()
                + ";" + blockLoc.getBlockY() + ";" + blockLoc.getBlockZ();
        Location spawnLoc = blockLoc.clone().add(0.5, yOffset(age, maxAge), 0.5);
        Transformation base = new Transformation(
                new Vector3f(), new Quaternionf(),
                new Vector3f(2/3f, 2/3f, 2/3f),
                new Quaternionf()
        );
        Transformation rotated = new Transformation(
                new Vector3f(), new Quaternionf().rotateY((float) (Math.PI / 2)),
                new Vector3f(2/3f, 2/3f, 2/3f),
                new Quaternionf()
        );
        for (Transformation t : new Transformation[]{base, rotated}) {
            ItemDisplay display = (ItemDisplay) blockLoc.getWorld().spawnEntity(spawnLoc, EntityType.ITEM_DISPLAY);
            display.setItemStack(crop.getDisplayItem());
            display.setTransformation(t);
            display.setPersistent(false);
            display.getPersistentDataContainer().set(CROP_LOC_KEY, PersistentDataType.STRING, tag);
        }
    }

    private Collection<ItemDisplay> findDisplays(Location blockLoc) {
        String tag = blockLoc.getWorld().getName() + ";" + blockLoc.getBlockX()
                + ";" + blockLoc.getBlockY() + ";" + blockLoc.getBlockZ();
        return blockLoc.getWorld()
                .getNearbyEntities(blockLoc.clone().add(0.5, 1, 0.5), 1, 1, 1)
                .stream()
                .filter(e -> e instanceof ItemDisplay)
                .filter(e -> tag.equals(e.getPersistentDataContainer().get(CROP_LOC_KEY, PersistentDataType.STRING)))
                .map(e -> (ItemDisplay) e)
                .toList();
    }

    @EventHandler
    public void onCustomItemPlace(PlayerInteractEvent event) {

        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemCategory category = ItemManager.getCategoryForItem(event.getItem());
        if (category == ItemCategory.ESSENCES) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlant(PlayerInteractEvent event) {

        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.FARMLAND) return;

        CustomCrop crop = CropRegistry.findBySeed(event.getItem());
        if (crop == null) return;

        Location cropLocation = event.getClickedBlock().getLocation().add(0, 1, 0);
        CropManager.trackedCrops.put(cropLocation, crop.getCropKey());
        saveCrops();

        final CustomCrop finalCrop = crop;
        final Location finalLoc = cropLocation.clone();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!(finalLoc.getBlock().getBlockData() instanceof Ageable ageable)) return;
            spawnDisplay(finalLoc, finalCrop, ageable.getAge(), ageable.getMaximumAge());
        }, 1L);

    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        Location loc = event.getBlock().getLocation();

        // Farmland broken — uproot any tracked crop above it
        if (event.getBlock().getType() == Material.FARMLAND) {
            Location cropLoc = loc.clone().add(0, 1, 0);
            String cropKey = CropManager.trackedCrops.remove(cropLoc);
            if (cropKey != null) {
                saveCrops();
                findDisplays(cropLoc).forEach(Entity::remove);
                CustomCrop crop = CropRegistry.findByKey(cropKey);
                // Prevent the crop block from dropping vanilla items
                cropLoc.getBlock().setType(Material.AIR);
                // Return the seed since the crop was uprooted
                if (crop != null) {
                    event.getBlock().getWorld().dropItemNaturally(cropLoc, crop.getSeedItem().clone());
                }
            }
            return;
        }

        // Crop block broken directly
        String cropKey = CropManager.trackedCrops.get(loc);
        if (cropKey == null) return;

        CustomCrop crop = CropRegistry.findByKey(cropKey);
        if (crop == null || event.getBlock().getType() != crop.getCropBlock()) return;

        event.setDropItems(false);
        findDisplays(loc).forEach(Entity::remove);
        CropManager.trackedCrops.remove(loc);
        saveCrops();

        Ageable ageable = (Ageable) event.getBlock().getBlockData();
        if (ageable.getAge() != ageable.getMaximumAge()) return;

        for (ItemStack drop : crop.getDrops()) {
            event.getBlock().getWorld().dropItemNaturally(loc, drop);
        }

    }

    @EventHandler
    public void onFarmlandTrample(EntityChangeBlockEvent event) {

        if (event.getBlock().getType() != Material.FARMLAND) return;
        if (event.getTo() != Material.DIRT) return;

        Location cropLoc = event.getBlock().getLocation().add(0, 1, 0);
        String cropKey = CropManager.trackedCrops.remove(cropLoc);
        if (cropKey == null) return;

        event.setCancelled(true);
        CropManager.trackedCrops.put(cropLoc, cropKey);

    }

    @EventHandler
    public void onHoeHarvest(PlayerInteractEvent event) {

        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;

        ItemStack hoe = event.getItem();
        if (!isHoe(hoe)) return;

        Block block = event.getClickedBlock();
        if (!(block.getBlockData() instanceof Ageable ageable)) return;
        if (ageable.getAge() != ageable.getMaximumAge()) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        String cropKey = CropManager.trackedCrops.get(block.getLocation());

        if (cropKey != null) {
            CustomCrop crop = CropRegistry.findByKey(cropKey);
            if (crop == null) return;
            ItemStack seed = crop.getSeedItem();
            for (ItemStack drop : crop.getDrops()) {
                // Skip the seed — crop stays planted, no need to return it
                if (drop.getType() == seed.getType()) {
                    ItemMeta dropMeta = drop.getItemMeta();
                    ItemMeta seedMeta = seed.getItemMeta();
                    boolean dropHasName = dropMeta != null && dropMeta.hasDisplayName();
                    boolean seedHasName = seedMeta != null && seedMeta.hasDisplayName();
                    if (dropHasName == seedHasName && (!dropHasName || dropMeta.getDisplayName().equals(seedMeta.getDisplayName()))) {
                        continue;
                    }
                }
                block.getWorld().dropItemNaturally(block.getLocation(), drop);
            }
            ageable.setAge(0);
            block.setBlockData(ageable);
            findDisplays(block.getLocation()).forEach(Entity::remove);
            spawnDisplay(block.getLocation(), crop, 0, ageable.getMaximumAge());
        } else if (VANILLA_CROPS.contains(block.getType())) {
            for (ItemStack drop : block.getDrops()) {
                block.getWorld().dropItemNaturally(block.getLocation(), drop);
            }
            ageable.setAge(0);
            block.setBlockData(ageable);
        } else {
            return;
        }

        block.getWorld().playSound(block.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
        damageHoe(player, hoe);

    }

    @EventHandler
    public void onCropGrow(BlockGrowEvent event) {

        Location loc = event.getBlock().getLocation();
        if (!CropManager.trackedCrops.containsKey(loc)) return;

        CustomCrop crop = CropRegistry.findByKey(CropManager.trackedCrops.get(loc));
        if (crop == null) return;

        Ageable ageable = (Ageable) event.getNewState().getBlockData();
        Location newLoc = loc.clone().add(0.5, yOffset(ageable.getAge(), ageable.getMaximumAge()), 0.5);
        findDisplays(loc).forEach(e -> e.teleport(newLoc));

    }

    @EventHandler
    public void onFertilize(BlockFertilizeEvent event) {

        Location loc = event.getBlock().getLocation();
        if (!CropManager.trackedCrops.containsKey(loc)) return;

        CustomCrop crop = CropRegistry.findByKey(CropManager.trackedCrops.get(loc));
        if (crop == null) return;

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!(event.getBlock().getBlockData() instanceof Ageable ageable)) return;
            Location newLoc = loc.clone().add(0.5, yOffset(ageable.getAge(), ageable.getMaximumAge()), 0.5);
            findDisplays(loc).forEach(e -> e.teleport(newLoc));
        }, 1L);

    }

    private boolean isHoe(ItemStack item) {
        if (item == null) return false;
        return switch (item.getType()) {
            case WOODEN_HOE, STONE_HOE, IRON_HOE, GOLDEN_HOE,
                 DIAMOND_HOE, NETHERITE_HOE -> true;
            default -> false;
        };
    }

    private void damageHoe(Player player, ItemStack hoe) {
        int unbreakingLevel = hoe.getEnchantmentLevel(Enchantment.UNBREAKING);
        if (unbreakingLevel > 0 && Math.random() < unbreakingLevel * 0.20) return;

        ItemMeta meta = hoe.getItemMeta();
        if (!(meta instanceof Damageable damageable)) return;
        damageable.setDamage(damageable.getDamage() + 1);

        if (damageable.getDamage() >= hoe.getType().getMaxDurability()) {
            player.getInventory().setItemInMainHand(null);
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
        } else {
            hoe.setItemMeta(meta);
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
                String cropKey = section.getString(key);
                if (cropKey == null) continue;
                Location loc = new Location(world, x, y, z);
                CropManager.trackedCrops.put(loc, cropKey);

                CustomCrop crop = CropRegistry.findByKey(cropKey);
                if (crop != null && loc.getBlock().getBlockData() instanceof Ageable ageable) {
                    spawnDisplay(loc, crop, ageable.getAge(), ageable.getMaximumAge());
                }
            } catch (NumberFormatException ignored) {}
        }

    }

    private void saveCrops() {

        YamlConfiguration data = new YamlConfiguration();

        for (Map.Entry<Location, String> entry : CropManager.trackedCrops.entrySet()) {
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
