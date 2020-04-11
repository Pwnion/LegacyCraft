package com.pwnion.legacycraft.quests;

import org.bukkit.World;

public class LocationData {
	
	World world;
	double x;
	double y;
	double z;
	
	double distance;
	
	LocationData(World world, double x, double y, double z, double distance) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.distance = distance;
	}
	
	public String serialise() {
		return world.getName() + "|" + x + "|" + y + "|" + z + "|" + distance;  
	}
}
