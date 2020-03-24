package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import com.pwnion.legacycraft.Util;

public class Circle {
	
	public static final HashSet<Block> get(World world, int x, int y, int z, int radius) {
		return get(new Location(world, x, y, z), radius, true);
	}
	
	public static void spawn(Location pos, int radius, boolean hollow) {
		Util.spawnBlocks(get(pos, radius, hollow));
	}
	
	public static final HashSet<Block> get(Location pos, int radius, boolean hollow) {
		pos = pos.toBlockLocation();
		
		HashSet<Block> circle = new HashSet<Block>();
		ArrayList<Vector> circleVectors = get(radius, new Vector(0, 1, 0));
		
		for(Vector blockVec : circleVectors) {
			circle.add(pos.clone().add(blockVec).getBlock());
		}
		
		if(!hollow) {
			HashSet<Block> circleInside = new HashSet<Block>();
			for(Vector point : circleVectors) {
				for(int i = radius - 1; i > 0; i -= 1) {
					circleInside.add(pos.clone().add(point.clone().normalize().multiply(i).toBlockVector()).getBlock());
				}
			}
			circleInside.add(pos.clone().add(new BlockVector(0, 0, 0)).getBlock());
			circle.addAll(circleInside);
		}
		
		return circle;
	}
	
	//Gets a circle outline for any given axis
	//the amount of points on the circle is calculated by circumference (2 * pi * r)
	public static final ArrayList<Vector> get(int radius, Vector axis) {
		ArrayList<Vector> circle = new ArrayList<Vector>();
		int steps = (int) Math.ceil(2 * Math.PI * radius) * 1;
		Vector pointer = Util.vectorCalc(Util.getYaw(axis), Util.getPitch(axis) + 90, radius);
		
		for(double circleRot = 0; circleRot < 360; circleRot += 360 / steps) {
			circle.add(pointer.clone().rotateAroundAxis(axis, circleRot));
		}
		
        return circle;
    }
}
