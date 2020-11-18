package com.pwnion.legacycraft.mobs.attacks;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.destroystokyo.paper.ParticleBuilder;
import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.mobs.LCEntity;

/**
 * An AOE attack that spreads out from the target as particles
 * 
 * Parameters:
 * Particle (type, colour?), Speed (the distance travelled per tick), Length (amount of particles that travel per line), Length Spacing (the distance between each particle in the line), 
 * Arc (the width of the wave), Arc Spacing (the distance, in degrees, between each line of particles), Offset (something to allow particles to originate from the head, feet, etc.) 
 */
public class Wave extends Projectile {
	
	final double arc;
	final double arcSpacing;


	public Wave(int weight, int cooldown, ParticleBuilder particle, double speed, int delay, int amount, double spacing, Vector offset, double damageRadius, double damage, double distance, double arc, double arcSpacing) {
		super(weight, cooldown, particle, speed, delay, amount, spacing, offset, damageRadius, damage, distance);
		
		if (arc <= 0 || arc > 360) {
			throw new IllegalArgumentException("Invalid arc, " + arc);
		}
		if (arcSpacing <= 0 || arc / 2 < arcSpacing) {
			throw new IllegalArgumentException("Invalid arcSpacing, " + arcSpacing);
		}
		
		this.arc = arc / 2;
		this.arcSpacing = arcSpacing;
	}

	@Override
	public void makeAttack(LivingEntity self, Location target) {
		Location origin = self.getEyeLocation().add(offset);
		target.setY(origin.getY());
		Vector vec = Util.getRelativeVec(origin, target);
		for(int i = 0; i < arc; i += arcSpacing) {
			Vector targetVec = vec.clone().rotateAroundY(i);
			createProjectile(self, origin.clone().add(targetVec), particle, speed, delay, amount, spacing, offset, damageRadius, damage, distance);
		}
	}
	
	@Override
	public Location getValidTarget(LCEntity self, Entity attacking) {
		if(attacking instanceof LivingEntity) {
			Location origin = self.entity.getEyeLocation().add(offset);
			Location targetLocation = ((LivingEntity) attacking).getEyeLocation();
			Vector attVec = Util.getRelativeVec(origin, targetLocation).setY(0).normalize().multiply(distance);
			return origin.add(attVec);
		}
		return null;
	}
}
