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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
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
	
	public static Location midLoc(Location loc1, Location loc2) {
		return new Location(loc1.getWorld(), (loc1.getX() + loc2.getX()) / 2, (loc1.getY() + loc2.getY()) / 2, (loc1.getZ() + loc2.getZ()) / 2);
	}
	
	public static final void activate(Player p, Portal portal) {
		int countParticle = 4; //How many particles per point
		float sizeParticle = 0.2f; //Size of redstone particle
		
		int stepsSpiral = 120; //The amount of points in the spiral
		double rotationSpiral = 360 * 3; //The amount the spiral rotates
		double radius = 1.25; //The radius of the spiral/circle
		double distFromPlayer = 1.1; //The distance from the player that the portal spawns

		int killDelay = Math.round(stepsSpiral / 2) + 30 * 20;
		
		//The centre of the portal
		Location centre = p.getEyeLocation();
		Vector vec = Util.vectorCalc(centre.getYaw(), centre.getPitch(), distFromPlayer);
		centre.add(vec);

		//Gets all the points on the spiral
		ArrayList<Location> spiral = Util.spiral(centre, radius, rotationSpiral, stepsSpiral);
		spiral.trimToSize();
		
		ParticleBuilder particles = new ParticleBuilder(Particle.REDSTONE);
		particles.color(portal.colour, sizeParticle);
		particles.count(countParticle);
		particles.offset(0, 0, 0);
		particles.force(true);
		
		ParticleBuilder ambientParticles = new ParticleBuilder(Particle.ENCHANTMENT_TABLE);
		ambientParticles.count(countParticle);
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
		
		final int startTick = Bukkit.getServer().getCurrentTick();
		final double rotPerStep = Math.toRadians(rotationSpiral / (stepsSpiral - 1));
		
		//A Vector that contains radius and yaw/pitch information
		final Vector pointer = Util.vectorCalc(centre, spiral.get(spiral.size() - 1));
		
		//The axis that the circle will rotate around
		final Vector axis = Util.vectorCalc(centre.getYaw(), centre.getPitch(), 1);
		
		BukkitTask portalTask = Bukkit.getServer().getScheduler().runTaskTimer(LegacyCraft.getPlugin(), new Runnable() {
				public void run() {
					int i = (Bukkit.getServer().getCurrentTick() - startTick) * 2;
					if(i < spiral.size() - 1) {
						spawnParticles.accept(spiral.get(i));
						spawnParticles.accept(spiral.get(i + 1));
					}

					if(i >= spiral.size() - 1) {
						Vector pointerClone = pointer.clone();
						
						pointerClone.rotateAroundAxis(axis, rotPerStep * i);
						spawnParticles.accept(centre.clone().add(pointerClone));
						
						pointerClone.rotateAroundAxis(axis, rotPerStep);
						spawnParticles.accept(centre.clone().add(pointerClone));
					}
				}
		}, 0, 1);
		
		float pitch = p.getLocation().getPitch();
		float fixedPitch = pitch + (pitch < 0 ? 90 : -90);
		float yaw = p.getLocation().getYaw();
		
		ArrayList<Location> points = new ArrayList<Location>(16);
		for(float vertical = 0.9375f; vertical >= -0.9375f; vertical = vertical - 0.625f) {
			for(float horizontal = -0.9375f; horizontal <= 0.9375; horizontal = horizontal + 0.625f) {
				Location point = Point.fromLocationInYawDir(centre, (vertical / 0.625) * 0.625 * (pitch < 0 ? -1 : 1) * (Math.cos(Math.toRadians(fixedPitch))), vertical);
				
				point.setYaw(point.getYaw() < 270 ? point.getYaw() + 90 : point.getYaw() - 270);
				
				point = Point.fromLocationInYawDir(point, horizontal, -(vertical / 0.625) * 0.625 * (1 - Math.abs(Math.sin(Math.toRadians(fixedPitch)))));
				
				points.add(point);
			}
		}
		
		Location loc1 = points.get(0).clone().add(0, 1.5, 0);
		Location loc2 = points.get(15).clone().add(0, 1.5, 0);
		
		loc1.setYaw(pitch < 0 ? (yaw < 180 ? yaw + 180 : yaw - 180) : yaw);
		loc2.setYaw(pitch < 0 ? (yaw < 180 ? yaw + 180 : yaw - 180) : yaw);
		
		final double yawMod = -0.26953 * Math.cos(Math.toRadians(Math.abs(pitch))) + 0.75 * Math.cos(Math.toRadians(Math.abs(fixedPitch)));
		final double vertMod = 0.26953 * Math.sin(Math.toRadians(Math.abs(pitch))) + 0.75 * Math.sin(Math.toRadians(Math.abs(fixedPitch)));
		
		loc1 = Point.fromLocationInYawDir(loc1, yawMod, vertMod);
		loc2 = Point.fromLocationInYawDir(loc2, yawMod, vertMod);
		
		Location mid = midLoc(loc1, loc2);
		
		Vector mod = centre.toVector().subtract(mid.toVector());
		
		ArrayList<ArmorStand> armourStands = new ArrayList<ArmorStand>(16);
		points.forEach((point) -> {
			armourStands.add(p.getWorld().spawn(point.add(mod), ArmorStand.class, (e) -> {
				e.setGravity(false);
				//e.setVisible(false);
				e.setInvulnerable(true);
				e.setMarker(true);
				e.setFireTicks(killDelay);
				e.setDisabledSlots(EquipmentSlot.values());;
				e.setRotation(pitch < 0 ? (yaw < 180 ? yaw + 180 : yaw - 180) : yaw, 0);
				e.setHeadPose(new EulerAngle((pitch < 0 ? -1 : 1) * Math.toRadians(pitch), 0, 0));
			}));
		});
		
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
		
		//Cancels the portal (killDelay) ticks after the spiral ends
		Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
			public void run() {
				portalTask.cancel();
				for(ArmorStand e : armourStands) {
					e.remove();
				}
			}
		}, killDelay);
	}
}