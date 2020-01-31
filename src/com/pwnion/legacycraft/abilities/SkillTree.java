package com.pwnion.legacycraft.abilities;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.pwnion.legacycraft.ConfigAccessor;

//Class for accessing ability data for players, mainly by reading/writing from/to yml files
public class SkillTree {
	//All possible classes a player can have equipped
	public static enum PlayerClass {
		NONE,
		STRIKER,
		VANGUARD,
		ROGUE,
		SHAMAN
	}
	
	//All possible aspects a player can have equipped
	public static enum Aspect {
		NONE,
		IGNIS,
		TERRA,
		VACUOUS,
		ARCTIC
	}
	
	//All possible builds a player can have equipped
	public static enum Build {
		NONE,
		IGNIS_STRIKER,
		TERRA_STRIKER,
		VACUOUS_STRIKER,
		ARCTIC_STRIKER,
		IGNIS_VANGUARD,
		TERRA_VANGUARD,
		VACUOUS_VANGUARD,
		ARCTIC_VANGUARD,
		IGNIS_ROGUE,
		TERRA_ROGUE,
		VACUOUS_ROGUE,
		ARCTIC_ROGUE,
		IGNIS_SHAMAN,
		TERRA_SHAMAN,
		VACUOUS_SHAMAN,
		ARCTIC_SHAMAN,
	}
	
	//All possible aptitudes a player can have equipped
	public static enum Aptitude {
		NONE,
		ONE,
		TWO
	}

	//All possible jumps a player can have equipped
	public static enum Jump {
		NONE,
		ONE,
		TWO
	}
	
	//All possible proficiencies a player can have equipped
	public static enum Proficiency {
		NONE,
		ONE,
		TWO
	}
	
	//All possible masteries a player can have equipped
	public static enum Mastery {
		NONE,
		ONE
	}
	
	private static final ConfigAccessor playerDataConfig = new ConfigAccessor("player-data.yml");
	private static final ConfigurationSection playerDataCS = playerDataConfig.getRoot();
	
	private UUID playerUUID;
	private String nodePrefix;
	private PlayerClass playerClass;
	private Aspect aspect;
	private Build build;
	
	public SkillTree(UUID playerUUID) {
		this.playerUUID = playerUUID;
		this.nodePrefix = "players." + playerUUID.toString() + ".";
		
		setup();
	}
	
	//If the plugin hasn't seen this player before, add default entries for them to player-data.yml
	private final void setup() {
		Set<String> playerUUIDs = playerDataCS.getKeys(true);
		if(!playerUUIDs.contains("players." + playerUUID.toString())) {
			ConfigAccessor playerDataTemplate = new ConfigAccessor("player-data-template.yml");
			ConfigurationSection playerDataTemplateCS = playerDataTemplate.getRoot();
			
			for(String key : playerDataTemplateCS.getKeys(true)) {
				playerDataCS.set(nodePrefix + key, playerDataTemplateCS.get(key));
			}
			playerDataConfig.saveCustomConfig();
		}
	}
	
	//Save player-data.yml
	private final void save() {
		playerDataConfig.saveCustomConfig();
	}
	
	/*
	 * CLASS GETTERS
	 */
	
	public final PlayerClass getPlayerClass() {
		playerClass = PlayerClass.valueOf(playerDataCS.getString(nodePrefix + "equipped.CLASS"));
		return playerClass;
	}
	
	/*
	 * CLASS SETTERS
	 */
	
	public final void setPlayerClass(PlayerClass playerClass) {
		playerDataCS.set(nodePrefix + "equipped.CLASS", playerClass.toString());
		save();
	}
	
	/*
	 * ASPECT GETTERS
	 */
	
	public final Aspect getAspect() {
		if(!getPlayerClass().equals(PlayerClass.NONE)) {
			aspect = Aspect.valueOf(playerDataCS.getString(nodePrefix + playerClass.toString() + ".ASPECT"));
		} else {
			aspect = Aspect.NONE;
		}
		return aspect;
	}
	
	public final Aspect getAspect(PlayerClass playerClass) {
		return Aspect.valueOf(playerDataCS.getString(nodePrefix + playerClass.toString() + ".ASPECT"));
	}
	
	/*
	 * ASPECT SETTERS
	 */
	
	public final void setAspect(PlayerClass playerClass, Aspect aspect) {
		playerDataCS.set(nodePrefix + playerClass.toString() + ".ASPECT", aspect.toString());
		save();
	}
	
