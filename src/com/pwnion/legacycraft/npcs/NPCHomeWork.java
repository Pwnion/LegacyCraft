package com.pwnion.legacycraft.npcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class NPCHomeWork {
	
	private static HashMap<UUID, ArrayList<Location>> TransferData = new HashMap<UUID, ArrayList<Location>>();
	
	public static String setHome(Player p, Location loc) {
		ArrayList<Location> data = getData(p);
		data.set(0, loc);
		TransferData.put(p.getUniqueId(), data);
		return ChatColor.GOLD + "Set NPC home Location!";
	}
	
	public static String setWork(Player p, Location loc) {
		ArrayList<Location> data = getData(p);
		data.set(1, p.getLocation());
		TransferData.put(p.getUniqueId(), data);
		return ChatColor.GOLD + "Set NPC work Location!";
	}
	
	private static ArrayList<Location> getData(Player p) {
		UUID uuid = p.getUniqueId();
		if(TransferData.containsKey(uuid)) {
			return TransferData.get(uuid);
		}
		ArrayList<Location> defaultData = new ArrayList<Location>();
		defaultData.add(0, null);
		defaultData.add(1, null);
		TransferData.put(uuid, defaultData);
		return defaultData;
	}
	
	public static Location getHome(Player p) {
		return getData(p).get(0);
	}
	
	public static Location getWork(Player p) {
		return getData(p).get(1);
	}
	
	public static boolean hasLocations(Player p) {
		if(getHome(p) == null || getWork(p) == null) {
			return false;
		}
		return true;
	}
	
}
