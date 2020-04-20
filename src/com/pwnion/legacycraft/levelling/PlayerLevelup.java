package com.pwnion.legacycraft.levelling;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class PlayerLevelup {
	
	//This is an event class that would be called when a player levels up in an experience type
	
	
	//Occurs before experience is saved
	public static void onPlayerLevelup(Player p, Experience experience, ExperienceType experienceType, int newExperience) {
		int newLevel = experience.getLevel(newExperience, experienceType);
		int oldLevel = experience.getLevel(experienceType);
		
		if(experienceType == ExperienceType.PLAYER) {
			p.sendMessage(ChatColor.GOLD + "Levelup!!! " + oldLevel + " -> " + newLevel);
		} else {
			p.sendMessage(ChatColor.AQUA + "You feel more comfortable weilding " + experienceType.toString() + " " + oldLevel + " -> " + newLevel);
		}
	} 
}
