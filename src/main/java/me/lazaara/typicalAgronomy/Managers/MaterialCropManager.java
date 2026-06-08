package me.lazaara.typicalAgronomy.Managers;

import me.lazaara.typicalAgronomy.Crops.CustomCrop;
import me.lazaara.typicalAgronomy.Crops.MaterialCrop;
import me.lazaara.typicalAgronomy.Items.AltiorEssence;
import me.lazaara.typicalAgronomy.Items.AltiorSeed;
import me.lazaara.typicalAgronomy.Items.InferiorEssence;
import me.lazaara.typicalAgronomy.Items.InferiorSeed;
import me.lazaara.typicalAgronomy.Items.ItemCategory;
import me.lazaara.typicalAgronomy.Items.MediorEssence;
import me.lazaara.typicalAgronomy.Items.MediorSeed;
import me.lazaara.typicalAgronomy.Items.MeliorEssence;
import me.lazaara.typicalAgronomy.Items.MeliorSeed;
import me.lazaara.typicalAgronomy.Items.RegisteredItem;
import me.lazaara.typicalAgronomy.Items.SuperiorEssence;
import me.lazaara.typicalAgronomy.Items.SuperiorSeed;
import me.lazaara.typicalAgronomy.Recipes.ShapedCustomRecipe;
import me.lazaara.typicalAgronomy.Recipes.StationType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class MaterialCropManager {

    private static final ChatColor[] TIER_COLORS = {
        null,
        ChatColor.GOLD,         // 1 - Inferior
        ChatColor.RED,          // 2 - Medior
        ChatColor.GRAY,         // 3 - Melior
        ChatColor.YELLOW,       // 4 - Superior
        ChatColor.LIGHT_PURPLE  // 5 - Altior
    };

    private static final List<MaterialCrop> CROPS = List.of(

        // ── Tier 1 ──────────────────────────────────────────────────────────────
        new MaterialCrop("Dirt",         Material.DIRT,         Material.BROWN_DYE,      1),
        new MaterialCrop("Cobblestone",  Material.COBBLESTONE,  Material.GRAY_DYE,       1),
        new MaterialCrop("Andesite",     Material.ANDESITE,     Material.GRAY_DYE,       1),
        new MaterialCrop("Diorite",      Material.DIORITE,      Material.LIGHT_GRAY_DYE, 1),
        new MaterialCrop("Granite",      Material.GRANITE,      Material.ORANGE_DYE,     1),
        new MaterialCrop("Sand",         Material.SAND,         Material.YELLOW_DYE,     1),
        new MaterialCrop("Red Sand",     Material.RED_SAND,     Material.RED_DYE,        1),
        new MaterialCrop("Gravel",       Material.GRAVEL,       Material.GRAY_DYE,       1),
        new MaterialCrop("Clay Ball",    Material.CLAY_BALL,    Material.LIGHT_GRAY_DYE, 1),
        new MaterialCrop("Oak Log",      Material.OAK_LOG,      Material.BROWN_DYE,      1),
        new MaterialCrop("Spruce Log",   Material.SPRUCE_LOG,   Material.BROWN_DYE,      1),
        new MaterialCrop("Birch Log",    Material.BIRCH_LOG,    Material.YELLOW_DYE,     1),
        new MaterialCrop("Jungle Log",   Material.JUNGLE_LOG,   Material.BROWN_DYE,      1),
        new MaterialCrop("Acacia Log",   Material.ACACIA_LOG,   Material.ORANGE_DYE,     1),
        new MaterialCrop("Dark Oak Log", Material.DARK_OAK_LOG, Material.BROWN_DYE,      1),
        new MaterialCrop("Cherry Log",   Material.CHERRY_LOG,   Material.PINK_DYE,       1),
        new MaterialCrop("Mangrove Log", Material.MANGROVE_LOG, Material.RED_DYE,        1),
        new MaterialCrop("Coal",         Material.COAL,         Material.BLACK_DYE,      1),
        new MaterialCrop("Copper Ingot", Material.COPPER_INGOT, Material.ORANGE_DYE,     1),
        new MaterialCrop("Rotten Flesh", Material.ROTTEN_FLESH, Material.RED_DYE,        1),
        new MaterialCrop("Bone",         Material.BONE,         Material.WHITE_DYE,      1),
        new MaterialCrop("String",       Material.STRING,       Material.WHITE_DYE,      1),
        new MaterialCrop("Feather",      Material.FEATHER,      Material.WHITE_DYE,      1),
        new MaterialCrop("Kelp",         Material.KELP,         Material.LIME_DYE,       1),
        new MaterialCrop("Sugar Cane",   Material.SUGAR_CANE,   Material.LIME_DYE,       1),
        new MaterialCrop("Wheat",        Material.WHEAT,        Material.YELLOW_DYE,     1),
        new MaterialCrop("Apple",        Material.APPLE,        Material.RED_DYE,        1),
        new MaterialCrop("Flint",        Material.FLINT,        Material.BLACK_DYE,      1),

        // ── Tier 2 ──────────────────────────────────────────────────────────────
        new MaterialCrop("Iron Ingot",    Material.IRON_INGOT,    Material.LIGHT_GRAY_DYE, 2),
        new MaterialCrop("Redstone Dust", Material.REDSTONE,      Material.RED_DYE,        2),
        new MaterialCrop("Glowstone Dust",Material.GLOWSTONE_DUST,Material.YELLOW_DYE,    2),
        new MaterialCrop("Amethyst Shard",Material.AMETHYST_SHARD,Material.PURPLE_DYE,    2),
        new MaterialCrop("Obsidian",      Material.OBSIDIAN,      Material.BLACK_DYE,      2),
        new MaterialCrop("Calcite",       Material.CALCITE,       Material.WHITE_DYE,      2),
        new MaterialCrop("Tuff",          Material.TUFF,          Material.GRAY_DYE,       2),
        new MaterialCrop("Leather",       Material.LEATHER,       Material.BROWN_DYE,      2),
        new MaterialCrop("Slimeball",     Material.SLIME_BALL,    Material.LIME_DYE,       2),
        new MaterialCrop("Ink Sac",       Material.INK_SAC,       Material.BLACK_DYE,      2),
        new MaterialCrop("Glow Ink Sac",  Material.GLOW_INK_SAC,  Material.YELLOW_DYE,    2),
        new MaterialCrop("Honeycomb",     Material.HONEYCOMB,     Material.YELLOW_DYE,     2),
        new MaterialCrop("Melon Slice",   Material.MELON_SLICE,   Material.LIME_DYE,       2),
        new MaterialCrop("Pumpkin",       Material.PUMPKIN,       Material.ORANGE_DYE,     2),
        new MaterialCrop("Cactus",        Material.CACTUS,        Material.GREEN_DYE,      2),
        new MaterialCrop("Cocoa Beans",   Material.COCOA_BEANS,   Material.BROWN_DYE,      2),
        new MaterialCrop("Snowball",      Material.SNOWBALL,      Material.WHITE_DYE,      2),
        new MaterialCrop("Netherrack",    Material.NETHERRACK,    Material.RED_DYE,        2),
        new MaterialCrop("Soul Sand",     Material.SOUL_SAND,     Material.BROWN_DYE,      2),
        new MaterialCrop("Soul Soil",     Material.SOUL_SOIL,     Material.BROWN_DYE,      2),

        // ── Tier 3 ──────────────────────────────────────────────────────────────
        new MaterialCrop("Gold Ingot",         Material.GOLD_INGOT,         Material.YELLOW_DYE,     3),
        new MaterialCrop("Lapis Lazuli",       Material.LAPIS_LAZULI,       Material.BLUE_DYE,       3),
        new MaterialCrop("Nether Quartz",      Material.QUARTZ,             Material.WHITE_DYE,      3),
        new MaterialCrop("Gunpowder",          Material.GUNPOWDER,          Material.GRAY_DYE,       3),
        new MaterialCrop("Spider Eye",         Material.SPIDER_EYE,         Material.RED_DYE,        3),
        new MaterialCrop("Magma Cream",        Material.MAGMA_CREAM,        Material.ORANGE_DYE,     3),
        new MaterialCrop("Ender Pearl",        Material.ENDER_PEARL,        Material.CYAN_DYE,       3),
        new MaterialCrop("Prismarine Shard",   Material.PRISMARINE_SHARD,   Material.CYAN_DYE,       3),
        new MaterialCrop("Prismarine Crystals",Material.PRISMARINE_CRYSTALS,Material.CYAN_DYE,       3),
        new MaterialCrop("Rabbit Hide",        Material.RABBIT_HIDE,        Material.BROWN_DYE,      3),
        new MaterialCrop("Rabbit's Foot",      Material.RABBIT_FOOT,        Material.BROWN_DYE,      3),
        new MaterialCrop("Phantom Membrane",   Material.PHANTOM_MEMBRANE,   Material.LIGHT_GRAY_DYE, 3),
        new MaterialCrop("Armadillo Scute",    Material.ARMADILLO_SCUTE,    Material.ORANGE_DYE,     3),
        new MaterialCrop("Turtle Scute",       Material.TURTLE_SCUTE,       Material.LIME_DYE,       3),
        new MaterialCrop("Sponge",             Material.SPONGE,             Material.YELLOW_DYE,     3),
        new MaterialCrop("Basalt",             Material.BASALT,             Material.BLACK_DYE,      3),
        new MaterialCrop("Blackstone",         Material.BLACKSTONE,         Material.BLACK_DYE,      3),

        // ── Tier 4 ──────────────────────────────────────────────────────────────
        new MaterialCrop("Diamond",              Material.DIAMOND,              Material.LIGHT_BLUE_DYE, 4),
        new MaterialCrop("Emerald",              Material.EMERALD,              Material.GREEN_DYE,      4),
        new MaterialCrop("Blaze Rod",            Material.BLAZE_ROD,            Material.YELLOW_DYE,     4),
        new MaterialCrop("Ghast Tear",           Material.GHAST_TEAR,           Material.WHITE_DYE,      4),
        new MaterialCrop("Shulker Shell",        Material.SHULKER_SHELL,        Material.PURPLE_DYE,     4),
        new MaterialCrop("Breeze Rod",           Material.BREEZE_ROD,           Material.CYAN_DYE,       4),
        new MaterialCrop("Echo Shard",           Material.ECHO_SHARD,           Material.CYAN_DYE,       4),
        new MaterialCrop("Popped Chorus Fruit",  Material.POPPED_CHORUS_FRUIT,  Material.PURPLE_DYE,     4),
        new MaterialCrop("End Stone",            Material.END_STONE,            Material.YELLOW_DYE,     4),
        new MaterialCrop("Crying Obsidian",      Material.CRYING_OBSIDIAN,      Material.PURPLE_DYE,     4),
        new MaterialCrop("Heart of the Sea",     Material.HEART_OF_THE_SEA,     Material.CYAN_DYE,       4),
        new MaterialCrop("Nautilus Shell",       Material.NAUTILUS_SHELL,       Material.CYAN_DYE,       4),
        new MaterialCrop("Fermented Spider Eye", Material.FERMENTED_SPIDER_EYE, Material.BROWN_DYE,      4),
        new MaterialCrop("Glistering Melon Slice",Material.GLISTERING_MELON_SLICE,Material.YELLOW_DYE,  4),
        new MaterialCrop("Golden Carrot",        Material.GOLDEN_CARROT,        Material.YELLOW_DYE,     4),

        // ── Tier 5 ──────────────────────────────────────────────────────────────
        new MaterialCrop("Netherite Scrap",       Material.NETHERITE_SCRAP,       Material.BLACK_DYE,      5),
        new MaterialCrop("Wither Skeleton Skull", Material.WITHER_SKELETON_SKULL, Material.BLACK_DYE,      5),
        new MaterialCrop("Totem of Undying",      Material.TOTEM_OF_UNDYING,      Material.YELLOW_DYE,     5),
        new MaterialCrop("Heavy Core",            Material.HEAVY_CORE,            Material.BLACK_DYE,      5),
        new MaterialCrop("Nether Star",           Material.NETHER_STAR,           Material.WHITE_DYE,      5),
        new MaterialCrop("Dragon's Breath",       Material.DRAGON_BREATH,         Material.PURPLE_DYE,     5),
        new MaterialCrop("Enchanted Golden Apple",Material.ENCHANTED_GOLDEN_APPLE,Material.YELLOW_DYE,    5),
        new MaterialCrop("Ominous Bottle",        Material.OMINOUS_BOTTLE,        Material.PURPLE_DYE,     5),
        new MaterialCrop("Elytra",                Material.ELYTRA,                Material.GRAY_DYE,       5)

    );

    public static ItemStack createSeedItem(MaterialCrop crop) {
        ItemStack item = new ItemStack(Material.WHEAT_SEEDS);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(TIER_COLORS[crop.tier()] + crop.name() + " Seed");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createEssenceItem(MaterialCrop crop) {
        ItemStack item = new ItemStack(crop.dyeMaterial());
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(TIER_COLORS[crop.tier()] + crop.name() + " Essence");
        item.setItemMeta(meta);
        return item;
    }

    public static void registerAll() {

        Map<Integer, ItemStack> tierSeeds = Map.of(
            1, new InferiorSeed().getItem(),
            2, new MediorSeed().getItem(),
            3, new MeliorSeed().getItem(),
            4, new SuperiorSeed().getItem(),
            5, new AltiorSeed().getItem()
        );
        Map<Integer, ItemStack> tierEssences = Map.of(
            1, new InferiorEssence().getItem(),
            2, new MediorEssence().getItem(),
            3, new MeliorEssence().getItem(),
            4, new SuperiorEssence().getItem(),
            5, new AltiorEssence().getItem()
        );

        // Start IDs after the 16 manually registered items
        int nextId = 17;

        for (MaterialCrop crop : CROPS) {

            ItemStack seedItem    = createSeedItem(crop);
            ItemStack essenceItem = createEssenceItem(crop);
            ItemStack displayItem = new ItemStack(crop.material());

            // Register crop
            CropRegistry.registerCrop(new CustomCrop(
                crop.getCropKey(),
                seedItem.clone(),
                displayItem,
                Material.WHEAT,
                List.of(seedItem.clone(), essenceItem.clone())
            ));

            // Register in item browser
            ItemManager.itemList.put(nextId++, new RegisteredItem(nextId - 1, seedItem.clone(),    ItemCategory.SEEDS));
            ItemManager.itemList.put(nextId++, new RegisteredItem(nextId - 1, essenceItem.clone(), ItemCategory.ESSENCES));

            // Seed recipe: mat corners + tier essence cardinals + tier seed center
            ItemStack mat = new ItemStack(crop.material());
            ItemStack te  = tierEssences.get(crop.tier());
            ItemStack ts  = tierSeeds.get(crop.tier());
            RecipeManager.registerRecipe(new ShapedCustomRecipe(
                new ItemStack[]{ mat, te, mat, te, ts, te, mat, te, mat },
                seedItem.clone(),
                StationType.AGRONOMY_STATION
            ));

            // Reconstitution recipe: 9x essence → 1x original material
            ItemStack ess = essenceItem.clone();
            RecipeManager.registerRecipe(new ShapedCustomRecipe(
                new ItemStack[]{ ess, ess, ess, ess, ess, ess, ess, ess, ess },
                new ItemStack(crop.material()),
                StationType.AGRONOMY_STATION
            ));
        }
    }

}