	/*
	 * BUILD GETTERS
	 */
	
	public final Build getBuild() {
		if(!getPlayerClass().equals(PlayerClass.NONE) && !getAspect().equals(Aspect.NONE)) {
			build = Build.valueOf(aspect.toString() + "_" + playerClass.toString());
		} else {
			build = Build.NONE;
		}
		return build;
	}
	
	public final Build getBuild(PlayerClass playerClass, Aspect aspect) {
		return Build.valueOf(aspect.toString() + "_" + playerClass.toString());
	}
	
	/*
	 * APTITUDE GETTERS
	 */
	
	private final HashMap<Aptitude, Boolean> getAptitudes(PlayerClass playerClass, boolean equipped) {
		HashMap<Aptitude, Boolean> aptitudes = new HashMap<Aptitude, Boolean>(2);
		ConfigurationSection aptitudesCS = playerDataCS.getConfigurationSection(nodePrefix + playerClass.toString() + ".aptitudes");
		for(String aptitudeName : aptitudesCS.getKeys(false)) {
			boolean value = aptitudesCS.getBoolean(aptitudeName + (equipped ? ".equipped" : ".unlocked"));
			aptitudes.put(Aptitude.valueOf(aptitudeName), value);
		}
		return aptitudes;
	}
	
	public final HashMap<Aptitude, Boolean> getEquippedAptitudes(PlayerClass playerClass) {
		return getAptitudes(playerClass, true);
	}
	
	public final HashMap<Aptitude, Boolean> getEquippedAptitudes() {
		if(!getPlayerClass().equals(PlayerClass.NONE)) {
			return getAptitudes(playerClass, true);
		} else {
			return Maps.newHashMap(ImmutableMap.of(Aptitude.ONE, false, Aptitude.TWO, false));
		}
	}
	
	public final HashMap<Aptitude, Boolean> getUnlockedAptitudes(PlayerClass playerClass) {
		return getAptitudes(playerClass, false);
	}
	
	public final HashMap<Aptitude, Boolean> getUnlockedAptitudes() {
		if(!getPlayerClass().equals(PlayerClass.NONE)) {
			return getAptitudes(playerClass, false);
		} else {
			return Maps.newHashMap(ImmutableMap.of(Aptitude.ONE, false, Aptitude.TWO, false));
		}
	}
	
	/*
	 * APTITUDE SETTERS
	 */
	
	private final void setAptitude(PlayerClass playerClass, Aptitude aptitude, boolean equipped) {
		playerDataCS.set(nodePrefix + playerClass.toString() + ".aptitudes." + aptitude.toString() + (equipped ? ".equipped" : ".unlocked"), true);
		save();
	}
	
	public final void setEquippedAptitude(PlayerClass playerClass, Aptitude aptitude) {
		setAptitude(playerClass, aptitude, true);
	}
	
	public final void setUnlockedAptitude(PlayerClass playerClass, Aptitude aptitude) {
		setAptitude(playerClass, aptitude, false);
	}
	
	/*
	 * JUMP GETTERS
	 */
	
	private final HashMap<Jump, Boolean> getJumps(PlayerClass playerClass, boolean equipped) {
		HashMap<Jump, Boolean> jumps = new HashMap<Jump, Boolean>(2);
		ConfigurationSection jumpsCS = playerDataCS.getConfigurationSection(nodePrefix + playerClass.toString() + ".jumps");
		for(String jumpName : jumpsCS.getKeys(false)) {
			boolean value = jumpsCS.getBoolean(jumpName + (equipped ? ".equipped" : ".unlocked"));
			jumps.put(Jump.valueOf(jumpName), value);
		}
		return jumps;
	}
	
	public final Jump getEquippedJump(PlayerClass playerClass) {
		HashMap<Jump, Boolean> jumps = getJumps(playerClass, true);
		for(Jump jump : jumps.keySet()) {
			if(jumps.get(jump)) {
				return jump;
			}
		}
		return Jump.NONE;
	}
	
	public final Jump getEquippedJump() {
		if(!getPlayerClass().equals(PlayerClass.NONE)) {
			return getEquippedJump(playerClass);
		} else {
			return Jump.NONE;
		}
	}
	
	public final HashMap<Jump, Boolean> getUnlockedJumps(PlayerClass playerClass) {
		return getJumps(playerClass, false);
	}
	
