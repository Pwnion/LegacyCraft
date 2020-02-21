package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import com.pwnion.legacycraft.Util;

public class Sphere {
	
	public static final HashSet<Block> get(Location pos, int radius, boolean hollow) {
		pos = pos.toBlockLocation();
		
		HashSet<Block> sphere = new HashSet<Block>();
		
		for(Vector blockVec : generate(radius, hollow)) {
			sphere.add(pos.clone().add(blockVec).getBlock());
		}
		
        return sphere;
    }
	
	public static void spawn(Location pos, int radius, boolean hollow) {
		Util.spawnBlocks(get(pos, radius, hollow));
	}
    
    public static final HashSet<Vector> generate(int radius, boolean hollow) {
    		HashSet<Vector> sphere = new HashSet<Vector>();
    
    		Vector axis = new Vector(0, radius, 0);
    		ArrayList<Vector> circle = Circle.get(radius, axis);
    		
    		for(Vector newAxis : circle) {
    			sphere.addAll(Circle.get(radius, newAxis));
    		}
    		
    		if(!hollow) {
    			HashSet<BlockVector> sphereInside = new HashSet<BlockVector>();
    			for(Vector point : sphere) {
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
