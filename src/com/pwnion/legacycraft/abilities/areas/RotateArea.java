package com.pwnion.legacycraft.abilities.areas;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import com.pwnion.legacycraft.Util;

public class RotateArea {
	
	//angle is in degrees
	public static final HashSet<Location> LocArea(Location centre, Collection<Location> area, Vector axis, double angle) {
		HashSet<Vector> relativeArea = Util.getRelativeVecArea(centre, area);
		HashSet<Location> rotatedArea = new HashSet<Location>();
		angle = Math.toRadians(angle);
		
		for(Vector vec : relativeArea) {
			Vector rotated = vec.rotateAroundAxis(axis, angle);
			rotatedArea.add(rotated.toLocation(centre.getWorld()).add(centre));
		}
		
		return rotatedArea;
	}

	//Please note that this may not work very well if not done in multiples of 90
	public static final HashSet<Block> BlockArea(Location centre, Collection<Block> area, Vector axis, double angle) {
		return Util.getBlocks(LocArea(centre, Util.getLocations(area), axis, angle));
	}
}
