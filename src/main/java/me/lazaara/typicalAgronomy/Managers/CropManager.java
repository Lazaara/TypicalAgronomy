package me.lazaara.typicalAgronomy.Managers;

import org.bukkit.Location;

import java.util.HashMap;

public class CropManager {

    // Maps the location of a planted crop to the custom seed item ID
    public static final HashMap<Location, Integer> trackedCrops = new HashMap<>();

}