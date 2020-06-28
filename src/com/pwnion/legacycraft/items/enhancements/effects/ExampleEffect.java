package com.pwnion.legacycraft.items.enhancements.effects;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.items.enhancements.Enhancement;
import com.pwnion.legacycraft.items.enhancements.EnhancementType;

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
		return EnhancementType.WEAPON_HIT;
	}
	
	@Override
	public void apply(Entity wielder, LivingEntity target, double damage) {
		target.damage(5);
	}

	@Override
	public void onEquip(ItemStack item) {
		
	}
}
