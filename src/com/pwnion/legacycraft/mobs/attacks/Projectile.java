package com.pwnion.legacycraft.mobs.attacks;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.destroystokyo.paper.ParticleBuilder;
import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.mobs.LCEntity;

/**
 * Send a particle projectile towards a target
 * 
 * Parameters:
 * Particle type (colour?), Speed (the distance travelled per tick), Amount (amount of particles that travel), Spacing (the distance between each particle in the line), Offset (something to allow particles to originate from the head, feet, etc.)
 */
public class Projectile extends Attack {
	
	final ParticleBuilder particle;
	final double speed;
	final int delay;
	final int amount;
	final double spacing;
	final Vector offset;
	final double distance;
	
	final double damageRadius;
	final double damage;

	public Projectile(int weight, int cooldown, ParticleBuilder particle, double speed, int delay, int amount, double spacing, @Nullable Vector offset, double damageRadius, double damage, double distance) {
		super(weight, cooldown);
		
		if (particle == null) {
			throw new NullPointerException("Invalid particle");
		}
		if (speed <= 0) {
			throw new IllegalArgumentException("Invalid speed, " + speed);
		}
		if (amount <= 0) {
			throw new IllegalArgumentException("Invalid amount, " + amount);
		}
		if (damageRadius < 0) {
			throw new IllegalArgumentException("Invalid damageRadius, " + damageRadius);
		}
		if (damage < 0) {
			throw new IllegalArgumentException("Invalid damage, " + damage);
		}
		
		this.particle = particle;
		this.speed = speed;
		this.delay = delay;
		this.amount = amount;
		this.spacing = spacing;
		this.offset = offset == null ? new Vector(0, 0, 0) : offset;
		this.distance = distance;
		
		this.damageRadius = damageRadius;
		this.damage = damage;
	}

	@Override
	public void makeAttack(LivingEntity self, Location target) {
		createProjectile(self, target, particle, speed, delay, amount, spacing, offset, damageRadius, damage, distance);
	}
	
	static void createProjectile(LivingEntity self, Location target, ParticleBuilder particle, double speed, int delay, int amount, double spacing, Vector offset, double damageRadius, double damage, double distance) {
		Location origin = self.getEyeLocation().add(offset);
		
		Vector targetVec = Util.getRelativeVec(origin, target);
		
		Vector line = targetVec.clone().normalize().multiply(speed);
		Vector spacingVec = targetVec.clone().normalize().multiply(-spacing);
		
		int time;
		
		if (spacing < 0) {
			time = delay;
			spacing = spacing * spacing * -1; // make spacing m^2 for cheaper distance comparisons (preserving negatives)
		} else {
			time = delay + amount - 1 + (int) (targetVec.length() / speed);
			spacing = spacing * spacing; // make spacing m^2 for cheaper distance comparisons
		}
		
		final double spacing0 = spacing; // Make spacing final for runnable
		final double distance0 = distance * distance;
		
		BukkitTask task = Bukkit.getScheduler().runTaskTimer(LegacyCraft.getPlugin(), new Runnable() {
			boolean spawning = true;
			int counter = 1;
			
			@Override
			public void run() {
				Vector vec = line.clone().multiply(counter++);
				double vecLenSq = vec.lengthSquared();
				Location particlePos = self.getEyeLocation().add(offset).add(vec);
				int spawnAmount = amount - 1;
		
				if(spawning) {
					double distance = vec.lengthSquared();
					int i = amount - 1;
					for (; distance > 0 && i > 0; i--) {
						distance -= spacing0;
					}
					if (distance > 0) {
						spawning = false;
					}
					spawnAmount -= i;
				}
				
				if(vecLenSq < distance0) {
					particle.location(particlePos);
					for(LivingEntity le : particlePos.getNearbyLivingEntities(damageRadius)) {
						if (!le.equals(self)) {
							le.damage(damage, self);
						}
					}
					particle.spawn();
				}
				
				//Spawn trailing particles
				for (int i = 1; i <= spawnAmount; i++) {
					if(vecLenSq - spacing0 * i < distance0) {
						particle.location(particlePos.add(spacingVec));
						for(LivingEntity le : particlePos.getNearbyLivingEntities(damageRadius)) {
							if (!le.equals(self)) {
								le.damage(damage, self);
							}
						}
						particle.spawn();
					}
				}
			}
		}, delay, 1);
		LegacyCraft.addTaskToBeCancelled(task, time);
	}

	@Override
	public Location getValidTarget(LCEntity self, Entity attacking) {
		if(attacking instanceof LivingEntity) {
			Location origin = self.entity.getEyeLocation().add(offset);
			Location targetLocation = ((LivingEntity) attacking).getEyeLocation();
			Vector attVec = Util.getRelativeVec(origin, targetLocation).normalize().multiply(distance);
			return origin.add(attVec);
		}
		return null;
	}
}
