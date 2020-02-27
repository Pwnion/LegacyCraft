package com.pwnion.legacycraft.npcs;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class HomeWorkData {
	
	private Player p;
	private Location home = null;
	private Location work = null;
	
	
	public HomeWorkData(Player p) {
		this.p = p;
	}
	
	public String setHome() {
		home = p.getLocation();
		return ChatColor.GOLD + "Set NPC home Location!";
	}
	
	public String setWork() {
		work = p.getLocation();
		return ChatColor.GOLD + "Set NPC work Location!";
	}
	
	public Location getHome() {
		return home;
	}
	
	public Location getWork() {
		return work;
	}
	
	public boolean hasLocations() {
		if(home == null || work == null) {
			return false;
		}
		return true;
	}
}
