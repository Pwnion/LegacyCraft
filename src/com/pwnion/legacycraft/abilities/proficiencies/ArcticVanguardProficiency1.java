package com.pwnion.legacycraft.abilities.proficiencies;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.abilities.areas.RectangularPrism;
import com.pwnion.legacycraft.abilities.areas.Selection;

public class ArcticVanguardProficiency1 {
	private static final ArrayList<HashMap<Location, Material>> iceBlockLists = getIceBlockLists(3);
	
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
				iceblockLists.add(iceblockSplit);
			}
		}
		
		return iceblockLists;
	}
	
	
	
	public static final String activate(Player p) {
		int time = 20 * 10;
		int delay = 10;
		
		Location centre = p.getLocation();
		World w = p.getWorld();
		
		if(!centre.clone().add(0, -1, 0).getBlock().getType().isSolid()) {
			return ChatColor.RED + "Stand on Solid Ground!";
		}
		
		ArrayList<Block> safetyRectangularPrism = RectangularPrism.get(centre.getBlock(), 1, 4);
		for(Block block : safetyRectangularPrism) {
			if(block.getType().isSolid()) {
				return ChatColor.RED + "Surrounding Area not Clear!";
			}
		}
		
		p.teleport(centre.toCenterLocation());
		w.spawnParticle(Particle.SNOWBALL, centre, 50, 3, 3, 3);
		
		ArrayList<Block> changing = new ArrayList<Block>();
		for(int i = 0; i < iceBlockLists.size(); i++) {
			changing.addAll(ChangeBlocksToIce(centre, iceBlockLists.get(i), delay * (i + 1)));
		}
		
		final ArrayList<Block> changed = changing;
		Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
			public void run() {
				for(Block block : changed) {
					ItemStack itemCrackData = new ItemStack(block.getType());
		    		w.spawnParticle(Particle.BLOCK_CRACK, block.getLocation(), 10, itemCrackData);
					block.setType(Material.AIR);
				}
			}
		}, time);
		
		return ChatColor.DARK_GREEN + "Casted Ice Block!";
	}
	
	private static final ArrayList<Block> ChangeBlocksToIce(Location centre, HashMap<Location, Material> blocks, int delay) {
		ArrayList<Block> changed = new ArrayList<Block>();
		World w = centre.getWorld();
		
		for(Location loc : blocks.keySet()) {
			loc.setWorld(w);
			loc.add(centre);
			Block block = loc.getBlock();
			if(block.isEmpty()) {
				changed.add(block);
			}
		}
		
		Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
		    public void run() {
		    	for (Block block : changed) {
		    		//Change Air blocks to Ice
		    		block.setType(blocks.get(block.getLocation()));
		    		ItemStack itemCrackData = new ItemStack(block.getType());
		    		w.spawnParticle(Particle.BLOCK_CRACK, block.getLocation(), 20, itemCrackData);
		    	}
		    }
		}, delay);
		return changed;
	}
	
	private static double distance(Location loc1, Location loc2) {
		return Math.sqrt(Math.pow(loc1.getX() - loc2.getX(), 2) + Math.pow(loc1.getY() - loc2.getY(), 2) + Math.pow(loc1.getZ() - loc2.getZ(), 2));
	}
	
	private static double distance(Location loc1, int x2, int y2, int z2) {
		return distance(loc1, new Location(null, x2, y2, z2));
	}

}
