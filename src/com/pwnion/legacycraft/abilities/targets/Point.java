package com.pwnion.legacycraft.abilities.targets;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

//Please use Util.vectorCalc() instead

@Deprecated
public class Point {
	
	//Returns a location relative to a given location
	public static final Location fromLocationInYawDir(Location loc, double distance, double heightMod) {
		Location locClone = loc.clone();
		float yaw = locClone.getYaw();
		yaw = (yaw < 0) ? (yaw + 360) : (yaw == 360) ? 0 : yaw;
		
		switch(Math.round((yaw / 90) - 0.5f)) {
		case 0:
			return locClone.add(new Vector(-distance * Math.sin(Math.toRadians(yaw)), heightMod, distance * Math.cos(Math.toRadians(yaw))));
		case 1:
			yaw -= 90;
			return locClone.add(new Vector(-distance * Math.cos(Math.toRadians(yaw)), heightMod, -distance * Math.sin(Math.toRadians(yaw))));
		case 2:
			yaw -= 180;
			return locClone.add(new Vector(distance * Math.sin(Math.toRadians(yaw)), heightMod, -distance * Math.cos(Math.toRadians(yaw))));
		case 3:
			yaw -= 270;
			return locClone.add(new Vector(distance * Math.cos(Math.toRadians(yaw)), heightMod, distance * Math.sin(Math.toRadians(yaw))));
		default:
			return locClone;
		}
	}
	
	public static final Location fromLocationInYawDir(Entity e, double distance, double heightMod) {
		return fromLocationInYawDir(e.getLocation(), distance, heightMod);
	}
	
	public static final Location fromLocationInPitchDir(Location loc, double distance) {
		return fromLocationInYawDir(loc, distance, -distance * Math.sin(Math.toRadians(loc.getPitch())));
	}
	
	public static final Location fromLocationInPitchDir(Entity e, double distance) {
		return fromLocationInYawDir(e.getLocation(), distance, -distance * Math.sin(Math.toRadians(e.getLocation().getPitch())));
	}
	
	public static final Location fromLocationInDir(Location loc, double distance) {
		return fromLocationInYawDir(loc, distance, -distance * Math.sin(Math.toRadians(loc.getPitch())));
	}
	
	public static final Location fromLocationInDir(Entity e, double distance) {
		return fromLocationInYawDir(e.getLocation(), distance, -distance * Math.sin(Math.toRadians(e.getLocation().getPitch())));
	}
}