	public final HashMap<Jump, Boolean> getUnlockedJumps() {
		if(!getPlayerClass().equals(PlayerClass.NONE)) {
			return getJumps(playerClass, false);
		} else {
			return Maps.newHashMap(ImmutableMap.of(Jump.ONE, false, Jump.TWO, false));
		}
	}
	
	/*
	 * JUMP SETTERS
	 */
	
	private final void setJump(PlayerClass playerClass, Jump jump, boolean equipped) {
		String path = nodePrefix + playerClass.toString() + ".jumps.";
		if(equipped) {
			Jump oppJump = jump.equals(Jump.ONE) ? Jump.TWO : Jump.ONE;
			playerDataCS.set(path + jump.toString() + ".equipped", true);
			playerDataCS.set(path + oppJump.toString() + ".equipped", false);
		} else {
			playerDataCS.set(path + jump.toString() + ".unlocked", true);
		}
		save();
	}
	
	public final void setEquippedJump(PlayerClass playerClass, Jump jump) {
		setJump(playerClass, jump, true);
	}
	
	public final void setUnlockedJump(PlayerClass playerClass, Jump jump) {
		setJump(playerClass, jump, false);
	}
	
	/*
	 * PROFICIENCY GETTERS
	 */
	
	private final HashMap<Proficiency, Boolean> getProficiencies(Build build, boolean equipped) {
		HashMap<Proficiency, Boolean> proficiencies = new HashMap<Proficiency, Boolean>(2);
		ConfigurationSection proficienciesCS = playerDataCS.getConfigurationSection(nodePrefix + build.toString() + ".proficiencies");
		for(String proficiencyName : proficienciesCS.getKeys(false)) {
			boolean value = proficienciesCS.getBoolean(proficiencyName + (equipped ? ".equipped" : ".unlocked"));
			proficiencies.put(Proficiency.valueOf(proficiencyName), value);
		}
		return proficiencies;
	}
	
	public final HashMap<Proficiency, Boolean> getEquippedProficiencies(Build build) {
		return getProficiencies(build, true);
	}
	
	public final HashMap<Proficiency, Boolean> getEquippedProficiencies() {
		if(!getBuild().equals(Build.NONE)) {
			return getProficiencies(build, true);
		} else {
			return Maps.newHashMap(ImmutableMap.of(Proficiency.ONE, false, Proficiency.TWO, false));
		}
	}
	
	public final HashMap<Proficiency, Boolean> getUnlockedProficiencies(Build build) {
		return getProficiencies(build, false);
	}
	
	public final HashMap<Proficiency, Boolean> getUnlockedProficiencies() {
		if(!getBuild().equals(Build.NONE)) {
			return getProficiencies(build, false);
		} else {
			return Maps.newHashMap(ImmutableMap.of(Proficiency.ONE, false, Proficiency.TWO, false));
		}
	}
	
	/*
	 * PROFICIENCY SETTERS
	 */
	
	private final void setProficiency(Build build, Proficiency proficiency, boolean equipped) {
		playerDataCS.set(nodePrefix + build.toString() + ".proficiencies." + proficiency.toString() + (equipped ? ".equipped" : ".unlocked"), true);
		save();
	}
	
	public final void setEquippedProficiency(Build build, Proficiency proficiency) {
		setProficiency(build, proficiency, true);
		save();
	}
	
	public final void setUnlockedProficiency(Build build, Proficiency proficiency) {
		setProficiency(build, proficiency, false);
		save();
	}
	
	/*
	 * MASTERY GETTERS
	 */
	
	private final HashMap<Mastery, Boolean> getMastery(Build build, boolean equipped) {
		String path = nodePrefix + build.toString() + ".masteries.ONE.";
		boolean value = playerDataCS.getBoolean(path + (equipped ? "equipped" : "unlocked"));
		
		return Maps.newHashMap(ImmutableMap.of(Mastery.ONE, value));
	}
	
	public final HashMap<Mastery, Boolean> getEquippedMastery(Build build) {
		return getMastery(build, true);
	}
	
	public final HashMap<Mastery, Boolean> getUnlockedMastery(Build build) {
		return getMastery(build, false);
	}
	
	/*
	 * MASTERY SETTERS
	 */
	
	private final void setMastery(Build build, boolean equipped) {
		playerDataCS.set(nodePrefix + build.toString() + ".masteries.ONE." + (equipped ? "equipped" : "unlocked"), true);
	}
	
	public final void setEquippedMastery(Build build) {
		setMastery(build, true);
	}
	
	public final void setUnlockedMastery(Build build) {
		setMastery(build, false);
	}
}