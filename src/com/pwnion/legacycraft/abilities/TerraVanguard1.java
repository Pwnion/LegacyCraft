package com.pwnion.legacycraft.abilities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.abilities.areas.RectangularPrism;
import com.pwnion.legacycraft.abilities.areas.Square;

public class TerraVanguard1 {
	private Player p;
	
	public TerraVanguard1(Player p) {
		this.p = p;
	}
	
	final int height = 10;
	public String activate(int radius) {
		Block startingBlock = p.getLocation().getBlock();
		
		if(!p.getLocation().getBlock().getRelative(BlockFace.DOWN, 1).getType().isSolid()) {
			return ChatColor.RED + "Stand on Solid Ground!";
		}
		
		ArrayList<Block> safetyRectangularPrism = new RectangularPrism(p.getLocation().getBlock().getRelative(BlockFace.UP, 1)).get(0, 4);
		safetyRectangularPrism.addAll(new RectangularPrism(p.getLocation().getBlock().getRelative(BlockFace.UP, 6)).get(1, 10));
		for(Block block : safetyRectangularPrism) {
			if(block.getType().isSolid()) {
				return ChatColor.RED + "Surrounding Area not Clear!";
			}
		}
		
		ArrayList<Block> rectangularPrism = new RectangularPrism(p.getLocation().getBlock()).get(radius, height);
		ArrayList<Block> groundBlocks = new Square(p.getLocation().getBlock().getRelative(BlockFace.DOWN, 1)).get(radius);
		ArrayList<Block> createdBlocks = new ArrayList<Block>();
		ArrayList<Block> notSolidBlocks = new ArrayList<Block>();
		ArrayList<Block> existingBlocks = new ArrayList<Block>();
		
		p.setAllowFlight(false);
		
		BukkitTask velocityTask = Bukkit.getServer().getScheduler().runTaskTimer(LegacyCraft.getPlugin(), new Runnable() {
            @Override
            public void run() {
            	p.setVelocity(new Vector(-p.getVelocity().getX(), p.getVelocity().getY(), -p.getVelocity().getZ()));
            }
        }, 0, 0);
		
		BukkitTask particleTask = Bukkit.getServer().getScheduler().runTaskTimer(LegacyCraft.getPlugin(), new Runnable() {
            @Override
            public void run() {
            	ArrayList<Block> blocks = new Square(p.getLocation().getBlock()).get(4);
        		for(Block block : blocks) {
        			p.getWorld().spawnParticle(Particle.BLOCK_DUST, block.getLocation(), 2, 0, 0, 0, 1000, block.getRelative(BlockFace.DOWN, 1).getBlockData(), true);
        			
        			p.getWorld().playSound(block.getRelative(BlockFace.DOWN).getLocation(), block.getRelative(BlockFace.DOWN).getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 0.2f, 1f);
        		}
            }
        }, 0, 0);
		
		ArrayList<Block> checkSafetyBlocks = new RectangularPrism(p.getLocation().getBlock()).get(radius + 3, 10);
		for(Entity e : p.getNearbyEntities(7, 7, 10)) {
			if(checkSafetyBlocks.contains(e.getLocation().getBlock())) {
				Vector launch = p.getLocation().toVector().subtract(e.getLocation().toVector());
				launch.multiply(-((1 / launch.length()) * 1.5));
				launch.setY(0.8);
				
				e.setVelocity(e.getVelocity().add(launch));
			}
		}
		
		final int squareSize = rectangularPrism.size() / height;
		for(int i = 0; i < height; i++) {
			int iThread = i;
			
			Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
			    public void run() {
			    	for(int j = 0; j < squareSize; j++) {
			    		Block block = rectangularPrism.get(j + (iThread * squareSize));
			    		
		    			if(!groundBlocks.get(j).getType().isSolid()) {
							notSolidBlocks.add(block);
						} else if(!block.getType().isAir()) {
							existingBlocks.add(block);
						} else {
							createdBlocks.add(block);
							block.setType(groundBlocks.get(j).getType(), true);
							
							p.getWorld().playSound(block.getLocation(), block.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 0.2f, 1f);

							Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
							    public void run() {
							    	if(!notSolidBlocks.contains(block) || !existingBlocks.contains(block)) {
							    		p.getWorld().playSound(block.getLocation(), block.getSoundGroup().getBreakSound(), SoundCategory.BLOCKS, 0.2f, 1f);
							    		
							    		block.setType(Material.AIR, true);
							    	}
							    	
							    	if(createdBlocks.contains(p.getLocation().getBlock())) {
							    		p.setFallDistance(0);
							    	}
							    }
							}, 160 + ((10 - (iThread + 1)) * 4));
						}
			    	}
			    	
			    	for(Entity e : p.getNearbyEntities(4, 4, 12)) {
			    		if(createdBlocks.contains(e.getLocation().getBlock()) && !e.getUniqueId().equals(p.getUniqueId())) {
			    			for(int i = 0; i < 10; i++) {
			    				ArrayList<Block> checkSafetyBlocks = new RectangularPrism(e.getLocation().getBlock()).get(radius + i, i + 2);
			    				for(Block block : checkSafetyBlocks) {
			    					boolean safe = true;
			    					for(Block surroundingBlock : new RectangularPrism(block).get(7, 2)) {
			    						if(surroundingBlock.getType().isSolid()) {
			    							safe = false;
			    							break;
			    						}
			    					}
			    					if(safe) {
			    						e.teleport(block.getLocation());
			    						i = 10;
			    						break;
			    					}
			    				}
			    			}
			    		}
			    	}
			    	
			    	switch(iThread) {
			    	case 0:
			    		p.setVelocity(new Vector(0, 1.5, 0));
			    		particleTask.cancel();
			    		break;
			    	case 9:
			    		if(createdBlocks.contains(p.getLocation().getBlock())) {
			    			Location loc = startingBlock.getLocation();
			    			loc.add(0, 10, 0);
			    			loc.setPitch(p.getLocation().getPitch());
			    			loc.setYaw(p.getLocation().getYaw());
			    			
			    			p.teleport(loc);
				    	}
			    		
			    		p.setFallDistance(0);
			    		
			    		velocityTask.cancel();
			    		break;
			    	}
			    }
			}, 20 + (iThread + 1) * 2);
		}
		
		return ChatColor.DARK_GREEN + "Casted Earth Pillar!";
	}
}
