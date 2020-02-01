package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class Sphere {
	public static ArrayList<Block> get(Location pos, int radius) {
        ArrayList<Block> sphere = new ArrayList<Block>();
        Set<Block> sphereTemp = new HashSet<Block>();
        ArrayList<Block> temp = new ArrayList<Block>();
        int cY = pos.getBlockY();
        
        for (int i = -radius; i <= radius; i += 1) {
        	   int LocalRadius = (int) (radius * Math.abs(Math.cos((Math.PI / (2 * radius)) * i)));
        	   pos.setY(cY + i);
        	   temp = Circle.get(pos, LocalRadius);
        	   sphereTemp.addAll(temp);
        	   sphereTemp.addAll(RotateArea.get(pos, temp, "sphere"));
        }
        
        sphere.addAll(sphereTemp);
        return sphere;
    }
}
