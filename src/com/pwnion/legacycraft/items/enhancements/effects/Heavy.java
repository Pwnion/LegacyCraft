package com.pwnion.legacycraft.items.enhancements.effects;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pwnion.legacycraft.items.enhancements.Enhancement;
import com.pwnion.legacycraft.items.enhancements.EnhancementType;

public class Heavy implements Enhancement {

	@Override
	public EnhancementType getType() {
		return EnhancementType.WEAPON_HIT;
	}
	
	private static final double DMG_PERCENT_ADD = 0.1;
	private static final double SPEED_PERCENT_REDUCE = 0.3;

	@Override
	public void apply(LivingEntity wielder, LivingEntity target, double damage) {
		target.damage(damage * DMG_PERCENT_ADD);
	}
	
	@Override
	public void onEquip(ItemStack item, boolean initial) {
		final AttributeModifier att = new AttributeModifier(UUID.nameUUIDFromBytes("Heavy".getBytes()), "Heavy", -SPEED_PERCENT_REDUCE, Operation.MULTIPLY_SCALAR_1);
		ItemMeta meta = item.getItemMeta();
		//Stop duplication
		meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, att);
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, att);
		item.setItemMeta(meta);
	}

}
