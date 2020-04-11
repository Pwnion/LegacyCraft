package com.pwnion.legacycraft.quests;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BlockVector;

public class LocationData {
	
    Location centre;
	HashSet<BlockVector> area;

	
	LocationData(Location centre, double radius) {
		this.centre = centre;
		
		RectangularPrism
	}
	
	LocationData() {
		
	}
	
	public String serialise() {
		return world.getName() + "|" + x + "|" + y + "|" + z + "|" + distance;  
	}
}
