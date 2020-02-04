package com.pwnion.legacycraft.abilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Util {

	private static final Vector VectorCalc(Location centre, double yaw, double pitch, double radius) {
		yaw += 180;
		Vector vec = new Vector(0, 0, radius);
		vec.rotateAroundY(yaw);
		vec.rotateAroundNonUnitAxis(vec.getCrossProduct(new Vector(0, 1, 0)), pitch);
		return vec;
	}
	
	public static final void Spiral(Player p) {
		Location centre = p.getEyeLocation();
		
		int delay = 1;
		int steps = 10;
		double radius = 1;
		double rotation = 1080;
		double distFromPlayer = 1;
		
		double radiusPerStep = radius / steps;
		double rotPerStep = rotation / steps;
		
		Vector vec = VectorCalc(centre, centre.getYaw(), centre.getPitch(), distFromPlayer);
		centre.add(vec);
		Vector up = new Vector(0, 1, 0);
		Vector cross = vec.crossProduct(up);
		if(cross.length() == 0) {
			cross = new Vector(1, 0, 0);
		}
		up.rotateAroundNonUnitAxis(cross, 90 - Math.toDegrees(vec.angle(up)));
		
		for(int i = 0; i < steps; i++) {
			up.rotateAroundNonUnitAxis(vec, rotPerStep).normalize();
			up.multiply(radiusPerStep);
			Location particleLoc = centre.clone().add(up);
			
			p.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, particleLoc, steps, 0, 0, 0, 0, null, true);
		}
		
	}
	
}
