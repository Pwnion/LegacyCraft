package com.pwnion.legacycraft.items.enhancements.effects.ranged;

import org.bukkit.entity.LivingEntity;

import com.pwnion.legacycraft.items.ItemType;
import com.pwnion.legacycraft.items.enhancements.Enhancement;

public class DeadlyEye implements Enhancement {
	
	@Override
	public String getName() {
		return "Deadly Eye";
	}

	@Override
	public ItemType getRestriction() {
		return ItemType.RANGED;
	}
	
	private static final double DMG_PERCENT_ADD = 0.1;
	
	@Override
	public void onHit(LivingEntity wielder, LivingEntity target, double damage) {
		target.damage(damage * DMG_PERCENT_ADD);
	}
}
