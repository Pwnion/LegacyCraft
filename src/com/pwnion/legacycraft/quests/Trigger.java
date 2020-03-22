package com.pwnion.legacycraft.quests;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;

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
	
	public Location getLocation() {
		if(name == "location") {
			return (Location) type;
		}
		return null;
	}
	
	public boolean equals(Trigger trigger) {
		return (name == trigger.name && type == trigger.type);
	}
}
