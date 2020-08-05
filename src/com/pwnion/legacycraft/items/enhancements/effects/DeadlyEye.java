package com.pwnion.legacycraft.items.enhancements.effects;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.items.enhancements.RangedEnhancement;

public class DeadlyEye implements RangedEnhancement {
	
	private static final double DMG_PERCENT_ADD = 0.1;
	
	@Override
	public void onHit(ItemStack item, LivingEntity wielder, LivingEntity target, double damage) {
		target.damage(damage * DMG_PERCENT_ADD);
	}
}
