package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class RectangularPrism {
	//Returns a list of blocks that represent a rectangular prism
	//Uses the Square class to generate the list of blocks
	public static final ArrayList<Block> get(Block block, int radius, int height) {
		ArrayList<Block> rectangularPrism = new ArrayList<Block>((int) (Math.pow(radius * 2 + 1, 2) * height));
		
		for(int distance = 0; distance < height; distance++) {
			for(Block squareBlock : Square.get(block.getRelative(BlockFace.UP, distance), radius)) {
				rectangularPrism.add(squareBlock);
			}
		}
		
		return rectangularPrism;
	}
}
