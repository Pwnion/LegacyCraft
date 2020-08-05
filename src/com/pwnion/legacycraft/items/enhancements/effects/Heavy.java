package com.pwnion.legacycraft.items.enhancements.effects;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.items.ItemManager;
import com.pwnion.legacycraft.items.ItemStat;
import com.pwnion.legacycraft.items.enhancements.MeleeEnhancement;

public class Heavy implements MeleeEnhancement {
	
	private static final double DMG_PERCENT_ADD = 0.1;
	private static final double SPEED_REDUCE = 0.7;

	@Override
	public void onHit(ItemStack item, LivingEntity wielder, LivingEntity target, double damage) {
		target.damage(damage * DMG_PERCENT_ADD);
	}
	
	@Override
	public void onEquip(ItemStack item, boolean initial) {
		ItemManager.getItemData(item).incrementStat(ItemStat.SPEED, -SPEED_REDUCE);
	}

	@Override
	public void onRemove(ItemStack item) {
		ItemManager.getItemData(item).incrementStat(ItemStat.SPEED, SPEED_REDUCE);
	}
}
