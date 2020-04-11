package com.pwnion.legacycraft.quests;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

public class Trigger {
	
	TriggerType type;
	Object data;
	int finishCondition;

	public Trigger(TriggerType name, String data, int finishCondition) {
		this.type = name;
		this.data = deserialise(name, data);
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
	public LocationData getLocationData() {
		if(type == TriggerType.LOCATION) {
			return (LocationData) data;
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
	
	private static Object deserialise(TriggerType type, String data) {
		switch(type) {
		case ITEM:
			return Material.matchMaterial(data);
		case LOCATION:
			String locArray[] = data.split("|");
			//return new LocationData(Bukkit.getWorld(locArray[0]), 
			//		Double.parseDouble(locArray[1]), 
			//		Double.parseDouble(locArray[2]), 
			//		Double.parseDouble(locArray[3]), 
			//		Double.parseDouble(locArray[4]));
		case KILLENTITY:
			return EntityType.valueOf(data);
		case NPC:
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
