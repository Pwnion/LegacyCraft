package com.pwnion.legacycraft.abilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Util {

	private static final Vector VectorCalc(double yaw, double pitch, double radius) {
		pitch = ((pitch + 90) * Math.PI) / 180;
		yaw  = ((yaw + 90)  * Math.PI) / 180;
		double x = Math.sin(pitch) * Math.cos(yaw);
		double y = Math.cos(pitch);
		double z = Math.sin(pitch) * Math.sin(yaw);
		Vector vector = new Vector(x, y, z);
		vector.multiply(radius);
		return vector;
	}
	
	public static final void Spiral(Player p) {
		Location centre = p.getEyeLocation();
		
		int delay = 1;
		int steps = 40;
		double radius = 1;
		double rotation = 1080;
		double distFromPlayer = 1.5;
		
		double radiusPerStep = radius / (steps - 1);
		double rotPerStep = Math.toRadians(rotation / (steps - 1));

		Vector vec = VectorCalc(centre.getYaw(), centre.getPitch(), distFromPlayer);
		centre.add(vec);
		Vector up = new Vector(0, 1, 0);
		Vector cross = vec.clone().crossProduct(up);
		if(cross.length() == 0) {
			cross = new Vector(1, 0, 0);
		}
		
		up.rotateAroundAxis(cross, Math.toRadians(90) - vec.angle(up));
		for(int i = 0; i <= steps; i++) {
			Vector pointer = up.clone();
			pointer.rotateAroundAxis(vec, rotPerStep * i);
			pointer.multiply(radiusPerStep * i);
			Location particleLoc = centre.clone().add(pointer);
			
			p.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, particleLoc, 4, 0, 0, 0, 0, null, true);
		}
		
	}
	
}
