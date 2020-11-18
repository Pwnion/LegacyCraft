package com.pwnion.legacycraft.mobs.attacks;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.pwnion.legacycraft.mobs.LCEntity;

public abstract class Attack {
	
	public int weight;
	public int cooldown;
	
	public Attack(int weight, int cooldown) {
		if(weight <= 0) {
			throw new IllegalArgumentException("Invalid weight, '" + weight + "'");
		}
		if(cooldown <= 0) {
			throw new IllegalArgumentException("Invalid cooldown, '" + cooldown + "'");
		}
		this.weight = weight;
		this.cooldown = cooldown;
	}

	/**
	 * Executes the attack on the given target.
	 * 
	 * @param self
	 * @param target
	 */
	public abstract void makeAttack(LivingEntity self, Location target);
	
	/**
	 * Gets the target to attack or null if attack cannot be made.
	 * 
	 * @param self
	 * @return
	 */
	public Location getValidTarget(LivingEntity self) {
		LCEntity lce = LCEntity.get(self);
		if (lce == null) {
			return null;
		}
		return getValidTarget(lce, LCEntity.getAttacking(self));
	}
	
	public abstract Location getValidTarget(LCEntity self, Entity target);

}
