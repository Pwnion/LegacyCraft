package com.pwnion.legacycraft.abilities.targets;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public abstract class Point {
	public static Location fromEntityInFacingDir(Entity e, int distance, int heightMod) {
		float yaw = e.getLocation().getYaw();
		yaw = (yaw < 0) ? (yaw + 360) : (yaw == 360) ? 0 : yaw;
		
		switch(Math.round((yaw / 90) - 0.5f)) {
		case 0:
			return e.getLocation().add(new Vector(-distance * Math.sin(Math.toRadians(yaw)), heightMod, distance * Math.cos(Math.toRadians(yaw))));
		case 1:
			yaw -= 90;
			return e.getLocation().add(new Vector(-distance * Math.cos(Math.toRadians(yaw)), heightMod, -distance * Math.sin(Math.toRadians(yaw))));
		case 2:
			yaw -= 180;
			return e.getLocation().add(new Vector(distance * Math.sin(Math.toRadians(yaw)), heightMod, -distance * Math.cos(Math.toRadians(yaw))));
		case 3:
			yaw -= 270;
			return e.getLocation().add(new Vector(distance * Math.cos(Math.toRadians(yaw)), heightMod, distance * Math.sin(Math.toRadians(yaw))));
		default:
			return e.getLocation();
		}
	}
}
