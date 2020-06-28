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

	@Override
	public void apply(Entity wielder, LivingEntity target, double damage) {
		
	}
	
	@Override
	public void onEquip(ItemStack item) {
		final AttributeModifier att = new AttributeModifier(UUID.nameUUIDFromBytes("Heavy".getBytes()), "Heavy", -0.3, Operation.MULTIPLY_SCALAR_1);
		ItemMeta meta = item.getItemMeta();
		//Stop duplication
		meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, att);
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, att);
		item.setItemMeta(meta);
	}

}
