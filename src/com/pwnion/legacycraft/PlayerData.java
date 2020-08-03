package com.pwnion.legacycraft;

//All types of player data that is shared between classes
public enum PlayerData {
	SKILL_TREE,
	JUMP_COUNTER,
	FALL_DISTANCE,
	CLASS_INVENTORY_OPEN,
	ASPECT_INVENTORY_OPEN,
	ADVENTURE_MODE,
	UNFINISHED_QUESTS,
	FINISHED_QUESTS,
	SWAP_SLOT,
	EXPERIENCE,
	LAST_ATTACK;
	
	/* Possible replacement to PlayerData
	 
	public enum PlayerDataType {
		Test(new PlayerDataStore<Integer>(1));

		private PlayerDataStore<?> defaultt;
	 
	 	PlayerDataType(PlayerDataStore<?> defaultt) {
	 		this.defaultt = defaultt;
	 	}
	 	
	 	public PlayerDataStore<?> getDefault() {
	 		return defaultt;
	 	}
	 }
	 
	 private class PlayerDataStore<T> {
	 	private T data;
	 	
	 	public PlayerDataStore(T data) {
	 		this.data = data;
	 	}
	 	
	 	public T get() {
	 		return data;
	 	}
	 	
	 	public void set(T data) {
	 		this.data = data;
	 	}
	 	
	 	public PlayerDataStore<T> clone() {
	 		return new PlayerDataStore<T>(data);
	 	}
	 }
	 
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
	 }
	 
	 //No Casting!
	 Integer lastAttack = PlayerData.get(playerUUID, PlayerDataType.LAST_ATTACK).get();
	//*/
}
