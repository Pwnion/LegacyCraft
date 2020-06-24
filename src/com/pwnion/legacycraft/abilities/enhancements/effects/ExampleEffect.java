package com.pwnion.legacycraft.abilities.enhancements.effects;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;

import com.pwnion.legacycraft.abilities.enhancements.Enhancement;
import com.pwnion.legacycraft.abilities.enhancements.EnhancementType;

public class ExampleEffect implements Enhancement {

	/*
	 * This Example effect deals 5 extra damage to the target
	 */
	
	@Override
	public String getName() {
		return "Example Effect";
	}
	
	@Override
	public EnhancementType getType() {
		return EnhancementType.WeaponHit;
	}
	
	@Override
	public void apply(Entity wielder, Damageable target, double damage) {
		target.damage(5);
	}
}
