package com.pwnion.legacycraft.mobs.attacks;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

/**
 * Send a particle projectile towards a target
 * 
 * Parameters:
 * Particle type (colour?), Speed (the distance travelled per tick), Length (amount of particles that travel), Spacing (the distance between each particle in the line), Offset (something to allow particles to originate from the head, feet, etc.)
 */
public class Projectile extends Attack {

	public Projectile(int weight, int cooldown) {
		super(weight, cooldown);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void target(LivingEntity self, Location target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Location isValid(LivingEntity self) {
		// TODO Auto-generated method stub
		return null;
	}



}
