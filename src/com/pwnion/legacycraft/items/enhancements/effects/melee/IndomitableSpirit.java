/**
 * 
 */
package com.pwnion.legacycraft.items.enhancements.effects.melee;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.items.ItemType;
import com.pwnion.legacycraft.items.enhancements.Enhancement;

/**
 * Indomitable Spirit (the more health you lose the more damage you do)
 * 
 * @author Zephreo
 */
public class IndomitableSpirit implements Enhancement {
	
	@Override
	public String getName() {
		return "Indomitable Spirit";
	}
	
	@Override
	public ItemType getRestriction() {
		return ItemType.MELEE;
	}
	
	private static final double EVERY_VAL_PERCENT = 0.1; //Every 10% health lost
	private static final double DMG_INCREASE = 0.1; //Give 10% Damage Boost

	@Override
	public void onHit(LivingEntity wielder, LivingEntity target, double damage) {
		double mod = (1 - (wielder.getHealth() / wielder.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())) / EVERY_VAL_PERCENT;
		target.damage(damage * mod * DMG_INCREASE);
	}
}
