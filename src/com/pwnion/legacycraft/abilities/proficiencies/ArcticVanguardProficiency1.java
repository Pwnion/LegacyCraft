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

public class ArcticVanguardProficiency1 {
	private static final HashMap<Location, Material> icecubeFinal = new HashMap<Location, Material>();

	private static final HashMap<Location, Material> icecube1 = new HashMap<Location, Material>();
	private static final HashMap<Location, Material> icecube2 = new HashMap<Location, Material>();
	private static final HashMap<Location, Material> icecube3 = new HashMap<Location, Material>();
	private static final HashMap<Location, Material> icecube4 = new HashMap<Location, Material>();
	
	
	public String activate(Player p) {
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
		
		changing = ChangeBlocksToIce(centre, icecube1, time);
		changing.addAll(ChangeBlocksToIce(centre, icecube2, delay * 2));
		changing.addAll(ChangeBlocksToIce(centre, icecube3, delay * 3));
		changing.addAll(ChangeBlocksToIce(centre, icecube4, delay * 4));
		
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
	
	public ArrayList<Block> ChangeBlocksToIce(Location centre, HashMap<Location, Material> blocks, int delay) {
		ArrayList<Block> changed = new ArrayList<Block>();
		
		for(Location loc : blocks.keySet()) {
			loc.add(centre);
			Block block = loc.getBlock();
			if(block.isEmpty()) {
				changed.add(block);
			}
		}
		
		Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
		    public void run() {
		    	for (Block block : changed) {
		    		block.setType(blocks.get(block.getLocation().add(centre)));
		    	}
		    }
		}, delay);
		return changed;
	}
}
