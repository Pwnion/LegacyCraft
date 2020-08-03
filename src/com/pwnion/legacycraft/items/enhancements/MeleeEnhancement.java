package com.pwnion.legacycraft.items.enhancements;

import com.pwnion.legacycraft.items.ItemType;

/**
 * Enhancement with default restriction of Melee
 * 
 * @author Zephreo
 *
 */
public interface MeleeEnhancement extends Enhancement {
	public default ItemType getRestriction() {
		return ItemType.MELEE;
	}
}
