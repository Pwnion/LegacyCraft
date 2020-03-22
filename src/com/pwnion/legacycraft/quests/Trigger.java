package com.pwnion.legacycraft.quests;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class Trigger {
	
	String name;
	Object type;

	public Trigger(String name, Object type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}
	
	public Material getItem() {
		if(name == "item") {
			return (Material) type;
		}
		return null;
	}
	
	public HashMap<Location, Integer> getLocationData() {
		if(name == "location") {
			return (HashMap<Location, Integer>) type;
		}
		return null;
	}
	
	public EntityType getKillEntity() {
		if(name == "killentity") {
			return (EntityType) type;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Boolean> getNPCData() {
		if(name == "npc") {
			return (HashMap<String, Boolean>) type;
		}
		return null;
	}
	
	public String getNPCName() {
		return (String) getNPCData().keySet().toArray()[0];
	}
	
	public boolean equals(Trigger trigger) {
		return (name == trigger.name && type == trigger.type);
	}
}
