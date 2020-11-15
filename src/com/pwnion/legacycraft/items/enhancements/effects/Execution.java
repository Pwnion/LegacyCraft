/**
 * 
 */
package com.pwnion.legacycraft.items.enhancements.effects;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.items.enhancements.MeleeEnhancement;

/**
 * Execution (do more damage if attacking from behind) <br>
 * 
 * +50% damage from behind
 * 
 * @author Zephreo
 */
public class Execution implements MeleeEnhancement {
	
	private static final double DMG_INCREASE = 0.5;

	@Override
	public void onHit(ItemStack item, LivingEntity wielder, LivingEntity target, double damage) {
		if(wielder.getFacing() == target.getFacing()) {
			target.damage(damage * DMG_INCREASE);
		}
		
	}
}
