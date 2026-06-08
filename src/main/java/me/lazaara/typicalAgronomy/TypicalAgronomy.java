package me.lazaara.typicalAgronomy;

import me.lazaara.typicalAgronomy.Commands.TypicalAgronomyCommand;
import me.lazaara.typicalAgronomy.Listeners.CraftingStationListener;
import me.lazaara.typicalAgronomy.Listeners.CropListener;
import me.lazaara.typicalAgronomy.Listeners.ItemMenuListener;
import me.lazaara.typicalAgronomy.Listeners.RecipeMenuListener;
import me.lazaara.typicalAgronomy.Crops.CustomCrop;
import me.lazaara.typicalAgronomy.Items.AltiorEssence;
import me.lazaara.typicalAgronomy.Items.AltiorSeed;
import me.lazaara.typicalAgronomy.Items.CondensedAltiorEssence;
import me.lazaara.typicalAgronomy.Items.CondensedInferiorEssence;
import me.lazaara.typicalAgronomy.Items.CondensedMediorEssence;
import me.lazaara.typicalAgronomy.Items.CondensedMeliorEssence;
import me.lazaara.typicalAgronomy.Items.CondensedSuperiorEssence;
import me.lazaara.typicalAgronomy.Items.CraftingStation;
import me.lazaara.typicalAgronomy.Items.InferiorEssence;
import me.lazaara.typicalAgronomy.Items.InferiorSeed;
import me.lazaara.typicalAgronomy.Items.MediorEssence;
import me.lazaara.typicalAgronomy.Items.MediorSeed;
import me.lazaara.typicalAgronomy.Items.MeliorEssence;
import me.lazaara.typicalAgronomy.Items.MeliorSeed;
import me.lazaara.typicalAgronomy.Items.SuperiorEssence;
import me.lazaara.typicalAgronomy.Items.SuperiorSeed;
import me.lazaara.typicalAgronomy.Managers.CropRegistry;
import me.lazaara.typicalAgronomy.Managers.ItemManager;
import me.lazaara.typicalAgronomy.Managers.MaterialCropManager;
import me.lazaara.typicalAgronomy.Managers.RecipeManager;
import me.lazaara.typicalAgronomy.Recipes.ShapedCustomRecipe;
import me.lazaara.typicalAgronomy.Recipes.StationType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.Objects;

public final class TypicalAgronomy extends JavaPlugin {

    public ItemManager itemManager;
    private FileConfiguration lang;

