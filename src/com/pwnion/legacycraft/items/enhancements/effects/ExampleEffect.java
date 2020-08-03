package com.pwnion.legacycraft.items.enhancements.effects;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.items.ItemTier;
import com.pwnion.legacycraft.items.ItemType;
import com.pwnion.legacycraft.items.enhancements.Enhancement;

public class ExampleEffect implements Enhancement {

	/*
	 * This Example effect deals 5 extra damage to the target
	 */
	
	@Override
	public String getName() {
		return "Example Effect";
	}
	
	@Override
	public ItemType getRestriction() {
		return null;
	}
	
	@Override
	public void onHit(LivingEntity wielder, LivingEntity target, double damage) {
		target.damage(5);
	}

	@Override
	public void onSwing(LivingEntity wielder) {
		
	}

	@Override
	public void onRemove(ItemStack item) {
		
	}

	@Override
	public void onEquip(ItemStack item, boolean initial) {
		
	}
}
