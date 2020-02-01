package com.pwnion.legacycraft.abilities.proficiencies;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.LegacyCraft;

public class ArcticVanguardProficiency1 {
	private static final ArrayList<Block> icecube = new ArrayList<Block>();
	int time = 50;
	int delay = 2;
	int radius = 5;
	
	public void activate(Player p) {
		
	}
	
	public void ChangeBlocksToIce(ArrayList<Block> blocks, int time) {
		Material originalMaterial = block.getType();
		block.setType(Material.ICE);
		Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
		    public void run() {
		    	block.setType(originalMaterial);
		    }
		}, time);
	}
}
