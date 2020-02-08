package com.pwnion.legacycraft.abilities.ooc;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.abilities.Util;

public class Portal {
	public static final void activate(Player p) {
		double distFromPlayer = 1.5;

		int delay = 1;
		int stepsSpiral = 60; //the amount of locations (not including the starting location)
		int stepsCircle = 100;
		double radius = 1.25;
		double rotation = 360 * 9;

		double rotationSpiral = (stepsSpiral / (stepsSpiral + stepsCircle)) * rotation;
		double rotationCircle = rotation - rotationSpiral;

		Location centre = p.getEyeLocation();
		Vector vec = Util.vectorCalc(centre.getYaw(), centre.getPitch(), distFromPlayer);
		centre.add(vec);

		int count = 4; //the amount of particles to spawn per location
		Particle particle = Particle.ENCHANTMENT_TABLE;

		ArrayList<Location> spiral = Util.spiral(centre, radius, rotationSpiral, stepsSpiral);
		spiral.trimToSize();
		ArrayList<Location> circle = Util.circle(centre, Util.vectorCalc(centre, spiral.get(spiral.size() - 1)), rotationCircle, stepsCircle);

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
}
