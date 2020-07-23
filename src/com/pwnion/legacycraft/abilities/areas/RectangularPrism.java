package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.BlockVector;

import com.pwnion.legacycraft.Util;

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
	
	public static final ArrayList<Block> get(Block block1, Block block2) {
		if(block1.getWorld() != block2.getWorld()) {
			return null;
		}
		ArrayList<Block> rectangularPrism = new ArrayList<Block>();
		
		Location pos1 = block1.getLocation();
		Location pos2 = block1.getLocation().clone().subtract(block2.getLocation());
		Location temp;
		
		for(int x = 0; x <= pos2.getBlockX(); x++) {
			for(int y = 0; y <= pos2.getBlockY(); y++) {
				for(int z = 0; z <= pos2.getBlockZ(); z++) {
					temp = new Location(block1.getWorld(), x, y, z);
					temp.add(pos1);
					rectangularPrism.add(temp.getBlock());
				}
			}
		}
		
		return rectangularPrism;
	}
	
	public static final HashSet<BlockVector> get(int radius) {
		HashSet<BlockVector> cube = new HashSet<BlockVector>((int) (Math.pow(radius * 2 + 1, 3)));
		
		for(int distance = -radius; distance <= radius; distance++) {
			for(BlockVector vec : Square.get(radius)) {
				cube.add(vec);
			}
		}
		
		return cube;
	}
}
