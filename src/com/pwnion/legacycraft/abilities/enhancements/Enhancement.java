package com.pwnion.legacycraft.abilities.enhancements;

import javax.annotation.Nullable;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;

public interface Enhancement {
	
	public String getName();

	public EnhancementType getType();
	
	public void apply(Entity wielder, @Nullable Damageable target, double damage);
	
}
