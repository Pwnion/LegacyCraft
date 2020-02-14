package com.pwnion.legacycraft.abilities;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Util {

	public static final Vector vectorCalc(double yaw, double pitch, double dist) {
		pitch = Math.toRadians(pitch + 90);
		yaw  = Math.toRadians(yaw + 90);
		double x = Math.sin(pitch) * Math.cos(yaw);
		double y = Math.cos(pitch);
		double z = Math.sin(pitch) * Math.sin(yaw);
		Vector vector = new Vector(x, y, z);
		vector.multiply(dist);
		return vector;
	}

	public static final Vector vectorCalc(Entity e, double dist) {
		return vectorCalc(e.getLocation().getYaw(), e.getLocation().getPitch(), dist);
	}

	public static final Vector vectorCalc(Location pos1, Location pos2) {
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

	public static final ArrayList<Location> spiral(Location centre, double radius, double rotation, int steps) {

		double radiusPerStep = radius / (steps - 1);
		double rotPerStep = Math.toRadians(rotation / (steps - 1));

		Vector axis = vectorCalc(centre.getYaw(), centre.getPitch(), 1);
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

	public static final ArrayList<Location> circle(Location centre, Vector pointer, double rotation, int steps) {
		ArrayList<Location> circle = new ArrayList<Location>(steps + 1);

		double rotPerStep = Math.toRadians(rotation / (steps - 1));

		Vector axis = vectorCalc(centre.getYaw(), centre.getPitch(), 1);

		for(int i = 0; i<= steps; i++) {
			Vector pointerClone = pointer.clone();
			pointerClone.rotateAroundAxis(axis, rotPerStep * i);

			circle.add(centre.clone().add(pointerClone));
		}

		return circle;
	}

	public static final void playSoundWithoutConflict(Location centre, Sound sound, SoundCategory soundCategory, float volume, float pitch, double radius) {
		centre.getNearbyEntities(radius, radius, radius).forEach((e) -> {
			if(e instanceof Player) {
				((Player) e).playSound(centre, sound, soundCategory, volume, pitch);
			}
		});
	}
	
	public static final void playSoundWithoutConflict(Location centre, Sound sound, SoundCategory soundCategory, float volume, float pitch) {
		double radius = volume < 1f ? 16D : 16D * volume;
		playSoundWithoutConflict(centre, sound, soundCategory, volume, pitch, radius);
	}
	
	public static final void playSoundWithoutConflict(Location centre, Sound sound, float volume, float pitch) {
		playSoundWithoutConflict(centre, sound, SoundCategory.PLAYERS, volume, pitch);
	}
	
	public static final void stopSurroundingSound(Sound sound, SoundCategory soundCategory, Location centre, double radius) {
		centre.getNearbyEntities(radius, radius, radius).forEach((e) -> {
			if(e instanceof Player) {
				((Player) e).stopSound(sound, soundCategory);
			}
		});
	}
	
	public static final void stopSurroundingSound(Location centre, Sound sound, double radius) {
		stopSurroundingSound(sound, SoundCategory.PLAYERS, centre, radius);
	}
	
	public static final void stopSurroundingSound(Location centre, Sound sound) {
		stopSurroundingSound(sound, SoundCategory.PLAYERS, centre, 16);
	}
}
