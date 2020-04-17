package com.pwnion.legacycraft.levels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.ConfigAccessor;
import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.quests.Quest;

public class Levels {
	
	//Add PLAYER_EXPERIENCE to Player Data
	
	//Save experience to file
	public static void save(UUID playerUUID) {
		//TODO: Get checked by Aden
		
		final ConfigAccessor playerDataConfig = new ConfigAccessor("player-data.yml");
		final ConfigurationSection playerDataCS = playerDataConfig.getRoot();
		final String node = "players." + playerUUID.toString() + ".experience";
		
		ConfigurationSection experienceCS = playerDataCS.getConfigurationSection(node);
		
		experienceCS.set(node, getTotalExperience(playerUUID));
		
		playerDataConfig.saveCustomConfig();
	}
	
	//Load experience from file into player data
	public static int load(UUID playerUUID) {
		int playerExperience = 0;
		
		final ConfigAccessor playerDataConfig = new ConfigAccessor("player-data.yml");
		final ConfigurationSection playerDataCS = playerDataConfig.getRoot();
		final String node = "players." + playerUUID.toString() + ".experience";
		
		ConfigurationSection experienceCS = playerDataCS.getConfigurationSection(node);
		
		playerExperience = experienceCS.getInt(""); //TODO: Get Aden to check

		return playerExperience;
	}
	
	public static int getTotalExperience(UUID playerUUID) {
		return (int) LegacyCraft.getPlayerData(playerUUID, PlayerData.EXPERIENCE);
		
		//TODO: get from player data
	}
	
	//Will get experience from current level
	public static int getExperience(UUID playerUUID) {
		return getTotalExperience(playerUUID) - getTotalExperienceForLevel(getLevel(playerUUID));
	}
	
	public static void setTotalExperience(Player p, int experience) {
		if(getLevel(p.getUniqueId()) < getLevel(experience)) {
			Levelup.onPlayerLevelup(p, experience);
		}
		
		//TODO: save to player data
		LegacyCraft.setPlayerData(p.getUniqueId(), PlayerData.EXPERIENCE, experience);
		
		p.setTotalExperience(0);
		p.giveExpLevels(getLevel(p.getUniqueId()));
		p.setExp(getPercentExperience(p.getUniqueId()));
	}
	
	public static void addExperience(Player p, int experience) {
		setTotalExperience(p, getTotalExperience(p.getUniqueId()) + experience);
	}
	
	public static void setLevel(Player p, int level) {
		setTotalExperience(p, getTotalExperienceForLevel(level));
	}
	
	//Gets the level given the total amount of experience
	public static int getLevel(int experience) {
		int i = 1;
		while(getTotalExperienceForLevel(i + 1) <= experience) {
			i++; 
		}
		return i;
	} 
	
	//Gets the level of the player
	public static int getLevel(UUID playerUUID) {
		return getLevel(getTotalExperience(playerUUID));
	}
	
	//Gets experience required to levelup from 'level' to next level
	public static int getExperienceFromLevel(int level) {
		if(level < 1) { return 0; }
		
		final double level2Exp = 100;
		final double roundToNearest = 50;
		final double geometricSeriesConstant = 1.5;
		
		return (int) (Math.round(level2Exp * Math.pow(geometricSeriesConstant, level - 1) / roundToNearest) * roundToNearest);
	}
	
	//Gets experience required to levelup to 'level' from level 1
	public static int getTotalExperienceForLevel(int level) {
		if(level <= 1) { return 0; }
		return getTotalExperienceForLevel(level - 1) + getExperienceFromLevel(level - 1);
	}
	
	//Returns value between 0 and 1
	public static float getPercentExperience(UUID playerUUID) {
		return (float) getExperience(playerUUID) / (float) getExperienceFromLevel(getLevel(playerUUID));
	}
}
