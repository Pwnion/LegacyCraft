package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class Circle {
	public static ArrayList<Block> get (Location loc, int radius, Boolean hollow) {
		ArrayList<Location> circleloc = new ArrayList<Location>();
		ArrayList<Block> circle = new ArrayList<Block>();
        int centreX = loc.getBlockX();
        int centreY = loc.getBlockY();
        int centreZ = loc.getBlockZ();
        for (int x = centreX - radius; x <= centreX + radius; x++) {
            for (int z = centreZ - radius; z <= centreZ + radius; z++) {
                double dist = Math.pow((centreX - x), 2) + Math.pow((centreZ - z), 2);
                if (dist < radius * radius && !(hollow && dist < (radius - 1) * (radius - 1))) {
                	Location l = new Location(loc.getWorld(), x, centreY, z);
                	circleloc.add(l);
                }
            }
        }
        for(Location l: circleloc) {
        	circle.add(l.getBlock());
        }
        return circle;
    }
}
