package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import com.pwnion.legacycraft.Util;

public class Sphere {
	
	//HashMap<radius, relativeSphere>
	static HashMap<Integer, HashSet<Vector>> savedSolid = new HashMap<Integer, HashSet<Vector>>();
	static HashMap<Integer, HashSet<Vector>> savedHollow = new HashMap<Integer, HashSet<Vector>>();
	
	//prefers saved spheres
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
	
	//spawns in a stone sphere for testing purposes 
	//(this method is destructive to surrounding blocks)
	public static void spawn(Location pos, int radius, boolean hollow) {
		Util.spawnBlocks(get(pos, radius, hollow));
	}
    
    //generates a sphere relative to 0,0    uses the circle class to create a sphere out of vertically aligned circles
    //this method may be heavy if called for large radiuses
    //for large radiuses try pre-generate before user use
    //this will always return the same values for the given parameters as there is no randomness, if the sphere is deformed force generating will not fix
    public static final HashSet<Vector> generate(int radius, boolean hollow) {
    		HashSet<Vector> sphere = new HashSet<Vector>();
    
    		Vector axis = new Vector(0, radius, 0);
    		ArrayList<Vector> circle = Circle.get(radius, axis);
    		
    		for(Vector newAxis : circle) {
    			sphere.addAll(Circle.get(radius, newAxis));
    		}
    		
    		if(!hollow) {
    			HashSet<Vector> sphereInside = new HashSet<Vector>();
    			for(Vector point : sphere) {
    				sphereInside.addAll(Line.getBlockVectors(point));
    			}
    			sphereInside.add(new Vector(0, 0, 0));
    			sphere.addAll(sphereInside);
    			savedSolid.put(radius, sphere);
    		} else {
    			savedHollow.put(radius, sphere);
    		}
    		
    		return sphere;
    }
}
