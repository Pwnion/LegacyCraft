package com.pwnion.legacycraft.npcs;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class NPCHomeWork {
	
	public static Player editPlayer = null;
	private static final ArrayList<Location> transferData = new ArrayList<Location>(1) {
		private static final long serialVersionUID = 1L;
		{
			add(null);
			add(null);
		}
	};
	
	public static String setHome(Player p, Location loc) {
		editPlayer = p;
		transferData.set(0, loc);
		return ChatColor.GOLD + "Set NPC home Location!";
	}
	
	public static String setWork(Player p, Location loc) {
		editPlayer = p;
		transferData.set(1, loc);
		return ChatColor.GOLD + "Set NPC work Location!";
	}
	
	public static Location getHome() {
		return transferData.get(0);
	}
	
	public static Location getWork() {
		return transferData.get(1);
	}
	
	public static boolean hasLocations() {
		if(getHome() == null || getWork() == null) {
			return false;
		}
		return true;
	}
	
}
