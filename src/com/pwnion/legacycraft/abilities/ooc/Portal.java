package com.pwnion.legacycraft.abilities.ooc;

import java.util.ArrayList;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.destroystokyo.paper.ParticleBuilder;
import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.Util;

//Out of Combat

public enum Portal {
	NEUTRAL(new Location(Bukkit.getWorld("Neutral"), 98.5, 60.0, 81.5, 180f, 0f), Color.fromRGB(255, 255, 255), 1, 13),
	IGNIS(new Location(Bukkit.getWorld("Ignis"), 0, 0, 0, 0, 0), Color.fromRGB(255, 0, 0), 14, 26),
	TERRA(new Location(Bukkit.getWorld("Terra"), 0, 0, 0, 0, 0), Color.fromRGB(0, 255, 0), 27, 39),
	AER(new Location(Bukkit.getWorld("Aer"), 0, 0, 0, 0, 0), Color.fromRGB(255, 255, 0), 40, 52),
	AQUA(new Location(Bukkit.getWorld("Aqua"), 0, 0, 0, 0, 0), Color.fromRGB(0, 0, 255), 53, 65),
	CUSTOM(new Location(Bukkit.getWorld("Custom"), 0, 0, 0, 0, 0), Color.fromRGB(128, 0, 128), 66, 78);
	
	private static Location midLoc(Location loc1, Location loc2) {
		return new Location(loc1.getWorld(), (loc1.getX() + loc2.getX()) / 2, (loc1.getY() + loc2.getY()) / 2, (loc1.getZ() + loc2.getZ()) / 2);
	}

	private final Location dest;
	private final Color colour;
	private final ArrayList<Integer> customModelDataRange;
	
	private Portal(Location dest, Color colour, int customModelDataMin, int customModelDataMax) {
		ArrayList<Integer> customModelDataRange = new ArrayList<Integer>(13);
		for(int i = customModelDataMin; i <= customModelDataMax; i++) {
			customModelDataRange.add(i);
		}
		
		this.customModelDataRange = customModelDataRange;
		this.dest = dest;
		this.colour = colour;
	}
	
