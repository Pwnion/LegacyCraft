package com.pwnion.legacycraft.items;

import java.util.HashMap;

import com.pwnion.legacycraft.Util;

public enum ItemType {
	NONE,
	MELEE,
	SHORTSWORD(MELEE, 0, 1, 0),
	LONGSWORD(MELEE, 1, -1, 1),
	RANGED,
	BOW(RANGED, 0, 0, 0);
	
	ItemType category = null;
	private final HashMap<ItemStat, Integer> statMod = new HashMap<ItemStat, Integer>();
	
	ItemType(ItemType category, int attack, int speed, int range) {
		this.category = category;
		statMod.put(ItemStat.ATTACK, attack);
		statMod.put(ItemStat.SPEED, speed);
		statMod.put(ItemStat.RANGE, range);
	}
	
	ItemType() {}
	
	public int getMod(ItemStat stat) {
		return statMod.getOrDefault(stat, 0);
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
		return Util.getEnumFromString(ItemType.class, name);
	}
	
	@Override
	public String toString() {
		return Util.toTitleCase(this.name().replace("_", " "));
	}
	
	public boolean equalsAny(ItemType... types) {
		for(ItemType type : types) {
			if(type == this) {
				return true;
			}
		}
		return false;
	}
}
