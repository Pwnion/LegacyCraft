package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Square {
	private Block block;
	
	public Square(Block block) {
		this.block = block;
	}
	
	public ArrayList<Block> get(int radius) {
		ArrayList<Block> square = new ArrayList<Block>((int) Math.pow(radius * 2 + 1, 2));
		
		Block startingBlock = block.getRelative(BlockFace.EAST, radius);
		
		for(int distance = 0; distance < radius * 2 + 1; distance++) {
			for(Block lineBlock : new Line(startingBlock.getRelative(BlockFace.WEST, distance)).get(BlockFace.NORTH, radius * 2 + 1, true)) {
				square.add(lineBlock);
			}
		}
		
		return square;
	}
}
