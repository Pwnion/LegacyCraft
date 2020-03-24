package com.pwnion.legacycraft.quests;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class Trigger {
	
	TriggerType name;
	Object data;
	int finishCondition;

	@SuppressWarnings("incomplete-switch")
	public Trigger(TriggerType name, Object type, int finishCondition) {
		this.name = name;
		
		switch(name) {
		case NPC:
			if(type instanceof String) {
				HashMap<String, Boolean> npcData = new HashMap<String, Boolean>();
				npcData.put((String) type, false);
				type = npcData;
			}
			break;
		}
		
		this.data = type;
		this.finishCondition = finishCondition;
	}
	
	public TriggerType getName() {
		return name;
	}
	
	public int getFinishCondition() {
		return finishCondition;
	}
	
	public void setFinishCondition(int value) {
		finishCondition = value;
	}
	
	public Material getItem() {
		if(name == TriggerType.ITEM) {
			return (Material) data;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<Location, Integer> getLocationData() {
		if(name == TriggerType.LOCATION) {
			return (HashMap<Location, Integer>) data;
		}
		return null;
	}
	
	public EntityType getKillEntity() {
		if(name == TriggerType.KILLENTITY) {
			return (EntityType) data;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Boolean> getNPCData() {
		if(name == TriggerType.NPC) {
			return (HashMap<String, Boolean>) data;
		}
		return null;
	}
	
	public String getNPCName() {
		return (String) getNPCData().keySet().toArray()[0];
	}
	
	public boolean equals(Trigger trigger) {
		return (name == trigger.name && data == trigger.data);
	}
}
