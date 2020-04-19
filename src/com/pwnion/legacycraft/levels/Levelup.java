package com.pwnion.legacycraft.levels;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Levelup {

	//Occurs before experience is saved
	public static void onPlayerLevelup(Player p, ExperienceType experienceType, int newExperience) {
		int newLevel = Experience.getLevel(newExperience, experienceType);
		int oldLevel = Experience.getLevel(p.getUniqueId(), experienceType);
		
		if(experienceType == ExperienceType.PLAYER) {
			p.sendMessage(ChatColor.GOLD + "Levelup!!! " + oldLevel + " -> " + newLevel);
		} else {
			p.sendMessage(ChatColor.AQUA + "You feel more comfortable weilding " + experienceType.toString() + " " + oldLevel + " -> " + newLevel);
		}
	}
}
