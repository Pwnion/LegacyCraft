package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import com.pwnion.legacycraft.abilities.Util;

public class Circle {
	public static final HashSet<Block> get(World world, int x, int y, int z, int radius) {
		return get(new Location(world, x, y, z), radius);
	}
	
	public static final HashSet<Block> get(Location loc, int radius) {
		HashSet<Block> circle = new HashSet<Block>();
		if(radius == 0) {
			return circle;
		}
		
		for (double a = 0; a < 2 * Math.PI; a += (Math.PI) / (radius * 4)) {
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
		
        return circle;
    }
	
	//Gets the block at a location plus x and z coords
	private static Block blockAt(Location loc, int plusX, int plusZ) {
		return loc.toBlockLocation().add(plusX, 0, plusZ).getBlock();
	}
	
	//Gets a circle outline
	public static final ArrayList<BlockVector> get(int radius, Vector axis) {
		ArrayList<BlockVector> circle = new ArrayList<BlockVector>();
		int steps = (int) Math.ceil(2 * Math.PI * radius);
		Vector pointer = Util.vectorCalc(Util.getYaw(axis), Util.getPitch(axis) + 90, radius);
		
		for(double circleRot = 0; circleRot < 360; circleRot += 360 / steps) {
			circle.add(pointer.clone().rotateAroundAxis(axis, circleRot).toBlockVector());
		}
		
        return circle;
    }
}
