package com.pwnion.legacycraft.abilities.proficiencies;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.abilities.areas.RectangularPrism;
import com.pwnion.legacycraft.abilities.areas.Selection;

public class ArcticVanguardProficiency1 {
	private static final ArrayList<ArrayList<String>> iceBlockLists = getIceBlockLists("iceblock", 3);
	private static final ArrayList<ArrayList<String>> getIceBlockLists(String namePrefix, int num) {
		final ArrayList<ArrayList<String>> iceBlocks = new ArrayList<ArrayList<String>>(num);
		for(int i = 1; i < num + 1; i++) {
			iceBlocks.add(Selection.load(namePrefix + String.valueOf(i)));
		}
		return iceBlocks;
	}
	
	public static final String activate(Player p) {
		int time = 20 * 10;
		int delay = 10;
		
		Location centre = p.getLocation();
		
		ArrayList<Block> safetyRectangularPrism = RectangularPrism.get(centre.getBlock(), 1, 4);
		for(Block block : safetyRectangularPrism) {
			if(block.getType().isSolid()) {
				return ChatColor.RED + "Surrounding Area not Clear!";
			}
		}
		
		p.teleport(centre.toCenterLocation());
		
		ArrayList<Block> changing = new ArrayList<Block>();
		for(int i = 0; i < iceBlockLists.size(); i++) {
			changing.addAll(ChangeBlocksToIce(centre, iceBlockLists.get(i), delay * (i + 1)));
		}
		
		final ArrayList<Block> changed = changing;
		Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
			public void run() {
				for(Block block : changed) {
					block.setType(Material.AIR);
				}
			}
		}, time);
		
		return ChatColor.DARK_GREEN + "Casted Ice Block!";
	}
	
	private static final ArrayList<Block> ChangeBlocksToIce(Location centre, ArrayList<String> blocksAsString, int delay) {
		ArrayList<Block> changed = new ArrayList<Block>();
		HashMap<Block, Material> blocks = new HashMap<Block, Material>();
		
		for(String dataS : blocksAsString) {
			String data[] = dataS.split(",");
			Location loc = new Location(centre.getWorld(), Float.valueOf(data[0]) - 1, Float.valueOf(data[1]), Float.valueOf(data[2]) - 1);
			Material mat = Material.getMaterial(data[3]);
			loc.add(centre);
			Block block = loc.getBlock();
			if(block.isEmpty()) {
				changed.add(block);
			}
			blocks.put(loc.getBlock(), mat);
		}
		
		Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
		    public void run() {
		    	for (Block block : changed) {
		    		block.setType(blocks.get(block));
		    	}
		    }
		}, delay);
		return changed;
	}
}
