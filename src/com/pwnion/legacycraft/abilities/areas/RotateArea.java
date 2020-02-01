package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class RotateArea {
	public static ArrayList<Block> get(Location centre, ArrayList<Block> area, String type) {
		ArrayList<Block> newArea = new ArrayList<Block>();
		for (Block block : area) {
			newArea.add(RotateLoc(centre, block.getLocation(), type).getBlock());
		}
		return newArea;
	}
	
	private static Location RotateLoc(Location centre, Location pos, String type) {
		if(centre.getWorld() != pos.getWorld()) {
			return null;
		}
		pos = getRelativeLoc(centre, pos);
		int Rx = pos.getBlockX();
		int Ry = pos.getBlockY();
		int Rz = pos.getBlockZ();
		switch(type) {
		case "sphere":
			Rx = pos.getBlockZ();
			Ry = pos.getBlockX();
			Rz = pos.getBlockY();
			break;
		}
		int x = Rx + centre.getBlockX();
		int y = Ry + centre.getBlockY();
		int z = Rz + centre.getBlockZ();
		return new Location(centre.getWorld(), x, y, z);
	}

	private static Location getRelativeLoc(Location centre, Location pos) {
		if(centre.getWorld() != pos.getWorld()) {
			return null;
		}
		
		int x = pos.getBlockX() - centre.getBlockX();
		int y = pos.getBlockY() - centre.getBlockY();
		int z = pos.getBlockZ() - centre.getBlockZ();
		return new Location(centre.getWorld(), x, y, z);
	}
}
