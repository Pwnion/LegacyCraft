package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import com.pwnion.legacycraft.Util;

public class Line {
	//Returns a list of blocks that represent a line
	public static final ArrayList<Block> get(Block block, BlockFace dir, int length, boolean centred) {
		if(length % 2 == 0 && centred) return null;
		
		ArrayList<Block> line = new ArrayList<Block>(length);
		Block startingBlock = centred ? block.getRelative(dir.getOppositeFace(), (int) Math.floor(length / 2.0)) : block;
		
		for(int distance = 0; distance < length; distance++) {
			line.add(startingBlock.getRelative(dir, distance));
		}
		
		return line;
	}
	
	public static final ArrayList<Vector> get(Vector vector, double spacing) {
		double length = vector.length();
		vector.normalize();
		ArrayList<Vector> line = new ArrayList<Vector>();
		for(double i = 0; i <= length; i += spacing) {
   			line.add(vector.clone().multiply(i));
    	}
    	return line;
	}
	
	public static final HashSet<BlockVector> getBlockVectors(Vector vector) {
		return Util.approxBlocks(get(vector, 1));
	}
	
	public static final ArrayList<Block> get(Location centre, Vector vector) {
		ArrayList<Block> line = new ArrayList<Block>();
		
		for(Vector pointOnLine : get(vector, 1)) {
			line.add(centre.clone().add(pointOnLine).getBlock());
		}
		
		return line;
	}
}
