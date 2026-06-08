package me.lazaara.typicalAgronomy.Managers;

import org.bukkit.Location;

import java.util.HashMap;

public class CropManager {

    // Maps the location of a planted crop to its crop key (e.g. "inferior_seed")
    public static final HashMap<Location, String> trackedCrops = new HashMap<>();

}
