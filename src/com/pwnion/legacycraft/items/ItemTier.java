package com.pwnion.legacycraft.items;

import com.pwnion.legacycraft.Util;

public enum ItemTier {
	NONE(1),
	WEAK(1), 
	STABLE(3), 
	TOUGH(5), 
	STRONG(7);
	
	final int power;
	
	private ItemTier(int power) {
		this.power = power;
	}
	
	/**
	 * gets the ItemTier from a string. IgnoresCase, Ignores Leading/Trailing whitespace, Spaces treated as '_'
	 * 
	 * @param name
	 * @return
	 * 
	 * @Nullable if none found
	 */
	public static ItemTier fromString(String name) {
	    for (ItemTier enumValue : ItemTier.values()) {
	        if (enumValue.toString().equalsIgnoreCase(name.replace(" ", "_"))) {
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
