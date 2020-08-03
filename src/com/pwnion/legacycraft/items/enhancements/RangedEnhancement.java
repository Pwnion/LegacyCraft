package com.pwnion.legacycraft.items.enhancements;

import com.pwnion.legacycraft.items.ItemType;

/**
 * Enhancement with default restriction of Ranged
 * 
 * @author Zephreo
 *
 */
public interface RangedEnhancement extends Enhancement {
	public default ItemType getRestriction() {
		return ItemType.RANGED;
	}
}
