package com.pwnion.legacycraft.levelling;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.ConfigAccessor;
import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.abilities.SkillTree;
import com.pwnion.legacycraft.abilities.SkillTree.PlayerClass;

public class Experience {
	private Player p;
	private UUID playerUUID;
	private SkillTree skillTree;
	private PlayerClass playerClass;
	private HashMap<PlayerClass, HashMap<ExperienceType, Integer>> allExperience;
	
	public Experience(Player p) {
		this.p = p;
		this.playerUUID = p.getUniqueId();
		this.skillTree = (SkillTree) LegacyCraft.getPlayerData(playerUUID, PlayerData.SKILL_TREE);
		this.playerClass = skillTree.getPlayerClass();
		this.allExperience = load();
	}
	
	//Save experience to file
	public void save() {
		final ConfigAccessor playerDataConfig = new ConfigAccessor("player-data.yml");
		final ConfigurationSection playerDataCS = playerDataConfig.getRoot();
		
		for(PlayerClass playerClass : SkillTree.PlayerClass.values()) {
			if(playerClass.equals(PlayerClass.NONE)) continue;
			
			String node = "players." + playerUUID.toString() + "." + playerClass.toString() + ".experience";
			
			for(ExperienceType experienceType : ExperienceType.values()) {
				playerDataCS.set(node + "." + experienceType.toString(), getTotalExperience(experienceType, playerClass));
			}
		}
		
		playerDataConfig.saveCustomConfig();
	}
	
	//Load experience from file into player data
	public HashMap<PlayerClass, HashMap<ExperienceType, Integer>> load() {
		HashMap<PlayerClass, HashMap<ExperienceType, Integer>> playerExperience = new HashMap<PlayerClass, HashMap<ExperienceType, Integer>>();
		
		final ConfigAccessor playerDataConfig = new ConfigAccessor("player-data.yml");
		final ConfigurationSection playerDataCS = playerDataConfig.getRoot();
        SkillTree skillTree = (SkillTree) LegacyCraft.getPlayerData(playerUUID, PlayerData.SKILL_TREE);
		
        for(PlayerClass playerClass : SkillTree.PlayerClass.values()) {
        	if(playerClass.equals(PlayerClass.NONE)) continue;
        	
        	String node = "players." + playerUUID.toString() + "." + playerClass.toString() + ".experience";
        	ConfigurationSection experienceCS = playerDataCS.getConfigurationSection(node);
        	
        	HashMap<ExperienceType, Integer> playerClassExperience = new HashMap<ExperienceType, Integer>();
        	
        	if(experienceCS != null) {
        		for(String experienceTypeString : experienceCS.getKeys(false)) {
        			ExperienceType experienceType = ExperienceType.valueOf(experienceTypeString);
        			int experienceValue = playerDataCS.getInt(node + "." + experienceTypeString);
        			
        			playerClassExperience.put(experienceType, experienceValue);
        		}
        	}
        	playerExperience.put(playerClass, playerClassExperience);
        }
        
		return playerExperience;
	}
	
	//Gets the total experience for all experience types
	public HashMap<ExperienceType, Integer> getAllExperience() {
		return allExperience.get(playerClass);
	}
	
	public HashMap<ExperienceType, Integer> getAllExperience(PlayerClass playerClass) {
		return allExperience.get(playerClass);
	}

	//Gets Experience from level 1
	public int getTotalExperience(ExperienceType experienceType) {
		return getAllExperience().get(experienceType);
	}
	
	public int getTotalExperience(ExperienceType experienceType, PlayerClass playerClass) {
		return getAllExperience(playerClass).get(experienceType);
	}
	
	//Gets experience from current level
	public int getExperience(ExperienceType experienceType) {
		return getTotalExperience(experienceType) - getTotalExperienceForLevel(getLevel(experienceType), experienceType);
	}
	
	public void setTotalExperience(int experience, ExperienceType experienceType) {
		if(getLevel(experienceType) < getLevel(experience, experienceType)) {
			PlayerLevelup.onPlayerLevelup(p, this, experienceType, experience);
		}
		
		getAllExperience().put(experienceType, experience);
		
		p.setTotalExperience(0);
		p.giveExpLevels(getLevel(experienceType));
		p.setExp(getPercentExperience(experienceType));
	}
	
	public void addExperience(int experience, ExperienceType experienceType) {
		
		//Subject to change
		if(experienceType == ExperienceType.PLAYER) {
			p.sendMessage(ChatColor.GRAY + "+" + experience);
		}
		
		setTotalExperience(getTotalExperience(experienceType) + experience, experienceType);
	}
	
	public void setLevel(int level, ExperienceType experienceType) {
		setTotalExperience(getTotalExperienceForLevel(level, experienceType), experienceType);
	}
	
	//Adds 'level' levels
	public void addLevel(int level, ExperienceType experienceType) {
		
		//Subject to change
		if(experienceType == ExperienceType.PLAYER) {
			String addS = "";
			if(level > 1) { addS = "s"; }
			p.sendMessage(ChatColor.GRAY + "+" + level + " Level" + addS);
		}
		
		setTotalExperience(getExperience(experienceType) + getTotalExperienceForLevel(level + getLevel(experienceType), experienceType), experienceType);
	}
	
	//Adds one level
	public void addLevel(ExperienceType experienceType) {
		addLevel(1, experienceType);
	}
	
	//Gets the level given the total amount of experience
	public int getLevel(int experience, ExperienceType experienceType) {
		int i = 1;
		while(getTotalExperienceForLevel(i + 1, experienceType) <= experience) i++;
		return i;
	} 
	
	//Gets the level of the player
	public int getLevel(ExperienceType experienceType) {
		return getLevel(getTotalExperience(experienceType), experienceType);
	}
	
	//Gets experience required to levelup from 'level' to next level
	public int getExperienceFromLevel(int level, ExperienceType experienceType) {
		if(level < 1) { return 0; }
		
		//Differing calculations per type?
		
		final double level2Exp = 100;
		final double roundToNearest = 50;
		final double geometricSeriesConstant = 1.5;
		
		return (int) (Math.round(level2Exp * Math.pow(geometricSeriesConstant, level - 1) / roundToNearest) * roundToNearest);
	}
	
	//Gets experience required to levelup to 'level' from level 1
	public int getTotalExperienceForLevel(int level, ExperienceType experienceType) {
		if(level <= 1) { return 0; }
		
		// ==== Non-recursion sum ====
		//final double a0 = getExperienceFromLevel(1, experienceType);
		//final double k = geometricSeriesConstant;
		//final double i = level;
		//final double roundToNearest = 50;
		
		//return (int) (Math.round(((a0 * (Math.pow(k, i + 1) - 1)) / (k - 1)) / roundToNearest) * roundToNearest);
		
		return getTotalExperienceForLevel(level - 1, experienceType) + getExperienceFromLevel(level - 1, experienceType);
	}
	
	//Returns value between 0 and 1
	public float getPercentExperience(ExperienceType experienceType) {
		return (float) getExperience(experienceType) / (float) getExperienceFromLevel(getLevel(experienceType), experienceType);
	}
}