	public final void activate(Player p) {
		float pitch = p.getLocation().getPitch();
		double fixedPitch = pitch + (pitch < 0 ? 90 : -90);
		float yaw = p.getLocation().getYaw();
		
		int stepsSpiral = 240;
		double rotationSpiral = 360 * 6;
		double radius = 1.25;
		
		double distFromPlayer = 1.1;
		Location centre = p.getEyeLocation();
		centre.add(Util.vectorCalc(centre.getPitch(), centre.getYaw(), distFromPlayer));
		
		ArrayList<Location> spiral = Util.spiral(centre, radius, rotationSpiral, stepsSpiral);
		spiral.trimToSize();
		
		final double rotPerStep = Math.toRadians(rotationSpiral / (stepsSpiral - 1));
		final Vector pointer = Util.getRelativeVec(centre, spiral.get(spiral.size() - 1));
		final Vector axis = Util.vectorCalc(pitch, yaw, 1);
		final int killDelay = Math.round(stepsSpiral / 2) + 30 * 20;
		
		final double yawMod = -0.26953 * Math.cos(Math.toRadians(Math.abs(pitch))) + 0.75 * Math.cos(Math.abs(Math.toRadians(fixedPitch)));
		final double vertMod = 0.26953 * Math.sin(Math.toRadians(Math.abs(pitch))) + 0.75 * Math.sin(Math.abs(Math.toRadians(fixedPitch)));
		
		ArrayList<Location> points = new ArrayList<Location>(16);
		ArrayList<Location> portalMidSectionPoints = new ArrayList<Location>(8);
		int counter = 0;
		for(float vertical = 0.9375f; vertical >= -0.9375f; vertical = vertical - 0.625f) {
			for(float horizontal = -0.9375f; horizontal <= 0.9375; horizontal = horizontal + 0.625f) {
				//Location point = Point.fromLocationInYawDir(centre, (vertical / 0.625) * 0.625 * (pitch < 0 ? -1 : 1) * (Math.cos(Math.toRadians(fixedPitch))), vertical);
				Location point = Util.locationCalc(centre, 
						vertical * (pitch < 0 ? -1 : 1) * Math.cos(Math.toRadians(fixedPitch)), //Distance
						vertical * Math.abs(Math.sin(Math.toRadians(fixedPitch)))); //Y value from centre
				
				point.setYaw(centre.getYaw() < 270 ? centre.getYaw() + 90 : centre.getYaw() - 270);
				
				point = Util.locationCalc(point, horizontal, 0);
				//point = Point.fromLocationInYawDir(point, horizontal, -(vertical / 0.625) * 0.625 * (1 - Math.abs(Math.sin(Math.toRadians(fixedPitch)))));
				
				points.add(point);
				
				if(counter >= 4 && counter <= 11) {
					Location portalMidSectionPoint = point.clone().add(0, 1.5, 0);
					portalMidSectionPoint.setYaw(pitch < 0 ? (yaw < 180 ? yaw + 180 : yaw - 180) : yaw);
					//portalMidSectionPoints.add(Point.fromLocationInYawDir(portalMidSectionPoint, yawMod, vertMod));
					portalMidSectionPoints.add(Util.locationCalc(portalMidSectionPoint, yawMod, vertMod));
				}
				
				counter++;
			}
		}
		
		Vector portalMod = centre.clone().toVector().subtract(midLoc(portalMidSectionPoints.get(0), portalMidSectionPoints.get(7)).toVector());
		portalMidSectionPoints.forEach((point) -> {
			point.add(portalMod);
		});
		
		ArrayList<ArmorStand> armourStands = new ArrayList<ArmorStand>(16);
		points.forEach((point) -> {
			armourStands.add(p.getWorld().spawn(point.add(portalMod), ArmorStand.class, (e) -> {
				e.setGravity(false);
				e.setVisible(false);
				e.setSilent(true);
				
				//Cannot be set on fire if Invulnerable?
				//e.setInvulnerable(true);
				
				/*
				   NoDamage Ticks:
				    	Decrease noDamageTicks by 1 per tick (until it hits 0)
						Upon receiving damage, if noDamageTicks is larger than the half of maxNoDamageTicks:
							cancel the damage if the current damage is smaller than the last damage received
							otherwise, damage the entity by the difference of the current damage and last damage
						If damage was applied, set noDamageTicks to maxNoDamageTicks
				 */
				
				//Invulnerable given damage does not exceed last damage
				e.setLastDamage(Double.MAX_VALUE);
				e.setMaximumNoDamageTicks(killDelay * 2);
				e.setNoDamageTicks(killDelay * 2);
				
				//Setting the AS on fire stops the AS from going black when inside a block (Cannot be used with Invulnerable?)
				e.setFireTicks(killDelay);
				
				//Marker stops punching and allows interaction through the portal
				//Creates Render Bug https://bugs.mojang.com/browse/MC-98146
				e.setMarker(true);
				
				e.setDisabledSlots(EquipmentSlot.values());
				e.setRotation(pitch < 0 ? (yaw < 180 ? yaw + 180 : yaw - 180) : yaw, 0);
				e.setHeadPose(new EulerAngle((pitch < 0 ? -1 : 1) * Math.toRadians(pitch), 0, 0));
			}));
		});
		
		BiConsumer<Location, Boolean> spawnParticles = (point, circleNotSpiral) -> {
			ParticleBuilder radiusParticles = new ParticleBuilder(Particle.REDSTONE);
			radiusParticles.offset(0, 0, 0);
			radiusParticles.force(true);
			radiusParticles.color(colour, circleNotSpiral ? 1f : 0.5f);
			radiusParticles.count(circleNotSpiral ? 5 : 1);
			radiusParticles.location(point);
			radiusParticles.receivers(100);
			radiusParticles.spawn();
			
			ParticleBuilder enchantParticles = new ParticleBuilder(Particle.ENCHANTMENT_TABLE);
			double offsetX = circleNotSpiral ? 0.625 * Math.abs(Math.sin(yaw)) * Math.sin(Math.abs(pitch)) : 0;
			double offsetY = circleNotSpiral ? 0.625 * Math.cos(Math.abs(pitch)) : 0;
			double offsetZ = circleNotSpiral ? 0.625 * Math.abs(Math.cos(yaw) * Math.sin(Math.abs(pitch))) : 0;
			enchantParticles.offset(offsetX, offsetY, offsetZ);
			enchantParticles.force(true);
			enchantParticles.extra(circleNotSpiral ? 10D : 0D);
			enchantParticles.count(circleNotSpiral ? 5 : 1);
			enchantParticles.location(circleNotSpiral ? centre.clone().add(0, 1.25, 0) : point);
			enchantParticles.receivers(100);
			enchantParticles.spawn();
		};
		
		final int startTick = Bukkit.getServer().getCurrentTick();
		BukkitTask portalTask = Bukkit.getServer().getScheduler().runTaskTimer(LegacyCraft.getPlugin(), new Runnable() {
				public void run() {
					int i = (Bukkit.getServer().getCurrentTick() - startTick) * 2;
					if(i < spiral.size() - 1) {
						spawnParticles.accept(spiral.get(i), false);
						spawnParticles.accept(spiral.get(i + 1), false);
						
						if(i % 12 == 0) {
							Util.playSoundWithoutConflict(centre, Sound.BLOCK_END_PORTAL_FRAME_FILL, 0.5f, 1 + (i / 240f));
						}
						
						if(i == spiral.size() - 9) {
							Util.playSoundWithoutConflict(centre, Sound.BLOCK_END_PORTAL_SPAWN, 0.5f, 0.001f);
						}
					}

					if(i >= spiral.size() - 4) {
						
						for(int j = 0; j < 2; j++) {
							pointer.rotateAroundAxis(axis, rotPerStep);
							spawnParticles.accept(centre.clone().add(pointer), true);
						}
						
						Util.playSoundWithoutConflict(centre, Sound.AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE, 0.5f, 0.1f);
					}
				}
		}, 0, 1);
		
		//Cancels the portal (killDelay) ticks after the spiral ends
		BukkitTask cleanupTask = Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
			public void run() {
				portalTask.cancel();
				for(ArmorStand e : armourStands) {
					e.remove();
				}
				
				p.stopSound(Sound.AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE, SoundCategory.PLAYERS);
				Util.stopSurroundingSound(centre, Sound.AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE);
				
				Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
					public void run() {
						//Util.playSoundWithoutConflict(centre, Sound.ITEM_TRIDENT_THUNDER, 0.3f, 0.1f);
						Util.playSoundWithoutConflict(centre, Sound.BLOCK_BEACON_DEACTIVATE, 2f, 0.8f);
					}
				}, 2);
			}
		}, killDelay);
		
		Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
			public void run() {
				//Flip CustomModelDataGrid horizontally if player is looking down
				if(pitch > 0) {
					for(int i = 0; i < 16; i++) {
						int swapTargetIndex = -1;
						if(i % 4 == 0) {
							swapTargetIndex = i + 3;
						} else if((i - 1) % 4 == 0) {
							swapTargetIndex = i + 1;
						} else {
							i += 1;
							continue;
						}
						ArmorStand swapTarget = armourStands.get(i);
						armourStands.set(i, armourStands.get(swapTargetIndex));
						armourStands.set(swapTargetIndex, swapTarget);
					}
				}
				
				int centreCounter = 0;
				for(int i = 1; i <= 16; i++) {
					ItemStack portalPiece = new ItemStack(Material.ITEM_FRAME);
					ItemMeta portalPieceMeta = portalPiece.getItemMeta();
					if(i != 6 && i != 7 && i != 10 && i != 11) {
						portalPieceMeta.setCustomModelData(customModelDataRange.get(i - centreCounter));
					} else {
						portalPieceMeta.setCustomModelData(customModelDataRange.get(0));
						centreCounter++;
					}
					portalPiece.setItemMeta(portalPieceMeta);
					armourStands.get(i - 1).setHelmet(portalPiece);
				}
				
				LegacyCraft.addTaskToBeCancelled(Bukkit.getServer().getScheduler().runTaskTimer(LegacyCraft.getPlugin(), new Runnable() {
					public void run() {
						if(dest.getWorld() != null) {
							for(int i = 0; i < portalMidSectionPoints.size(); i++) {
								for(Entity e : centre.getNearbyEntities(2, 2, 2)) {
									if(e instanceof Player) {
										Player p = (Player) e;
										
										if(p.getBoundingBox().contains(portalMidSectionPoints.get(i).toVector())) {
											Util.playSoundWithoutConflict(centre, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1f, 1f);
											
											p.teleport(dest);
													
											cleanupTask.cancel();
													
											portalTask.cancel();
											for(ArmorStand as : armourStands) {
												as.remove();
											}
											portalMidSectionPoints.clear();
											
											return;
										}
									}
								}
							}
						}
					}
				}, 0, 1), killDelay - (stepsSpiral / 2));
			}
		}, stepsSpiral / 2);
	}
}