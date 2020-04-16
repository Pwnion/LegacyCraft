package com.pwnion.legacycraft.levels;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.Util;

public class Levels {
	
	//Add PLAYER_EXPERIENCE to Player Data
	
	public static void save(UUID playerUUID) {
		//Save experience to file
	}
	
	public static void load(UUID playerUUID) {
		//Load experience into player data
	}
	
	public static int getTotalExperience(UUID playerUUID) {
		return 0; //TODO: get from player data
	}
	
	//Will get experience from current level
	public static int getExperience(UUID playerUUID) {
		return getTotalExperience(playerUUID) - getTotalExperienceForLevel(getLevel(playerUUID));
	}
	
	public static void setTotalExperience(Player p, int experience) {
		if(getLevel(p.getUniqueId()) < getLevel(experience)) {
			Levelup.onPlayerLevelup(p);
		}
		//TODO: save to player data
	}
	
	public static void addExperience(Player p, int experience) {
		setTotalExperience(p, getTotalExperience(p.getUniqueId()) + experience);
	}
	
	public static void setLevel(Player p, int level) {
		setTotalExperience(p, getTotalExperienceForLevel(level));
	}
	
	public static int getLevel(int experience) {
		int i = 1;
		while(getTotalExperienceForLevel(i + 1) <= experience) {
			i++; 
		}
		return i;
	} 
	
	public static int getLevel(UUID playerUUID) {
		return getLevel(getTotalExperience(playerUUID));
	}
	
	public static int getExperienceForLevel(int level) {
		return (int) (Math.round(getExperienceForLevelA(level) / 50) * 50); //TODO: Reverse Equations
	}
	
	public static double getExperienceForLevelA(int level) {
		if(level <= 1) { return 0; }
		return (int) (100 * Math.pow(1.5, level - 2)); //TODO: Reverse Equations
	}
	
	public static int getTotalExperienceForLevel(int level) {
		if(level <= 1) { return 0; }
		return getTotalExperienceForLevel(level - 1) + getExperienceForLevel(level);
	}
}
