package com.pwnion.legacycraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.pwnion.legacycraft.PlayerData.PlayerDataStore;
import com.pwnion.legacycraft.abilities.SkillTree;
import com.pwnion.legacycraft.abilities.SkillTree.PlayerClass;
import com.pwnion.legacycraft.quests.Quest;
import com.pwnion.legacycraft.levelling.Experience;

//All types of player data that is shared between classes
public class PlayerData {
	
	private static HashMap<UUID, HashMap<PlayerDataType, PlayerDataStore<?>>> playerData = new HashMap<UUID, HashMap<PlayerDataType, PlayerDataStore<?>>>();
 	
 	public static PlayerDataStore<?> get(UUID playerUUID, PlayerDataType type) {
 		return playerData.get(playerUUID).get(type);
 	}
 	
 	public static HashMap<PlayerDataType, PlayerDataStore<?>> get(UUID playerUUID) {
 		return playerData.get(playerUUID);
 	}
 	
 	public static void generate(UUID playerUUID) {
 		for(PlayerDataType type : PlayerDataType.values()) {
 			HashMap<PlayerDataType, PlayerDataStore<?>> data = new HashMap<PlayerDataType, PlayerDataStore<?>>();
 			data.put(type, type.getDefault().clone());
 			playerData.put(playerUUID, data);
 		}
 	}
 	
 	public static enum PlayerDataType {
 		SKILL_TREE(new PlayerDataStore<SkillTree>(null)),
 	    JUMP_COUNTER(new PlayerDataStore<Integer>(0)),
 		FALL_DISTANCE(new PlayerDataStore<Float>(0f)),
 		CLASS_INVENTORY_OPEN(new PlayerDataStore<PlayerClass>(PlayerClass.NONE)),
 		ASPECT_INVENTORY_OPEN(new PlayerDataStore<SkillTree.Aspect>(SkillTree.Aspect.NONE)),
 		UNFINISHED_QUESTS(new PlayerDataStore<HashMap<Quest, ArrayList<Integer>>>(new HashMap<Quest, ArrayList<Integer>>())),
 		FINISHED_QUESTS(new PlayerDataStore<ArrayList<Quest>>(new ArrayList<Quest>())),
 		SWAP_SLOT(new PlayerDataStore<Integer>(-1)),
 		EXPERIENCE(new PlayerDataStore<Experience>(null)),
 		LAST_ATTACK(new PlayerDataStore<Integer>(-1));

 		private PlayerDataStore<?> defaultt;
 	 
 	 	PlayerDataType(PlayerDataStore<?> defaultt) {
 	 		this.defaultt = defaultt;
 	 	}
 	 	
 	 	public PlayerDataStore<?> getDefault() {
 	 		return defaultt;
 	 	}
 	 }
 	
	 public static class PlayerDataStore<T> {
	 	private T data;
	 	
	 	public PlayerDataStore(T data) {
	 		this.data = data;
	 	}
	 	
	 	public Class<T> get() {
	 		return (Class<T>) data;
	 	}
	 	
	 	public void set(T data) {
	 		this.data = data;
	 	}
	 	
	 	public PlayerDataStore<T> clone() {
	 		return new PlayerDataStore<T>(data);
	 	}
	 }
	//*/
}
