package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class RectangularPrism {
	private Block block;
	
	public RectangularPrism(Block block) {
		this.block = block;
	}
	
	public ArrayList<Block> get(int radius, int height) {
		ArrayList<Block> rectangularPrism = new ArrayList<Block>((int) (Math.pow(radius * 2 + 1, 2) * height));
		
		for(int distance = 0; distance < height; distance++) {
			for(Block squareBlock : new Square(block.getRelative(BlockFace.UP, distance)).get(radius)) {
				rectangularPrism.add(squareBlock);
			}
		}
		
		return rectangularPrism;
	}
}
