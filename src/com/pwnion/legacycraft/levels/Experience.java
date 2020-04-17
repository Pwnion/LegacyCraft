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

public class Experience {
	
	//Save experience to file
	public static void save(UUID playerUUID) {
		//TODO: Get checked by Aden
		
		final ConfigAccessor playerDataConfig = new ConfigAccessor("player-data.yml");
		final ConfigurationSection playerDataCS = playerDataConfig.getRoot();
		final String nodePrefix = "players." + playerUUID.toString() + ".experience.";
		
		ConfigurationSection experienceCS = playerDataCS.getConfigurationSection(nodePrefix);
		
		//experienceCS.set(nodePrefix, getTotalExperience(playerUUID));
		
		getAllExperience(playerUUID).forEach((ExperienceType experienceType, Integer experienceValue) -> {
			playerDataCS.set(nodePrefix + experienceType.toString(), experienceValue);
		});
		
		playerDataConfig.saveCustomConfig();
	}
	
	//Load experience from file into player data
	public static HashMap<ExperienceType, Integer> load(UUID playerUUID) {
		HashMap<ExperienceType, Integer> playerExperience = new HashMap<ExperienceType, Integer>();
		
		final ConfigAccessor playerDataConfig = new ConfigAccessor("player-data.yml");
		final ConfigurationSection playerDataCS = playerDataConfig.getRoot();
		final String nodePrefix = "players." + playerUUID.toString() + ".experience.";
		
		ConfigurationSection experienceCS = playerDataCS.getConfigurationSection(nodePrefix);
		
		 //TODO: Get Aden to check
		
		if(experienceCS == null) return playerExperience;
		
		experienceCS.getKeys(false).forEach((String experienceTypeString) -> {
			ExperienceType experienceType = ExperienceType.valueOf(experienceTypeString);
			int experienceValue = playerDataCS.getInt(nodePrefix + experienceTypeString);
			
			playerExperience.put(experienceType, experienceValue);
		});

		return playerExperience;
	}
	
	//Gets the total experience for all experience types
	public static HashMap<ExperienceType, Integer> getAllExperience(UUID playerUUID) {
		HashMap<ExperienceType, Integer> allExperience = new HashMap<ExperienceType, Integer>();
		for(ExperienceType experienceType : ExperienceType.values()) {
			allExperience.put(experienceType, getTotalExperience(playerUUID, experienceType));
		}
		return allExperience;
	}
	
	public static int getTotalExperience(UUID playerUUID, ExperienceType experienceType) {
		return (int) LegacyCraft.getPlayerData(playerUUID, PlayerData.EXPERIENCE).get(experienceType);
	}
	
	//Will get experience from current level
	public static int getExperience(UUID playerUUID, ExperienceType experienceType) {
		return getTotalExperience(playerUUID, experienceType) - getTotalExperienceForLevel(getLevel(playerUUID, experienceType), experienceType);
	}
	
	public static void setTotalExperience(Player p, int experience, ExperienceType experienceType) {
		if(getLevel(p.getUniqueId(), experienceType) < getLevel(experience, experienceType)) {
			Levelup.onPlayerLevelup(p, experience);
		}
		
		//TODO: save to player data
		LegacyCraft.setPlayerData(p.getUniqueId(), PlayerData.EXPERIENCE, experience);
		
		p.setTotalExperience(0);
		p.giveExpLevels(getLevel(p.getUniqueId(), experienceType));
		p.setExp(getPercentExperience(p.getUniqueId(), experienceType));
	}
	
	public static void addExperience(Player p, int experience, ExperienceType experienceType) {
		setTotalExperience(p, getTotalExperience(p.getUniqueId(), experienceType) + experience, experienceType);
	}
	
	public static void setLevel(Player p, int level, ExperienceType experienceType) {
		setTotalExperience(p, getTotalExperienceForLevel(level, experienceType), experienceType);
	}
	
	//Gets the level given the total amount of experience
	public static int getLevel(int experience, ExperienceType experienceType) {
		int i = 1;
		while(getTotalExperienceForLevel(i + 1, experienceType) <= experience) {
			i++; 
		}
		return i;
	} 
	
	//Gets the level of the player
	public static int getLevel(UUID playerUUID, ExperienceType experienceType) {
		return getLevel(getTotalExperience(playerUUID, experienceType), experienceType);
	}
	
	//Gets experience required to levelup from 'level' to next level
	public static int getExperienceFromLevel(int level, ExperienceType experienceType) {
		if(level < 1) { return 0; }
		
		//Differing calculations per type?
		
		final double level2Exp = 100;
		final double roundToNearest = 50;
		final double geometricSeriesConstant = 1.5;
		
		return (int) (Math.round(level2Exp * Math.pow(geometricSeriesConstant, level - 1) / roundToNearest) * roundToNearest);
	}
	
	//Gets experience required to levelup to 'level' from level 1
	public static int getTotalExperienceForLevel(int level, ExperienceType experienceType) {
		if(level <= 1) { return 0; }
		return getTotalExperienceForLevel(level - 1, experienceType) + getExperienceFromLevel(level - 1, experienceType);
	}
	
	//Returns value between 0 and 1
	public static float getPercentExperience(UUID playerUUID, ExperienceType experienceType) {
		return (float) getExperience(playerUUID, experienceType) / (float) getExperienceFromLevel(getLevel(playerUUID, experienceType), experienceType);
	}
}
