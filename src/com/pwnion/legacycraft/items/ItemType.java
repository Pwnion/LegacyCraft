package com.pwnion.legacycraft.items;

import java.util.HashMap;

import com.pwnion.legacycraft.Util;

public enum ItemType {
	NONE(0, 0, 0),
	SHORTSWORD(0, 1, -1),
	LONGSWORD(1, -1, 1),
	BOW(0, 0, 0);
	
	HashMap<ItemStat, Double> statMod = new HashMap<ItemStat, Double>();
	
	ItemType(double attack, double speed, double range) {
		statMod.put(ItemStat.ATTACK, attack);
		statMod.put(ItemStat.SPEED, speed);
		statMod.put(ItemStat.RANGE, range);
	}
	
	public double get(ItemStat stat) {
		return statMod.getOrDefault(stat, 0.0);
	}
	
	/**
	 * gets the ItemType from a string. IgnoresCase, Ignores Leading/Trailing whitespace, Spaces treated as '_'
	 * 
	 * @param name
	 * @return
	 * 
	 * @Nullable if none found
	 */
	public static ItemType fromString(String name) {
	    for (ItemType enumValue : ItemType.values()) {
	        if (enumValue.toString().equalsIgnoreCase(name.trim().replace(" ", "_"))) {
	            return enumValue;
	        }
	    }
	    return null;
	}
	
	@Override
	public String toString() {
		return Util.toTitleCase(this.name().replace("_", " "));
	}
}
