package com.pwnion.legacycraft.items.enhancements.effects;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pwnion.legacycraft.items.ItemManager;
import com.pwnion.legacycraft.items.ItemStat;
import com.pwnion.legacycraft.items.enhancements.Enhancement;
import com.pwnion.legacycraft.items.enhancements.EnhancementType;

public class Heavy implements Enhancement {

	@Override
	public EnhancementType getType() {
		return EnhancementType.WEAPON_HIT;
	}
	
	private static final double DMG_PERCENT_ADD = 0.1;
	private static final double SPEED_REDUCE = 0.3;

	@Override
	public void apply(LivingEntity wielder, LivingEntity target, double damage) {
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
