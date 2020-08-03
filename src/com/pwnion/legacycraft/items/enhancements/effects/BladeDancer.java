/**
 * 
 */
package com.pwnion.legacycraft.items.enhancements.effects;

import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.items.ItemManager;
import com.pwnion.legacycraft.items.ItemStat;
import com.pwnion.legacycraft.items.enhancements.MeleeEnhancement;

/**
 * Blade Dancer (you can reach further)
 * 
 * @author Zephreo
 */
public class BladeDancer implements MeleeEnhancement {
	
	@Override
	public String getName() {
		return "Blade Dancer";
	}

	@Override
	public void onEquip(ItemStack item, boolean initial) {
		if(initial) {
			ItemManager.getItemData(item).addToStat(ItemStat.RANGE, 1);
		}
	}

	@Override
	public void onRemove(ItemStack item) {
		ItemManager.getItemData(item).addToStat(ItemStat.RANGE, -1);
	}
}
