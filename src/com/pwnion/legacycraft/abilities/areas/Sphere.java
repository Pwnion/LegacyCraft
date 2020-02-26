package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import com.pwnion.legacycraft.Util;

public class Sphere {
	
	//HashMap<radius, relativeSphere>
	static HashMap<Integer, HashSet<Vector>> savedSolid = new HashMap<Integer, HashSet<Vector>>();
	static HashMap<Integer, HashSet<Vector>> savedHollow = new HashMap<Integer, HashSet<Vector>>();
	
	public static final HashSet<Block> get(Location centre, int radius, boolean hollow) {
		centre = centre.toBlockLocation();
		
		HashSet<Block> sphere = new HashSet<Block>();
		
		HashSet<Vector> relativeSphere = new HashSet<Vector>();
		if(isSaved(radius, hollow)) {
			relativeSphere = getSaved(radius, hollow);
		} else {
			relativeSphere = generate(radius, hollow);
		}
		
		for(Vector vec : relativeSphere) {
			sphere.add(centre.clone().add(vec).getBlock());
		}
		
        return sphere;
    }
	
	private static final boolean isSaved(int radius, boolean hollow) {
		if(hollow) {
			return savedHollow.containsKey(radius);
		} else {
			return savedSolid.containsKey(radius);
		}
	}
	
	private static final HashSet<Vector> getSaved(int radius, boolean hollow) {
		if(hollow) {
			return savedHollow.get(radius);
		} else {
			return savedSolid.get(radius);
		}
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
    			savedSolid.put(radius, sphere);
    		} else {
    			savedHollow.put(radius, sphere);
    		}
    		
    		return sphere;
    }
}
