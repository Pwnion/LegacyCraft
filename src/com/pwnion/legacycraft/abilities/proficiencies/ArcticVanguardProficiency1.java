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
	private static final HashMap<String, String> iceblock1 = Selection.load("iceblock1");
	private static final HashMap<String, String> iceblock2 = Selection.load("iceblock2");
	private static final HashMap<String, String> iceblock3 = Selection.load("iceblock3");
	
	public static final String activate(Player p) {
		int time = 50;
		int delay = 2;
		
		Location centre = p.getLocation();
		
		ArrayList<Block> safetyRectangularPrism = RectangularPrism.get(centre.getBlock(), 1, 4);
		for(Block block : safetyRectangularPrism) {
			if(block.getType().isSolid()) {
				return ChatColor.RED + "Surrounding Area not Clear!";
			}
		}
		
		ArrayList<Block> changing = new ArrayList<Block>();
		
		changing = ChangeBlocksToIce(centre, iceblock1, delay);
		changing.addAll(ChangeBlocksToIce(centre, iceblock2, delay * 2));
		changing.addAll(ChangeBlocksToIce(centre, iceblock3, delay * 3));
		
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
	
	private static final ArrayList<Block> ChangeBlocksToIce(Location centre, HashMap<String, String> blocks, int delay) {
		ArrayList<Block> changed = new ArrayList<Block>();
		
		for(String locString : blocks.keySet()) {
			String xyz[] = locString.split(",");
			Location loc = new Location(centre.getWorld(), Float.valueOf(xyz[1]), Float.valueOf(xyz[2]), Float.valueOf(xyz[3]));
			loc.add(centre);
			Block block = loc.getBlock();
			if(block.isEmpty()) {
				changed.add(block);
			}
		}
		
		Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
		    public void run() {
		    	for (Block block : changed) {
		    		block.setType(Material.getMaterial(blocks.get(LocationToString(block.getLocation().add(centre)))));
		    	}
		    }
		}, delay);
		return changed;
	}
	
	private static String LocationToString(Location loc) {
    	return loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }
}
