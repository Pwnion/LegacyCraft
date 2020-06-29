/**
 * 
 */
package com.pwnion.legacycraft.items.enhancements.effects;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.items.ItemManager;
import com.pwnion.legacycraft.items.ItemStat;
import com.pwnion.legacycraft.items.enhancements.Enhancement;
import com.pwnion.legacycraft.items.enhancements.EnhancementType;

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
	public EnhancementType getType() {
		return null;
	}

	@Override
	public void apply(LivingEntity wielder, LivingEntity target, double damage) {
		
	}

	@Override
	public void onEquip(ItemStack item, boolean initial) {
		if(initial) {
			ItemManager.getItemData(item).addToStat(ItemStat.RANGE, 1);
		}
	}

}
