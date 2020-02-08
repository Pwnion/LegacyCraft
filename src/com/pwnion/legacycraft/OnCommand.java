package com.pwnion.legacycraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.pwnion.legacycraft.abilities.Util;
import com.pwnion.legacycraft.abilities.areas.Selection;
import com.pwnion.legacycraft.abilities.inventory.CharacterBuildMenuInv;
import com.pwnion.legacycraft.abilities.targets.Point;

public class OnCommand implements CommandExecutor {
	private static final String deniedMsg = ChatColor.DARK_RED + "I'm sorry, but you do not have permission to perform this command.";
	private static final HashMap<UUID, Selection> playerToSelection = new HashMap<UUID, Selection>();
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String lbl, String[] args) {
		Player p;
		UUID playerUUID;
		Point ep;
		//Ensure the command sender is a player
		if(cs instanceof Player) {
			p = (Player) cs;
			playerUUID = p.getUniqueId();
		} else {
			return false; 
		}
		
		//Manage legacy craft commands
		if(p.hasPermission("legacycraft.op")) {
			if(lbl.equals("legacycraft") || cmd.getAliases().contains(lbl)) {
				if(args.length == 0) {
					p.sendMessage(ChatColor.DARK_RED + "Try being more specific...");
					return false;
				} else {
					switch(args[0]) {
					case "class":
						if(p.getGameMode().equals(GameMode.ADVENTURE)) {
							CharacterBuildMenuInv.load(p);
						} else {
							p.sendMessage(ChatColor.DARK_RED + "You must be in adventure mode to do that!");
						}
						break;
					case "pos1":
						if(!playerToSelection.keySet().contains(playerUUID)) {
							playerToSelection.put(playerUUID, new Selection(p));
						}
						p.sendMessage(playerToSelection.get(playerUUID).setPos1());
						break;
					case "pos2":
						if(!playerToSelection.keySet().contains(playerUUID)) {
							playerToSelection.put(playerUUID, new Selection(p));
						}
						p.sendMessage(playerToSelection.get(playerUUID).setPos2());
						break;
					case "export":
						p.sendMessage(playerToSelection.get(playerUUID).export(args[1]));
						break;
					default:
						return false;
					}
				}
			} else if(lbl.equals("test")) {
				//p.sendMessage(ArcticVanguardProficiency1.activate(p));
				//Portal.activate(p);
				
				Location centre = p.getEyeLocation();
				
				int delay = 1;
				int steps = 60;
				double radius = 1.25;
				double rotation = 1080;
				double distFromPlayer = 1.5;
				
				int count = 5;
				Particle particle = Particle.ENCHANTMENT_TABLE;
				
				double radiusPerStep = radius / (steps - 1);
				double rotPerStep = Math.toRadians(rotation / (steps - 1));

				Vector vec = Util.vectorCalc(centre.getYaw(), centre.getPitch(), distFromPlayer);
				centre.add(vec);
				Vector up = new Vector(0, 1, 0);
				Vector cross = vec.clone().crossProduct(up);
				if(cross.length() == 0) {
					cross = new Vector(1, 0, 0);
				}
				
				up.rotateAroundAxis(cross, Math.toRadians(90) - vec.angle(up));
				for(int i = 0; i <= steps; i++) {
					Vector pointer = up.clone();
					pointer.rotateAroundAxis(vec, rotPerStep * i);
					pointer.multiply(radiusPerStep * i);
					Location particleLoc = centre.clone().add(pointer);
					
					if(i == 0) {
						centre.getWorld().spawnParticle(particle, particleLoc, count, 0, 0, 0, 0, null, true);
					} else {
						Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
							public void run() {
								centre.getWorld().spawnParticle(particle, particleLoc, count, 0, 0, 0, 0, null, true);
							}
						}, delay * i);
					}
				}
				
				int stepsCircle = 60;
				double rotationSpiral = (steps / (steps + stepsCircle)) * rotation;
				double rotationCircle = rotation - rotationSpiral;
				ArrayList<Location> spiral = Util.spiral(centre, radius, rotationSpiral, steps);
				spiral.trimToSize();
				ArrayList<Location> circle = Util.circle(centre, Util.vectorCalc(centre, spiral.get(spiral.size() - 1)), rotationCircle, stepsCircle);
				int i = steps;
				for(Location point : circle) {
					Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
						public void run() {
							centre.getWorld().spawnParticle(particle, point, count, 0, 0, 0, 0, null, true);
						}
					}, delay * i);
					i++;
				}
			}
			return true;
		} else {
			p.sendMessage(deniedMsg);
			return false;
		}
	}
}