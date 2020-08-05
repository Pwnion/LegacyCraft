package com.pwnion.legacycraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.pwnion.legacycraft.abilities.SkillTree;
import com.pwnion.legacycraft.abilities.SkillTree.PlayerClass;
import com.pwnion.legacycraft.quests.Quest;
import com.pwnion.legacycraft.quests.QuestManager;
import com.pwnion.legacycraft.levelling.Experience;

//All types of player data that is shared between classes
public class PlayerData {
	public static HashMap<UUID, HashMap<PlayerDataType, Object>> playerData = new HashMap<UUID, HashMap<PlayerDataType, Object>>();
 	
 	public static SkillTree getSkillTree(UUID playerUUID) {
		return (SkillTree) playerData.get(playerUUID).get(PlayerDataType.SKILL_TREE);
	}

	public static void setSkillTree(UUID playerUUID, SkillTree value) {
		playerData.get(playerUUID).put(PlayerDataType.SKILL_TREE, value);
	}
 	
 	public static int getJumpCount(UUID playerUUID) {
		return (int) playerData.get(playerUUID).get(PlayerDataType.JUMP_COUNTER);
	}

	public static void setJumpCount(UUID playerUUID, int value) {
		playerData.get(playerUUID).put(PlayerDataType.JUMP_COUNTER, value);
	}
	
	public static float getFallDistance(UUID playerUUID) {
		return (float) playerData.get(playerUUID).get(PlayerDataType.FALL_DISTANCE);
	}

	public static void setFallDistance(UUID playerUUID, float value) {
		playerData.get(playerUUID).put(PlayerDataType.FALL_DISTANCE, value);
	}
	
	public static PlayerClass getClassInventoryOpen(UUID playerUUID) {
		return (PlayerClass) playerData.get(playerUUID).get(PlayerDataType.CLASS_INVENTORY_OPEN);
	}

	public static void setClassInventoryOpen(UUID playerUUID, PlayerClass value) {
		playerData.get(playerUUID).put(PlayerDataType.CLASS_INVENTORY_OPEN, value);
	}
	
	public static SkillTree.Aspect getAspectInventoryOpen(UUID playerUUID) {
		return (SkillTree.Aspect) playerData.get(playerUUID).get(PlayerDataType.ASPECT_INVENTORY_OPEN);
	}

	public static void setAspectInventoryOpen(UUID playerUUID, SkillTree.Aspect value) {
		playerData.get(playerUUID).put(PlayerDataType.ASPECT_INVENTORY_OPEN, value);
	}
	
	public static HashMap<Quest, ArrayList<Integer>> getUnfinishedQuests(UUID playerUUID) {
		return (HashMap<Quest, ArrayList<Integer>>) playerData.get(playerUUID).get(PlayerDataType.UNFINISHED_QUESTS);
	}

	public static void setUnfinishedQuests(UUID playerUUID, HashMap<Quest, ArrayList<Integer>> value) {
		playerData.get(playerUUID).put(PlayerDataType.UNFINISHED_QUESTS, value);
	}
	
	public static ArrayList<Quest> getFinishedQuests(UUID playerUUID) {
		return (ArrayList<Quest>) playerData.get(playerUUID).get(PlayerDataType.FINISHED_QUESTS);
	}

	public static void setFinishedQuests(UUID playerUUID, ArrayList<Quest> value) {
		playerData.get(playerUUID).put(PlayerDataType.FINISHED_QUESTS, value);
	}
	
	public static int getSwapSlot(UUID playerUUID) {
		return (int) playerData.get(playerUUID).get(PlayerDataType.SWAP_SLOT);
	}

	public static void setSwapSlot(UUID playerUUID, int value) {
		playerData.get(playerUUID).put(PlayerDataType.SWAP_SLOT, value);
	}
	
	public static Experience getExperience(UUID playerUUID) {
		return (Experience) playerData.get(playerUUID).get(PlayerDataType.EXPERIENCE);
	}

	public static void setExperience(UUID playerUUID, Experience value) {
		playerData.get(playerUUID).put(PlayerDataType.EXPERIENCE, value);
	}
	
	public static int getLastAttack(UUID playerUUID) {
		return (int) playerData.get(playerUUID).get(PlayerDataType.LAST_ATTACK);
	}

	public static void setLastAttack(UUID playerUUID, int value) {
		playerData.get(playerUUID).put(PlayerDataType.LAST_ATTACK, value);
	}
 	
 	public static void generate(Player p) {
 		UUID playerUUID = p.getUniqueId();
 		HashMap<PlayerDataType, Object> data = new HashMap<PlayerDataType, Object>();
 		for(PlayerDataType type : PlayerDataType.values()) {
 			data.put(type, type.getDefault());
 			
 		}
 		playerData.put(playerUUID, data);
 		setSkillTree(playerUUID, new SkillTree(p));
 		setUnfinishedQuests(playerUUID, QuestManager.loadUnfinishedPlayerData(playerUUID));
 		setFinishedQuests(playerUUID, QuestManager.loadFinishedPlayerData(playerUUID));
 		setExperience(playerUUID, new Experience(p));
 	}
 	
 	public static void remove(UUID playerUUID) {
 		playerData.remove(playerUUID);
 	}
 	
 	private static enum PlayerDataType {
 		SKILL_TREE(SkillTree.class),
 	    JUMP_COUNTER(0),
 		FALL_DISTANCE(0f),
 		CLASS_INVENTORY_OPEN(PlayerClass.NONE),
 		ASPECT_INVENTORY_OPEN(SkillTree.Aspect.NONE),
 		UNFINISHED_QUESTS(new HashMap<Quest, ArrayList<Integer>>()),
 		FINISHED_QUESTS(new ArrayList<Quest>()),
 		SWAP_SLOT(-1),
 		EXPERIENCE(Experience.class),
 		LAST_ATTACK(-1);

 		private final Object defaultt;
 	 
 		PlayerDataType(Class<?> classOfDefault) {
 	 		this.defaultt = null;
 	 	}
 		
 	 	PlayerDataType(Object defaultt) {
 	 		this.defaultt = defaultt;
 	 	}

		public Object getDefault() {
 	 		return defaultt;
 	 	}
 	 }
}
