package com.pwnion.legacycraft.abilities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.pwnion.legacycraft.LegacyCraft;

public class Util {

	public static final Vector VectorCalc(double yaw, double pitch, double dist) {
		pitch = Math.toRadians(pitch + 90);
		yaw  = Math.toRadians(yaw + 90);
		double x = Math.sin(pitch) * Math.cos(yaw);
		double y = Math.cos(pitch);
		double z = Math.sin(pitch) * Math.sin(yaw);
		Vector vector = new Vector(x, y, z);
		vector.multiply(dist);
		return vector;
	}

	public static final Vector VectorCalc(Entity e, double dist) {
		return VectorCalc(e.getLocation().getYaw(), e.getLocation().getPitch(), dist);
	}

	public static final Vector VectorCalc(Location pos1, Location pos2) {
		if(pos1.getWorld() != pos2.getWorld()) {
			return null;
		}
		return new Vector(pos2.getX() - pos1.getX(), pos2.getY() - pos1.getY(), pos2.getZ() - pos1.getZ());
	}

	public static final Location addY(Location loc, double plusY) {
		return loc.clone().add(0, plusY, 0);
	}

	public static final Vector addY(Vector vec, double plusY) {
		return vec.clone().add(new Vector(0, plusY, 0));
	}

	public static final void Portal(Player p) {
		double distFromPlayer = 1.5;

		int delay = 1;
		int stepsSpiral = 40; //the amount of locations (not including the starting location)
		int stepsCircle = 80;
		double radius = 1.25;
		double rotation = 360 * 9;

		double rotationSpiral = (stepsSpiral / (stepsSpiral + stepsCircle)) * rotation;
		double rotationCircle = rotation - rotationSpiral;

		Location centre = p.getEyeLocation();
		Vector vec = VectorCalc(centre.getYaw(), centre	.getPitch(), distFromPlayer);
		centre.add(vec);


		int count = 4; //the amount of particles to spawn per location
		Particle particle = Particle.ENCHANTMENT_TABLE;

		ArrayList<Location> spiral = Spiral(centre, radius, rotationSpiral, stepsSpiral);
		spiral.trimToSize();
		ArrayList<Location> circle = Circle(centre, VectorCalc(centre, spiral.get(spiral.size() - 1)), rotationCircle, stepsCircle);

		int i = 0;
		for(Location point : spiral) {
			if(i == 0) {
				centre.getWorld().spawnParticle(particle, point, count, 0, 0, 0, 0, null, true);
			} else {
				Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
					public void run() {
						centre.getWorld().spawnParticle(particle, point, count, 0, 0, 0, 0, null, true);
					}
				}, delay * i);
			}
			i++;
		}
		for(Location point : circle) {
			Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
				public void run() {
					centre.getWorld().spawnParticle(particle, point, count, 0, 0, 0, 0, null, true);
				}
			}, delay * i);
			i++;
		}
	}

	public static final ArrayList<Location> Spiral(Location centre, double radius, double rotation, int steps) {

		double radiusPerStep = radius / (steps - 1);
		double rotPerStep = Math.toRadians(rotation / (steps - 1));

		Vector axis = VectorCalc(centre.getYaw(), centre.getPitch(), 1);
		Vector up = new Vector(0, 1, 0);
		Vector cross = axis.clone().crossProduct(up);
		if(cross.length() == 0) {
			cross = new Vector(1, 0, 0);
		}

		up.rotateAroundAxis(cross, Math.toRadians(90) - axis.angle(up));

		ArrayList<Location> spiral = new ArrayList<Location>(steps + 1);

		for(int i = 0; i <= steps; i++) {
			Vector pointer = up.clone();
			pointer.rotateAroundAxis(axis, rotPerStep * i);
			pointer.multiply(radiusPerStep * i);

			spiral.add(centre.clone().add(pointer));
		}
		return spiral;
	}

	public static final ArrayList<Location> Circle(Location centre, Vector pointer, double rotation, int steps) {
		ArrayList<Location> circle = new ArrayList<Location>(steps + 1);

		double rotPerStep = Math.toRadians(rotation / (steps - 1));

		Vector axis = VectorCalc(centre.getYaw(), centre.getPitch(), 1);

		for(int i = 0; i<= steps; i++) {
			Vector pointerClone = pointer.clone();
			pointerClone.rotateAroundAxis(axis, rotPerStep * i);

			circle.add(centre.clone().add(pointerClone));
		}

		return circle;
	}



}
