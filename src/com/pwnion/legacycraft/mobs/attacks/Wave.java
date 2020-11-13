package com.pwnion.legacycraft.mobs.attacks;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

/**
 * An AOE attack that spreads out from the target as particles
 * 
 * Parameters:
 * Particle (type, colour?), Speed (the distance travelled per tick), Length (amount of particles that travel per line), Length Spacing (the distance between each particle in the line), 
 * Arc (the width of the wave), Arc Spacing (the distance, in degrees, between each line of particles), Offset (something to allow particles to originate from the head, feet, etc.) 
 */
public class Wave extends Attack {


	public Wave(int weight, int cooldown) {
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
