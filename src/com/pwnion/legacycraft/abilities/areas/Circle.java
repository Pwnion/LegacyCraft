package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Circle {
	public static final ArrayList<Block> get(World world, int x, int y, int z, int radius) {
		return get(new Location(world, x, y, z), radius);
	}
	
	public static final ArrayList<Block> get(Location loc, int radius) {
		ArrayList<Block> circle = new ArrayList<Block>();
		if(radius == 0) {
			return circle;
		}
		
		for (double a = 0; a < 2 * Math.PI; a += (Math.PI) / (radius * 2 + 1)) {
  	      int x = (int) (Math.cos(a) * radius);
  	      int z = (int) (Math.sin(a) * radius);
  	      circle.add(blockAt(loc, x, z));
  	      for (int ix = -x; ix < x; ix++) {
  	    	circle.add(blockAt(loc, ix, z));
  	    	for (int iz = 0; iz < z; iz++) {
  	  	    	circle.add(blockAt(loc, ix, iz));
  	  	    }
  	      }
  	   }
		Set<Block> temp = new HashSet<Block>();
		temp.addAll(circle);
		circle.clear();
		circle.addAll(temp);
        return circle;
    }
	
	private static Block blockAt(Location loc, int plusX, int plusZ) {
		return loc.toBlockLocation().add(plusX, 0, plusZ).getBlock();
	}
	
	private static final ArrayList<Block> get1(Location loc, int radius, Boolean hollow) {
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
