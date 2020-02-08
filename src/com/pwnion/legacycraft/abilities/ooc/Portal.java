package com.pwnion.legacycraft.abilities.ooc;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.destroystokyo.paper.ParticleBuilder;
import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.abilities.Util;

public enum Portal {
	NEUTRAL(new Location(Bukkit.getWorld("Neutral"), 0, 0, 0, 0, 0), Color.fromRGB(255, 255, 255), 1, 12),
	IGNIS(new Location(Bukkit.getWorld("Ignis"), 0, 0, 0, 0, 0), Color.fromRGB(255, 0, 0), 13, 24),
	TERRA(new Location(Bukkit.getWorld("Terra"), 0, 0, 0, 0, 0), Color.fromRGB(0, 255, 0), 25, 36),
	AER(new Location(Bukkit.getWorld("Aer"), 0, 0, 0, 0, 0), Color.fromRGB(255, 255, 0), 37, 48),
	AQUA(new Location(Bukkit.getWorld("Aqua"), 0, 0, 0, 0, 0), Color.fromRGB(0, 0, 255), 49, 60);

	@SuppressWarnings("unused")
	private final Location dest;
	private final Color colour;
	@SuppressWarnings("unused")
	private final ArrayList<Integer> customModelDataRange;
	
	private Portal(Location dest, Color colour, int customModelDataMin, int customModelDataMax) {
		ArrayList<Integer> customModelDataRange = new ArrayList<Integer>(12);
		for(int i = customModelDataMin; i <= customModelDataMax; i++) {
			customModelDataRange.add(i);
		}
		
		this.customModelDataRange = customModelDataRange;
		this.dest = dest;
		this.colour = colour;
	}
	
	public static final void activate(Player p, Portal portal) {
		int delay = 1;
		int count = 4;
		int stepsSpiral = 60;
		int stepsCircle = 100;
		double radius = 1.25;
		double rotation = 360 * 9;
		double distFromPlayer = 1.5;

		double rotationSpiral = ((double) stepsSpiral / (double) (stepsSpiral + stepsCircle)) * rotation;
		double rotationCircle = rotation - rotationSpiral;

		Location centre = p.getEyeLocation();
		Vector vec = Util.vectorCalc(centre.getYaw(), centre.getPitch(), distFromPlayer);
		centre.add(vec);

		ArrayList<Location> spiral = Util.spiral(centre, radius, rotationSpiral, stepsSpiral);
		spiral.trimToSize();
		
		ArrayList<Location> circle = Util.circle(centre, Util.vectorCalc(centre, spiral.get(spiral.size() - 1)), rotationCircle, stepsCircle);
		
		ParticleBuilder particle = new ParticleBuilder(Particle.REDSTONE);
		particle.color(portal.colour, 0.7f);
		particle.count(count);
		particle.offset(0, 0, 0);
		particle.force(true);
		
		Consumer<Location> spawnParticle = (point) -> {
			particle.location(point);
			particle.receivers(100);
			particle.spawn();
		};
		
		int i = 0;
		for(Location point : spiral) {
			if(i == 0) {
				spawnParticle.accept(point);
			} else {
				Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
					public void run() {
						spawnParticle.accept(point);
					}
				}, delay * i);
			}
			i++;
		}
		for(Location point : circle) {
			Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
				public void run() {
					spawnParticle.accept(point);
				}
			}, delay * i);
			i++;
		}
	}
}