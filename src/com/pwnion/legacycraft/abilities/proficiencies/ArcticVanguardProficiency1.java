package com.pwnion.legacycraft.abilities.proficiencies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.abilities.areas.RectangularPrism;
import com.pwnion.legacycraft.abilities.areas.Selection;

public class ArcticVanguardProficiency1 {
	//private static final ArrayList<HashMap<Location, Material>> iceBlockLists = getIceBlockLists(3);
	
	//splits the iceblock file into the requested amount of HashMaps 
	private static final ArrayList<HashMap<Location, Material>> getIceBlockLists(int num) {
		final ArrayList<String> iceblockUnproccessed = Selection.load("iceblock");
		
		HashMap<Location, Material> iceblockFull = new HashMap<Location, Material>(iceblockUnproccessed.size());
		HashMap<Location, Double> distances = new HashMap<Location, Double>(iceblockUnproccessed.size());
		double furthest = 0;
		
		for(String dataS : iceblockUnproccessed) {
			String data[] = dataS.split(",");
			Location loc = new Location(null, Float.valueOf(data[0]), Float.valueOf(data[1]), Float.valueOf(data[2]));
			Material mat = Material.valueOf(data[3]);
			iceblockFull.put(loc, mat);

			double dist;
			if(loc.getBlockY() > 0) {
				dist = distance(loc, 0, 1, 0);
			} else {
				dist = distance(loc, 0, 0, 0);
			}
			
			distances.put(loc, dist);
			
			if(dist > furthest) {
				furthest = dist;
			}
		}
		
		ArrayList<HashMap<Location, Material>> iceblockLists = new ArrayList<HashMap<Location, Material>>();
		ArrayList<Location> checked = new ArrayList<Location>();
		double split = furthest / num;
		for(int i = 1; i <= num; i++) {
			HashMap<Location, Material> iceblockSplit = new HashMap<Location, Material>();
			for(Location loc : iceblockFull.keySet()) {
				double dist = distances.get(loc);
				if(dist < (split * i) && !checked.contains(loc)) {
					iceblockSplit.put(loc, iceblockFull.get(loc));
					
					checked.add(loc);
				}
			}
			iceblockLists.add(iceblockSplit);
		}
		
		return iceblockLists;
	}
	
	
	
	public static final String activate(Player p) {
		int time = 20 * 10;
		int delay = 4;
		
		Location centre = p.getLocation().toBlockLocation();
		World w = p.getWorld();
		
		if(!centre.clone().add(0, -1, 0).getBlock().getType().isSolid()) {
			return ChatColor.RED + "Stand on Solid Ground!";
		}
		
		ArrayList<Block> safetyRectangularPrism = RectangularPrism.get(centre.getBlock(), 1, 3);
		for(Block block : safetyRectangularPrism) {
			if(block.getType().isSolid()) {
				return ChatColor.RED + "Surrounding Area not Clear!";
			}
		}
		
		p.teleport(centre.toCenterLocation());
		//w.spawnParticle(Particle.BLOCK_DUST, centre, 1000, 2, 2, 2, 0, Material.SNOW_BLOCK.createBlockData(), true);
		
		final ArrayList<HashMap<Location, Material>> iceBlockLists = getIceBlockLists(3);
		HashSet<Block> changing = new HashSet<Block>();
		for(int i = 0; i < iceBlockLists.size(); i++) {
			if(iceBlockLists.get(i).size() > 0) {
				changing.addAll(ChangeBlocksToIce(centre, iceBlockLists.get(i), delay * (i)));
			}
		}
		
		final HashSet<Block> changed = changing;
		Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
			public void run() {
				for(Block block : changed) {
					w.spawnParticle(Particle.BLOCK_DUST, block.getLocation(), 20, 1, 1, 1, 0, block.getBlockData(), true);
		    		w.playSound(block.getLocation(), block.getSoundGroup().getBreakSound(), 0.05f, 1);
					block.setType(Material.AIR);
				}
			}
		}, time);
		
		return ChatColor.DARK_GREEN + "Casted Ice Block!";
	}
	
	private static final Set<Block> ChangeBlocksToIce(Location centre, HashMap<Location, Material> blocks, int delay) {
		
		HashMap<Block, Material> changed = new HashMap<Block, Material>();
		World w = centre.getWorld();

		for(Location loc : blocks.keySet()) {
			Material mat = blocks.get(loc);
			loc.setWorld(w);
			loc.add(centre);
			Block block = loc.getBlock();
			if(block.isEmpty()) {
				changed.put(block, mat);
			}
		}
		
		Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
		    public void run() {
		    	for (Block block : changed.keySet()) {
		    		//Change Air blocks to Ice
		    		block.setType(changed.get(block));
		    		w.spawnParticle(Particle.BLOCK_DUST, block.getLocation(), 20, 1, 1, 1, 0, block.getBlockData(), true);
		    		w.playSound(block.getLocation(), block.getSoundGroup().getPlaceSound(), (float) 0.2, 1);
		    	}
		    }
		}, delay);
		return changed.keySet();
	}
	
	private static double distance(Location loc1, Location loc2) {
		return Math.sqrt(Math.pow(loc1.getX() - loc2.getX(), 2) + Math.pow(loc1.getY() - loc2.getY(), 2) + Math.pow(loc1.getZ() - loc2.getZ(), 2));
	}
	
	private static double distance(Location loc1, int x2, int y2, int z2) {
		return distance(loc1, new Location(null, x2, y2, z2));
	}

}
