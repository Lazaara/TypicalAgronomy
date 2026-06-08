package me.lazaara.typicalAgronomy;

import me.lazaara.typicalAgronomy.Commands.TypicalAgronomyCommand;
import me.lazaara.typicalAgronomy.Listeners.CraftingStationListener;
import me.lazaara.typicalAgronomy.Listeners.CropListener;
import me.lazaara.typicalAgronomy.Listeners.ItemMenuListener;
import me.lazaara.typicalAgronomy.Listeners.RecipeMenuListener;
import me.lazaara.typicalAgronomy.Crops.CustomCrop;
import me.lazaara.typicalAgronomy.Items.CraftingStation;
import me.lazaara.typicalAgronomy.Items.InferiorEssence;
import me.lazaara.typicalAgronomy.Items.InferiorSeed;
import me.lazaara.typicalAgronomy.Managers.CropRegistry;
import me.lazaara.typicalAgronomy.Managers.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
        registerEvents();
        registerRecipes();
        registerCrops();

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
                1,
                Material.WHEAT,
                List.of(new InferiorSeed().getItem(), new InferiorEssence().getItem())
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
}
