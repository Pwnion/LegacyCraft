package com.pwnion.legacycraft.quests;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class Trigger {
	
	final TriggerType type;
	private final Object data;
	private final int finishCondition;

	public Trigger(TriggerType type, String data, int finishCondition) {
		this.type = type;
		this.data = deserialise(data);
		this.finishCondition = finishCondition;
	}
	
	//TEMP
	public Trigger(TriggerType type, Object data, int finishCondition) {
		this.type = type;
		this.data = (data);
		this.finishCondition = finishCondition;
	}
	
	public TriggerType getType() {
		return type;
	}
	
	public int getFinishCondition() {
		return finishCondition;
	}
	
	public Material getItem() {
		if(type == TriggerType.ITEM) {
			return (Material) data;
		}
		return null;
	}
	
	public LocationData getLocationData() {
		if(type == TriggerType.LOCATION) {
			return (LocationData) data;
		}
		return null;
	}
	
	public EntityType getKillEntity() {
		if(type == TriggerType.KILL_ENTITY) {
			return (EntityType) data;
		}
		return null;
	}
	
	public HashMap<String, Boolean> getNPCData() {
		if(type == TriggerType.NPC) {
			return (HashMap<String, Boolean>) data;
		}
		return null;
	}
	
	public String getNPCName() {
		return (String) getNPCData().keySet().toArray()[0];
	}
	
	//Only Required if an in-game quest creator was added
	/* 
	public String serialise() {
		switch(type) {
		case ITEM:
			return getItem().name();
		case LOCATION:
			return getLocationData().serialise();
		case KILLENTITY:
			return getKillEntity().toString();
		case NPC:
			String name = getNPCName();
			return name + "|" + getNPCData().get(name);
		}
		return null;
	}*/
	
	private Object deserialise(String data) {
		switch(type) {
		case ITEM: //Format: DIAMOND
			return Material.matchMaterial(data);
		case LOCATION: //Format: world|x|y|z|radius,world2|x2|y2|z2|radius2
			return new LocationData(data);
		case KILL_ENTITY: //Format: ZOMBIE
			return EntityType.valueOf(data);
		case NPC: //Format: NpcName|submit EG: blacksmith|false
			String npcArray[] = data.split("|");
			HashMap<String, Boolean> npcData = new HashMap<String, Boolean>();
			npcData.put(npcArray[0], Boolean.valueOf(npcArray[1]));
			return npcData;
		}
		return data;
	}
	
	public boolean equals(Trigger trigger) {
		return (type == trigger.type && data == trigger.data);
	}
}