    @Override
    public void onEnable(){

        saveDefaultConfig();
        saveResource("lang.yml", false);
        lang = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "lang.yml"));

        registerCommands();
        registerRecipes();
        registerCrops();
        registerCustomRecipes();
        MaterialCropManager.registerAll();
        registerEvents();

    }

    public String getLang(String path) {
        String msg = lang.getString(path, "&cMessage not found: " + path);
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    private void registerCommands() {

        TypicalAgronomyCommand typicalAgronomyCommand = new TypicalAgronomyCommand();
        Objects.requireNonNull(this.getCommand("typicalagronomy")).setExecutor(typicalAgronomyCommand);
        Objects.requireNonNull(this.getCommand("typicalagronomy")).setTabCompleter(typicalAgronomyCommand);

    }

    public void reload() {

        reloadConfig();
        lang = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "lang.yml"));

    }

    private void registerEvents() {

        getServer().getPluginManager().registerEvents(new CropListener(this), this);
        getServer().getPluginManager().registerEvents(new CraftingStationListener(this), this);
        getServer().getPluginManager().registerEvents(new ItemMenuListener(), this);
        getServer().getPluginManager().registerEvents(new RecipeMenuListener(), this);

    }

    private void registerCrops() {

        CropRegistry.registerCrop(new CustomCrop(
                "inferior_seed",
                new InferiorSeed().getItem(),
                new ItemStack(Material.GLOWSTONE_DUST),
                Material.WHEAT,
                List.of(new InferiorSeed().getItem(), new InferiorEssence().getItem())
        ));

        CropRegistry.registerCrop(new CustomCrop(
                "medior_seed",
                new MediorSeed().getItem(),
                new MediorEssence().getItem(),
                Material.WHEAT,
                List.of(new MediorSeed().getItem(), new MediorEssence().getItem())
        ));

        CropRegistry.registerCrop(new CustomCrop(
                "melior_seed",
                new MeliorSeed().getItem(),
                new MeliorEssence().getItem(),
                Material.WHEAT,
                List.of(new MeliorSeed().getItem(), new MeliorEssence().getItem())
        ));

        CropRegistry.registerCrop(new CustomCrop(
                "superior_seed",
                new SuperiorSeed().getItem(),
                new SuperiorEssence().getItem(),
                Material.WHEAT,
                List.of(new SuperiorSeed().getItem(), new SuperiorEssence().getItem())
        ));

        CropRegistry.registerCrop(new CustomCrop(
                "altior_seed",
                new AltiorSeed().getItem(),
                new AltiorEssence().getItem(),
                Material.WHEAT,
                List.of(new AltiorSeed().getItem(), new AltiorEssence().getItem())
        ));

    }

    private void registerRecipes() {

        NamespacedKey key = new NamespacedKey(this, "agronomy_station");
        getServer().removeRecipe(key);
        ShapelessRecipe recipe = new ShapelessRecipe(key, new CraftingStation().getItem());
        recipe.addIngredient(Material.CRAFTING_TABLE);
        recipe.addIngredient(Material.WHEAT_SEEDS);
        getServer().addRecipe(recipe);

    }

    private void registerCustomRecipes() {

        // Inferior Seed: corners = redstone, cardinal = glowstone, center = wheat seeds
        //   R G R
        //   G S G
        //   R G R
        ItemStack rd = new ItemStack(Material.REDSTONE);
        ItemStack gd = new ItemStack(Material.GLOWSTONE_DUST);
        ItemStack ws = new ItemStack(Material.WHEAT_SEEDS);

        RecipeManager.registerRecipe(new ShapedCustomRecipe(
                new ItemStack[]{ rd, gd, rd, gd, ws, gd, rd, gd, rd },
                new InferiorSeed().getItem(),
                StationType.AGRONOMY_STATION
        ));

        // Condensed Inferior Essence: 9x Inferior Essence
        ItemStack ie = new InferiorEssence().getItem();
        RecipeManager.registerRecipe(new ShapedCustomRecipe(
                new ItemStack[]{ ie, ie, ie, ie, ie, ie, ie, ie, ie },
                new CondensedInferiorEssence().getItem(),
                StationType.AGRONOMY_STATION
        ));

        // Medior Essence: 8x Condensed Inferior Essence surrounding 1x Inferior Essence
        ItemStack cie = new CondensedInferiorEssence().getItem();
        ItemStack ie2 = new InferiorEssence().getItem();
        RecipeManager.registerRecipe(new ShapedCustomRecipe(
                new ItemStack[]{ cie, cie, cie, cie, ie2, cie, cie, cie, cie },
                new MediorEssence().getItem(),
                StationType.AGRONOMY_STATION
        ));

        // Medior Seed: corners=CondensedInferior, cardinals=Medior, center=InferiorSeed
        ItemStack me  = new MediorEssence().getItem();
        ItemStack is2 = new InferiorSeed().getItem();
        RecipeManager.registerRecipe(new ShapedCustomRecipe(
                new ItemStack[]{ cie, me, cie, me, is2, me, cie, me, cie },
                new MediorSeed().getItem(),
                StationType.AGRONOMY_STATION
        ));

        // Condensed Medior Essence: 9x Medior Essence
        ItemStack me2 = new MediorEssence().getItem();
        RecipeManager.registerRecipe(new ShapedCustomRecipe(
                new ItemStack[]{ me2, me2, me2, me2, me2, me2, me2, me2, me2 },
                new CondensedMediorEssence().getItem(),
                StationType.AGRONOMY_STATION
        ));

        // Melior Essence: 8x Condensed Medior surrounding 1x Medior Essence
        ItemStack cme  = new CondensedMediorEssence().getItem();
        ItemStack me3  = new MediorEssence().getItem();
        RecipeManager.registerRecipe(new ShapedCustomRecipe(
                new ItemStack[]{ cme, cme, cme, cme, me3, cme, cme, cme, cme },
                new MeliorEssence().getItem(),
                StationType.AGRONOMY_STATION
        ));

        // Melior Seed: corners=CondensedMedior, cardinals=Melior, center=MediorSeed
        ItemStack mle  = new MeliorEssence().getItem();
        ItemStack ms2  = new MediorSeed().getItem();
        RecipeManager.registerRecipe(new ShapedCustomRecipe(
                new ItemStack[]{ cme, mle, cme, mle, ms2, mle, cme, mle, cme },
                new MeliorSeed().getItem(),
                StationType.AGRONOMY_STATION
        ));

        // Condensed Melior Essence: 9x Melior Essence
        ItemStack mle2 = new MeliorEssence().getItem();
        RecipeManager.registerRecipe(new ShapedCustomRecipe(
                new ItemStack[]{ mle2, mle2, mle2, mle2, mle2, mle2, mle2, mle2, mle2 },
                new CondensedMeliorEssence().getItem(),
                StationType.AGRONOMY_STATION
        ));

        // Superior Essence: 8x Condensed Melior surrounding 1x Melior Essence
        ItemStack cmle = new CondensedMeliorEssence().getItem();
        ItemStack mle3 = new MeliorEssence().getItem();
        RecipeManager.registerRecipe(new ShapedCustomRecipe(
                new ItemStack[]{ cmle, cmle, cmle, cmle, mle3, cmle, cmle, cmle, cmle },
                new SuperiorEssence().getItem(),
                StationType.AGRONOMY_STATION
        ));

        // Superior Seed: corners=CondensedMelior, cardinals=Superior, center=MeliorSeed
        ItemStack se   = new SuperiorEssence().getItem();
        ItemStack mls2 = new MeliorSeed().getItem();
        RecipeManager.registerRecipe(new ShapedCustomRecipe(
                new ItemStack[]{ cmle, se, cmle, se, mls2, se, cmle, se, cmle },
                new SuperiorSeed().getItem(),
                StationType.AGRONOMY_STATION
        ));

        // Condensed Superior Essence: 9x Superior Essence
        ItemStack se2  = new SuperiorEssence().getItem();
        RecipeManager.registerRecipe(new ShapedCustomRecipe(
                new ItemStack[]{ se2, se2, se2, se2, se2, se2, se2, se2, se2 },
                new CondensedSuperiorEssence().getItem(),
                StationType.AGRONOMY_STATION
        ));

        // Altior Essence: 8x Condensed Superior surrounding 1x Superior Essence
        ItemStack cse  = new CondensedSuperiorEssence().getItem();
        ItemStack se3  = new SuperiorEssence().getItem();
        RecipeManager.registerRecipe(new ShapedCustomRecipe(
                new ItemStack[]{ cse, cse, cse, cse, se3, cse, cse, cse, cse },
                new AltiorEssence().getItem(),
                StationType.AGRONOMY_STATION
        ));

        // Altior Seed: corners=CondensedSuperior, cardinals=Altior, center=SuperiorSeed
        ItemStack ae   = new AltiorEssence().getItem();
        ItemStack ss2  = new SuperiorSeed().getItem();
        RecipeManager.registerRecipe(new ShapedCustomRecipe(
                new ItemStack[]{ cse, ae, cse, ae, ss2, ae, cse, ae, cse },
                new AltiorSeed().getItem(),
                StationType.AGRONOMY_STATION
        ));

        // Condensed Altior Essence: 9x Altior Essence
        ItemStack ae2  = new AltiorEssence().getItem();
        RecipeManager.registerRecipe(new ShapedCustomRecipe(
                new ItemStack[]{ ae2, ae2, ae2, ae2, ae2, ae2, ae2, ae2, ae2 },
                new CondensedAltiorEssence().getItem(),
                StationType.AGRONOMY_STATION
        ));

    }
}
