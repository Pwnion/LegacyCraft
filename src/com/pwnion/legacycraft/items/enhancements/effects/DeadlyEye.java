package com.pwnion.legacycraft.items.enhancements.effects;

import org.bukkit.entity.LivingEntity;

import com.pwnion.legacycraft.items.enhancements.RangedEnhancement;

public class DeadlyEye implements RangedEnhancement {
	
	@Override
	public String getName() {
		return "Deadly Eye";
	}
	
	private static final double DMG_PERCENT_ADD = 0.1;
	
	@Override
	public void onHit(LivingEntity wielder, LivingEntity target, double damage) {
		target.damage(damage * DMG_PERCENT_ADD);
	}
}
