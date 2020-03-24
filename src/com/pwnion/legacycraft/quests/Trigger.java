package com.pwnion.legacycraft.quests;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class Trigger {
	
	TriggerType type;
	Object data;
	int finishCondition;

	@SuppressWarnings("incomplete-switch")
	public Trigger(TriggerType name, Object data, int finishCondition) {
		this.type = name;
		
		switch(name) {
		case NPC:
			if(data instanceof String) {
				HashMap<String, Boolean> npcData = new HashMap<String, Boolean>();
				npcData.put((String) data, false);
				data = npcData;
			}
			break;
		}
		
		this.data = data;
		this.finishCondition = finishCondition;
	}
	
	public TriggerType getName() {
		return type;
	}
	
	public int getFinishCondition() {
		return finishCondition;
	}
	
	public void setFinishCondition(int value) {
		finishCondition = value;
	}
	
	public Material getItem() {
		if(type == TriggerType.ITEM) {
			return (Material) data;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<Location, Integer> getLocationData() {
		if(type == TriggerType.LOCATION) {
			return (HashMap<Location, Integer>) data;
		}
		return null;
	}
	
	public EntityType getKillEntity() {
		if(type == TriggerType.KILLENTITY) {
			return (EntityType) data;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Boolean> getNPCData() {
		if(type == TriggerType.NPC) {
			return (HashMap<String, Boolean>) data;
		}
		return null;
	}
	
	public String getNPCName() {
		return (String) getNPCData().keySet().toArray()[0];
	}
	
	public String serialise() {
		switch(type) {
		case ITEM:
			return getItem().name();
		case LOCATION:
			HashMap<Location, Integer> locData = getLocationData();
			break;
		case KILLENTITY:
			return getKillEntity().toString();
		case NPC:
			String name = getNPCName();
			return name + "|" + getNPCData().get(name);
		}
		return null;
	}
	
	public static Object deserialise(TriggerType type, String data) {
		switch(type) {
		case ITEM:
			return Material.matchMaterial(data);
		case LOCATION:
			HashMap<Location, Integer> locData = new HashMap<Location, Integer>();
			break;
		case KILLENTITY:
			return EntityType.valueOf(data);
		case NPC:
			String array[] = data.split("|");
			HashMap<String, Boolean> npcData = new HashMap<String, Boolean>();
			npcData.put(array[0], Boolean.valueOf(array[1]));
			return npcData;
		}
	}
	
	public boolean equals(Trigger trigger) {
		return (type == trigger.type && data == trigger.data);
	}
}
