package com.pwnion.legacycraft.items;

public enum ItemType {
	NONE,
	SHORTSWORD,
	LONGSWORD,
	BOW;
	
	public static ItemType fromString(String name) {

	    for (ItemType enumValue : ItemType.values()) {
	        if (enumValue.toString().equalsIgnoreCase(name.replace(" ", "_"))) {
	            return enumValue;
	        }
	    }

	    return NONE;
	}
}
