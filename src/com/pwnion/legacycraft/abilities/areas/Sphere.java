package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

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
    
    public static final HashSet<BlockVector> generate(int radius, boolean hollow) {
    		HashSet<BlockVector> sphere = new HashSet<BlockVector>();
    
    		Vector axis = new Vector(0, radius, 0);
    		ArrayList<BlockVector> circle = Circle.get(radius, axis);
    		
    		for(BlockVector newAxis : circle) {
    			sphere.addAll(Circle.get(radius, newAxis));
    		}
    		
    		if(!hollow) {
    			HashSet<BlockVector> sphereInside = new HashSet<BlockVector>();
    			for(BlockVector point : sphere) {
    				for(int i = radius - 1; i > 0; i -= 1) {
    					sphereInside.add(point.clone().normalize().multiply(i).toBlockVector());
    				}
    			}
    			sphereInside.add(new BlockVector(0, 0, 0));
    			sphere.addAll(sphereInside);
    		}
    		
    		return sphere;
    }
}
