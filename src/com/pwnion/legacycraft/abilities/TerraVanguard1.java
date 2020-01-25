package com.pwnion.legacycraft.abilities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
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
            	ArrayList<Block> blocks = new Square(p.getLocation().getBlock()).get(3);
        		for(Block block : blocks) {
        			p.getWorld().spawnParticle(Particle.BLOCK_DUST, block.getLocation(), 2, 0, 0, 0, 1000, block.getRelative(BlockFace.DOWN, 1).getBlockData(), true);
        		}
            }
        }, 0, 0);
		
		ArrayList<Block> checkSafetyBlocks = new RectangularPrism(p.getLocation().getBlock()).get(radius + 1, 10);
		for(Entity e : p.getNearbyEntities(5, 5, 10)) {
			if(checkSafetyBlocks.contains(e.getLocation().getBlock())) {
				Vector launch = e.getLocation().toVector().subtract(p.getLocation().toVector());
				launch.add(new Vector(-launch.getX() * 2, 1, -launch.getZ() * 2));
				
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
							
							Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
							    public void run() {
							    	if(!notSolidBlocks.contains(block) || !existingBlocks.contains(block)) {
							    		block.setType(Material.AIR, true);
							    	}
							    	
							    	if(createdBlocks.contains(p.getLocation().getBlock())) {
							    		p.setFallDistance(0);
							    	}
							    }
							}, 160 + ((10 - (iThread + 1)) * 4));
						}
			    	}
			    	
			    	switch(iThread) {
			    	case 0:
			    		p.setVelocity(new Vector(0, 1.5, 0));
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
			    		particleTask.cancel();
			    		break;
			    	}
			    }
			}, 20 + (iThread + 1) * 2);
		}
		
		return ChatColor.DARK_GREEN + "Casted Earth Pillar!";
	}
}
