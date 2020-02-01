package com.pwnion.legacycraft.abilities.areas;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class Sphere {
	public static HashSet<Block> get(Location pos, int radius) {
		HashSet<Block> sphere = new HashSet<Block>();
		HashSet<Block> temp = new HashSet<Block>();
        int cY = pos.getBlockY();
        
        for (int i = -radius; i <= radius; i += 1) {
        	   int LocalRadius = (int) (radius * Math.abs(Math.cos((Math.PI / (2 * radius)) * i)));
        	   pos.setY(cY + i);
        	   temp = Circle.get(pos, LocalRadius);
        	   sphere.addAll(temp);
        	   sphere.addAll(RotateArea.sphere(pos, temp));
        }

        return sphere;
    }
}
