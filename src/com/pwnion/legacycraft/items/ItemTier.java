package com.pwnion.legacycraft.items;

public enum ItemTier {
	NONE,
	WEAK, 
	STABLE, 
	TOUGH, 
	STRONG;
	
	public static ItemTier fromString(String name) {

	    for (ItemTier enumValue : ItemTier.values()) {
	        if (enumValue.toString().equalsIgnoreCase(name.replace(" ", "_"))) {
	            return enumValue;
	        }
	    }

	    return NONE;
	}
}
