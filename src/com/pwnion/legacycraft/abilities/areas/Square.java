package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Square {
	//Returns a list of blocks that represent a square
	//Uses the Line class to generate the list of blocks
	public static final ArrayList<Block> get(Block block, int radius) {
		ArrayList<Block> square = new ArrayList<Block>((int) Math.pow(radius * 2 + 1, 2));
		
		Block startingBlock = block.getRelative(BlockFace.EAST, radius);
		
		for(int distance = 0; distance < radius * 2 + 1; distance++) {
			for(Block lineBlock : Line.get(startingBlock.getRelative(BlockFace.WEST, distance), BlockFace.NORTH, radius * 2 + 1, true)) {
				square.add(lineBlock);
			}
		}
		
		return square;
	}
}
