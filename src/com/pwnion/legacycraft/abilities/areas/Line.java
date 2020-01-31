package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

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
}
