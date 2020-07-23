/**
 * 
 */
package com.pwnion.legacycraft.items.enhancements.effects.melee;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.items.ItemManager;
import com.pwnion.legacycraft.items.ItemStat;
import com.pwnion.legacycraft.items.ItemType;
import com.pwnion.legacycraft.items.enhancements.Enhancement;

/**
 * Blade Dancer (you can reach further)
 * 
 * @author Zephreo
 */
public class BladeDancer implements Enhancement {
	
	@Override
	public String getName() {
		return "Blade Dancer";
	}
	
	@Override
	public ItemType getRestriction() {
		return ItemType.MELEE;
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
