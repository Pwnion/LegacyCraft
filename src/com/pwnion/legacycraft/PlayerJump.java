package com.pwnion.legacycraft;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.pwnion.legacycraft.abilities.targets.Point;

public class PlayerJump implements Listener {
	
	@EventHandler
	public void onFlightAttempt(PlayerToggleFlightEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();

		//Handle ability jumps
		if(p.getGameMode() == GameMode.SURVIVAL) {
			int maxJumps = 0;
			e.setCancelled(true);
			if(e.isFlying()) {
				float vMod = 0f;
				float vModY = 0f;
				float yaw = p.getLocation().getYaw();
				
				yaw = (yaw < 0) ? (yaw + 360) : (yaw == 360) ? 0 : yaw;
				
				//Setup velocity modifiers, max jumps and potion effects for ability jumps depending on the class
				switch(LegacyCraft.getClass(playerUUID)) {
					case "striker":
						switch(LegacyCraft.getJumpSlot(playerUUID)) {
						case 31:
							vMod = 0.4f;
							vModY = 0.8f;
							maxJumps = 2;
							break;
						case 40:
							break;
						}
						break;
					case "vanguard":
						switch(LegacyCraft.getJumpSlot(playerUUID)) {
						case 31:
							vMod = 1.2f;
							vModY = 0.55f;
							maxJumps = 1;
							break;
						case 40:
							break;
						}
						break;
					case "rogue":
						switch(LegacyCraft.getJumpSlot(playerUUID)) {
						case 31:
							vMod = 0.2f;
							vModY = 1f;
							maxJumps = 2;
							break;
						case 40:
							Location targetLoc = Point.fromEntityInFacingDir(p, 7, 5);
							
							ArrayList<Vector> dirs = new ArrayList<Vector>() {
								private static final long serialVersionUID = 1L;
								{
									add(targetLoc.getX() > p.getLocation().getX() ? new Vector(1, 0, 0) : new Vector(-1, 0, 0));
									add(targetLoc.getZ() > p.getLocation().getZ() ? new Vector(0, 0, 1) : new Vector(0, 0, -1));
									add(new Vector(0, 1, 0));
								}
							};
							
							if(p.getLocation().getBlock().getType().isSolid() && p.getTargetBlock(null, 100).getType().isSolid()) {
								p.sendMessage(ChatColor.RED + "Not enough space to blink!");
							} else {
								ArrayList<Location> dests = new ArrayList<Location>(3);
								for(int i = 0; i < 3; i++) {
									Location newLoc = p.getLocation();
									Location testLoc;
									Location oldLoc;
									
									int jLoopToSkip = -1;
									int skipCounter = 0;
									if(newLoc.getBlockX() == targetLoc.getBlockX()) {
										jLoopToSkip = i;
									} else if(newLoc.getBlockZ() == targetLoc.getBlockZ()) {
										jLoopToSkip = (i + 1 > 2 ? 0 : i + 1);
									}
									
									while(skipCounter != 3) {
										for(int j = 0; j < 3; j++) {
											while(true) {
												testLoc = newLoc.clone().add(dirs.get(j));
												
												if((j == jLoopToSkip) ||
														
												   (j == i && testLoc.getBlockX() == targetLoc.getBlockX()) || 
												   (j == (i + 1 > 2 ? 0 : i + 1) && testLoc.getBlockZ() == targetLoc.getBlockZ()) ||
												   (j == (2 - i * 2 == -2 ? 1 : 2 - i * 2) && testLoc.getBlockY() == targetLoc.getBlockY()) ||
												   
												   (testLoc.clone().add(new Vector(0, 1, 0)).getBlock().getType().isSolid()) ||
												   (testLoc.getBlock().getType().isSolid()))
												{
													skipCounter++;
													break;
												} else {
													oldLoc = newLoc;
													newLoc = testLoc;
													skipCounter = 0;
												}
											}
											if(skipCounter == 3) break;
										}
									}
									Vector dir0 = dirs.get(0);
									Vector dir1 = dirs.get(1);
									dirs.set(0, dirs.get(2));
									dirs.set(1, dir0);
									dirs.set(2, dir1);
									
									dests.add(newLoc);
								}
								
								Supplier<Location> closestDestToTarget = () -> {
									Location loc = dests.get(0).distance(targetLoc) < dests.get(1).distance(targetLoc) ? dests.get(0) : dests.get(1);
									loc = dests.get(2).distance(targetLoc) < loc.distance(targetLoc) ? dests.get(2) : loc;
									
									return loc;
								};
								
								final Location finalLoc = closestDestToTarget.get();
								if(p.getLocation().getBlock().equals(finalLoc.getBlock())) {
									p.sendMessage(ChatColor.RED + "Not enough space to blink!");
								} else {
									ArrayList<Block> surroundingFinal = new ArrayList<Block>();
									new ArrayList<Vector>() {
										private static final long serialVersionUID = 1L;
										{
											add(new Vector(0.5, 0, 0.5));
											add(new Vector(0.5, 0, -0.5));
											add(new Vector(-0.5, 0, 0.5));
											add(new Vector(-0.5, 0, -0.5));
										}
									}.forEach((v) -> {
										surroundingFinal.add(finalLoc.clone().add(v).getBlock());
									});

									Location adjustedFinalLoc = finalLoc.clone();
									for(Block block : surroundingFinal) {
										if(block.getType().isSolid() || block.getRelative(BlockFace.UP, 1).getType().isSolid()) {
											adjustedFinalLoc = finalLoc.getBlock().getLocation().add(0.5, 0, 0.5);
											break;
										}
										if(block.getRelative(BlockFace.UP, 1).getType().isSolid()) {
											adjustedFinalLoc.setY(block.getRelative(BlockFace.DOWN).getLocation().getY());
											break;
										}
									}
									
									adjustedFinalLoc.setYaw(p.getLocation().getYaw());
									adjustedFinalLoc.setPitch(p.getLocation().getPitch());
									
									p.teleport(adjustedFinalLoc);
								}
							}
							
							vMod = 0.4f;
							vModY = 0.4f;
							maxJumps = 2;
							break;
						}
						break;
					case "shaman":
						switch(LegacyCraft.getJumpSlot(playerUUID)) {
						case 31:
							vMod = 0.4f;
							vModY = 0.7f;
							maxJumps = 2;
							
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 120, 3, false, false, false), true);
							break;
						case 40:
							break;
						}
						break;
				}
				
				//Make sure the player jumps towards the direction they're facing and set velocity vectors for the jump
				switch(Math.round((yaw / 90) - 0.5f)) {
					case 0:
						p.setVelocity(new Vector(-vMod * Math.sin(Math.toRadians(yaw)), vModY, vMod * Math.cos(Math.toRadians(yaw))));
						break;
					case 1:
						yaw -= 90;
						p.setVelocity(new Vector(-vMod * Math.cos(Math.toRadians(yaw)), vModY, -vMod * Math.sin(Math.toRadians(yaw))));
						break;
					case 2:
						yaw -= 180;
						p.setVelocity(new Vector(vMod * Math.sin(Math.toRadians(yaw)), vModY, -vMod * Math.cos(Math.toRadians(yaw))));
						break;
					case 3:
						yaw -= 270;
						p.setVelocity(new Vector(vMod * Math.cos(Math.toRadians(yaw)), vModY, vMod * Math.sin(Math.toRadians(yaw))));
						break;
				}
				
				//Increment jump counter
			    LegacyCraft.setJumpCounter(playerUUID, (LegacyCraft.getJumpCounter(playerUUID) + 1));
			    //Set fall distance to 0 so ability jumps soften falls mid-air
			    p.setFallDistance(0f);
			}
			
			//If a player reaches their max jumps, disable flight
			if(LegacyCraft.getJumpCounter(playerUUID) >= maxJumps) p.setAllowFlight(false);
		}
	}	
}
