package com.pwnion.legacycraft.abilities.proficiencies;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

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
import com.pwnion.legacycraft.abilities.HotbarAbility;
import com.pwnion.legacycraft.abilities.Movement;
import com.pwnion.legacycraft.abilities.Pathfinding;
import com.pwnion.legacycraft.abilities.areas.RectangularPrism;
import com.pwnion.legacycraft.abilities.areas.Square;

public class TerraVanguardProficiency1 extends HotbarAbility {
	private static final int height = 10;
	private static final int radius = 2;
	
	@Override
	public final String activate(Player p) {
		return this.activate(p, radius);
	}
	
	//Initiates the first Terra Vanguard proficiency for a player
	public final String activate(Player p, int radius) {
		Block startingBlock = p.getLocation().getBlock();
		
		ArrayList<Block> smallGroundBlocks = Square.get(p.getLocation().add(0D, -0.25D, 0D).getBlock(), 1);
		for(Block block : smallGroundBlocks) {
			if(!block.getType().isSolid() || !block.getType().isOccluding()) {
				return ChatColor.RED + "Stand on even ground!";
			}
		}
		
		ArrayList<Block> safetyRectangularPrism = RectangularPrism.get(p.getLocation().getBlock().getRelative(BlockFace.UP, 1), 0, 4);
		safetyRectangularPrism.addAll(RectangularPrism.get(p.getLocation().getBlock().getRelative(BlockFace.UP, 6), 1, 10));
		for(Block block : safetyRectangularPrism) {
			if(block.getType().isSolid()) {
				return ChatColor.RED + "Surrounding Area not Clear!";
			}
		}
		
		ArrayList<Block> rectangularPrism = RectangularPrism.get(p.getLocation().getBlock(), radius, height);
		ArrayList<Block> groundBlocks = Square.get(p.getLocation().getBlock().getRelative(BlockFace.DOWN, 1), radius);
		ArrayList<Block> createdBlocks = new ArrayList<Block>();
		ArrayList<Block> notSolidBlocks = new ArrayList<Block>();
		ArrayList<Block> existingBlocks = new ArrayList<Block>();
		
		p.setAllowFlight(false);
		p.setWalkSpeed(0f);
		
		BukkitTask velocityTask = Bukkit.getServer().getScheduler().runTaskTimer(LegacyCraft.getPlugin(), new Runnable() {
            @Override
            public void run() {
            	p.setVelocity(new Vector(-p.getVelocity().getX(), p.getVelocity().getY(), -p.getVelocity().getZ()));
            }
        }, 0, 0);
		
		BukkitTask particleTask = Bukkit.getServer().getScheduler().runTaskTimer(LegacyCraft.getPlugin(), new Runnable() {
            @Override
            public void run() {
            	ArrayList<Block> blocks = Square.get(p.getLocation().getBlock(), 4);
        		for(Block block : blocks) {
        			p.getWorld().spawnParticle(Particle.BLOCK_DUST, block.getLocation(), 2, 0, 0, 0, 1000, block.getRelative(BlockFace.DOWN, 1).getBlockData(), true);
        			
        			p.getWorld().playSound(block.getRelative(BlockFace.DOWN).getLocation(), block.getRelative(BlockFace.DOWN).getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 0.2f, 1f);
        		}
            }
        }, 0, 0);
		
		ArrayList<Block> checkSafetyBlocks = RectangularPrism.get(p.getLocation().getBlock(), radius + 3, 10);
		for(Entity e : p.getNearbyEntities(7, 7, 10)) {
			if(checkSafetyBlocks.contains(e.getLocation().getBlock())) {
				Movement.launch(e, p.getLocation(), 1.5f, 0.8f);
			}
		}
		
		final int squareSize = rectangularPrism.size() / height;
		for(int i = 0; i < height; i++) {
			int iThread = i;
			
			Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
			    public void run() {
			    	for(int j = 0; j < squareSize; j++) {
			    		Block block = rectangularPrism.get(j + (iThread * squareSize));
			    		
		    			if(!groundBlocks.get(j).getType().isSolid() || !groundBlocks.get(j).getType().isOccluding()) {
							notSolidBlocks.add(block);
						} else if(!block.getType().isEmpty()) {
							existingBlocks.add(block);
						} else {
							createdBlocks.add(block);
							
							Supplier<Integer> getRandomGroundBlock = () -> {
								Random rand = new Random();
								int randIndex = rand.nextInt((int) Math.pow(radius * 2 + 1, 2));
								while(!groundBlocks.get(randIndex).getType().isSolid() || !groundBlocks.get(randIndex).getType().isOccluding()) {
									randIndex = rand.nextInt((int) Math.pow(radius * 2 + 1, 2));
								}
								return randIndex;
							};

							block.setType(groundBlocks.get(getRandomGroundBlock.get()).getType(), true);
							
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
			    			e.teleport(Pathfinding.nearestSafeLocation(e.getLocation()));
			    		}
			    	}
			    	
			    	switch(iThread) {
			    	case 0:
			    		particleTask.cancel();
			    		p.setWalkSpeed(0.2f);
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
			    		break;
			    	}
			    }
			}, 20 + (iThread + 1) * 2);
		}
		
		cooldown(p, HotbarAbility.Type.PROFICIENCY1, 40);
		return ChatColor.DARK_GREEN + "Casted Earth Pillar!";
	}
}
