package com.pwnion.legacycraft.abilities.ooc;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.destroystokyo.paper.ParticleBuilder;
import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.abilities.Util;
import com.pwnion.legacycraft.abilities.targets.Point;

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
		int count = 4;
		float size = 1f;
		int stepsSpiral = 120;
		int stepsCircle = 200;
		double radius = 1.25;
		double rotation = 360 * 12;
		double distFromPlayer = 1.5;

		double rotationSpiral = ((double) stepsSpiral / (double) (stepsSpiral + stepsCircle)) * rotation;
		double rotationCircle = rotation - rotationSpiral;

		Location centre = p.getEyeLocation();
		Vector vec = Util.vectorCalc(centre.getYaw(), centre.getPitch(), distFromPlayer);
		centre.add(vec);

		ArrayList<Location> spiral = Util.spiral(centre, radius, rotationSpiral, stepsSpiral);
		spiral.trimToSize();
		
		ArrayList<Location> circle = Util.circle(centre, Util.vectorCalc(centre, spiral.get(spiral.size() - 1)), rotationCircle, stepsCircle);
		
		ParticleBuilder particles = new ParticleBuilder(Particle.REDSTONE);
		particles.color(portal.colour, size);
		particles.count(count);
		particles.offset(0, 0, 0);
		particles.force(true);
		
		ParticleBuilder ambientParticles = new ParticleBuilder(Particle.ENCHANTMENT_TABLE);
		ambientParticles.count(count);
		ambientParticles.extra(0D);
		ambientParticles.offset(0, 0, 0);
		ambientParticles.force(true);
		
		Consumer<Location> spawnParticles = (point) -> {
			particles.location(point);
			particles.receivers(100);
			
			ambientParticles.location(point);
			ambientParticles.receivers(100);
			
			particles.spawn();
			ambientParticles.spawn();
		};
		
		int i = 0;
		for(Location point : spiral) {
			if(i == 0) {
				spawnParticles.accept(point);
			} else {
				Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
					public void run() {
						spawnParticles.accept(point);
					}
				}, i);
			}
			i++;
		}
		for(Location point : circle) {
			Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
				public void run() {
					spawnParticles.accept(point);
				}
			}, i);
			i++;
		}
		
		ArrayList<ArmorStand> armourStands = new ArrayList<ArmorStand>(16);
		float pitch = p.getLocation().getPitch();
		float fixedPitch = pitch + (pitch < 0 ? 90 : -90);
		
		float yaw = p.getLocation().getYaw();
		
		//the point at where the armour stand is when the head is in the centre
		Location AScentre = centre.clone().subtract(0, 2, 0);
		Vector back = Util.vectorCalc(yaw, pitch, 0.3125);
		AScentre.subtract(back);
		Vector up = Util.vectorCalc(yaw, pitch + 90, 0.24219);
		AScentre.add(up);
		
		for(float vertical = 0.9375f; vertical >= -0.9375f; vertical = vertical - 0.625f) {
			for(float horizontal = -0.9375f; horizontal <= 0.9375; horizontal = horizontal + 0.625f) {
				Location point = AScentre.clone();
				
				point = Point.fromLocationInYawDir(point, (vertical / 0.625) * 0.625 * (pitch < 0 ? -1 : 1) * (Math.cos(Math.toRadians(fixedPitch))), vertical);
				
				point.setYaw(point.getYaw() < 270 ? point.getYaw() + 90 : point.getYaw() - 270);
				
				point = Point.fromLocationInYawDir(point, horizontal, -(vertical / 0.625) * 0.625 * (1 - Math.abs(Math.sin(Math.toRadians(fixedPitch)))));
				
				armourStands.add(p.getWorld().spawn(point, ArmorStand.class, (e) -> {
					e.setGravity(false);
					e.setVisible(false);
					e.setInvulnerable(true);
					e.setHeadPose(new EulerAngle(Math.toRadians(pitch), Math.toRadians(yaw), 0));
				}));
			}
		}
		
		Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
			public void run() {
				int i = 0;
				for(ArmorStand e : armourStands) {
					ItemStack portalPiece = new ItemStack(Material.ITEM_FRAME);
					if(i != 6 && i != 7 && i != 10 && i != 11) {
						ItemMeta portalPieceMeta = portalPiece.getItemMeta();
						portalPieceMeta.setCustomModelData(i + 1);
						portalPiece.setItemMeta(portalPieceMeta);
						
						i++;
					}
					e.setHelmet(portalPiece);
				}
			}
		}, 1);
	}
}