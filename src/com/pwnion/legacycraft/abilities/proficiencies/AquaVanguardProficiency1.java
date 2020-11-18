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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.abilities.HotbarAbility;
import com.pwnion.legacycraft.abilities.areas.Selection;

public class AquaVanguardProficiency1 extends HotbarAbility {
	
	@Override
	public final String activate(Player p) {
		int time = 20 * 10;
		int delay = 4;
		
		final Location centre = p.getLocation().toBlockLocation();
		World w = p.getWorld();
		
		//Player must have a solid block 1 block below them
		if(!centre.clone().add(0, -1, 0).getBlock().getType().isSolid()) {
			return ChatColor.RED + "Stand on Solid Ground!";
		}
		
		/* Not sure if required
		//Player must have a 3x3 cube centred on their head non-solid
		ArrayList<Block> safetyRectangularPrism = RectangularPrism.get(centre.getBlock(), 1, 3);
		for(Block block : safetyRectangularPrism) {
			if(block.getType().isSolid()) {
				return ChatColor.RED + "Surrounding Area not Clear!";
			}
		} //*/
		
		//TODO: Not finished
		p.teleport(centre.toCenterLocation());
		p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, time, 2 /* Amplifier */, true /* Ambient */, false /* Particles */), true /* Forced */); //Regeneration 2 for 'time' ticks, no particles, forced.
		
		//Creates iceblock
		final ArrayList<HashMap<Location, Material>> iceBlockLists = getIceBlockLists(3);
		HashSet<Block> changing = new HashSet<Block>();
		for(int i = 0; i < iceBlockLists.size(); i++) {
			Util.br("i = " + i);
			if(iceBlockLists.get(i).size() > 0) {
				changing.addAll(ChangeBlocksToIce(centre, iceBlockLists.get(i), delay * i));
			}
		}
		
		Util.br("Changed");
		
		final HashSet<Block> changed = changing;
		
		//Sets the changed blocks back to air
		Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
			public void run() {
				for(Block block : changed) {
					w.spawnParticle(Particle.BLOCK_DUST, block.getLocation(), 20, 1, 1, 1, 0, block.getBlockData(), true);
		    		w.playSound(block.getLocation(), block.getSoundGroup().getBreakSound(), 0.05f, 1);
					block.setType(Material.AIR);
				}
			}
		}, time);
		
		cooldown(p, HotbarAbility.Type.PROFICIENCY1, 40);
		return ChatColor.DARK_GREEN + "Casted Ice Block!";
	}

	//splits the iceblock file into the requested amount of HashMaps 
	private static final ArrayList<HashMap<Location, Material>> getIceBlockLists(int num) {
		
		final ArrayList<String> iceblockUnproccessed = Selection.load("iceblock");
		
		HashMap<Location, Material> iceblockFull = new HashMap<Location, Material>(iceblockUnproccessed.size());
		HashMap<Location, Double> distances = new HashMap<Location, Double>(iceblockUnproccessed.size());
		
		double furthest = 0;
		
		//dataS should be formatted: x,y,z,Material
		// e.g:  1,1,1,ICE
		for(String dataS : iceblockUnproccessed) {
			String data[] = dataS.split(",");
			Location loc = new Location(null, Float.valueOf(data[0]), Float.valueOf(data[1]), Float.valueOf(data[2]));
			Material mat = Material.valueOf(data[3]);
			iceblockFull.put(loc, mat);

			//Gets the distance from either the head or the feet whichever is closest
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
	
private static Set<Block> ChangeBlocksToIce(Location centre, HashMap<Location, Material> blocks, int delay) {
		HashMap<Block, Material> changed = new HashMap<Block, Material>();
		World w = centre.getWorld();

		Util.br(blocks);
		
		for(Location loc : blocks.keySet()) {
			Material mat = blocks.get(loc); //TODO: Returns null the second time?
			Util.br(mat); 
			loc.setWorld(w);
			Block block = loc.clone().add(centre).getBlock();
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
