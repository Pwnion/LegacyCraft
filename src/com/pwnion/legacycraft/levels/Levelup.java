package com.pwnion.legacycraft.levels;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Levelup {

	//Occurs before experience is saved
	public static void onPlayerLevelup(Player p, int newExperience) {
		int newLevel = Levels.getLevel(newExperience);
		int oldLevel = Levels.getLevel(p.getUniqueId());
		
		p.sendMessage(ChatColor.GOLD + "Levelup!!! " + oldLevel + " -> " + newLevel);
	}
}
