package com.pwnion.legacycraft;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.abilities.areas.Selection;
import com.pwnion.legacycraft.abilities.areas.Sphere;
import com.pwnion.legacycraft.abilities.inventory.CharacterBuildMenuInv;
import com.pwnion.legacycraft.abilities.ooc.Portal;
import com.pwnion.legacycraft.abilities.targets.Point;
import com.pwnion.legacycraft.npcs.HomeWorkData;

public class OnCommand implements CommandExecutor {
	private static final String deniedMsg = ChatColor.DARK_RED + "I'm sorry, but you do not have permission to perform this command.";
	private static final HashMap<UUID, Selection> playerToSelection = new HashMap<UUID, Selection>();
	public static final HashMap<UUID, HomeWorkData> playerToNPCdata = new HashMap<UUID, HomeWorkData>();
	
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
					switch(args[0].toLowerCase()) {
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
					case "portal":
						try {
							Portal.valueOf(args[1].toUpperCase()).activate(p);
						} catch(Exception e) {
							p.sendMessage(ChatColor.DARK_RED + "Invalid portal type!");
						}
						break;
					case "home":
						if(!playerToNPCdata.keySet().contains(playerUUID)) {
							playerToNPCdata.put(playerUUID, new HomeWorkData(p));
						}
						p.sendMessage(playerToNPCdata.get(playerUUID).setHome());
						break;
					case "work":
						if(!playerToNPCdata.keySet().contains(playerUUID)) {
							playerToNPCdata.put(playerUUID, new HomeWorkData(p));
						}
						p.sendMessage(playerToNPCdata.get(playerUUID).setWork());
						break;
					default:
						return false;
					}
				}
			} else if(lbl.equals("test")) {
				Sphere.spawn(p.getTargetBlock(120).getLocation(), Integer.parseInt(args[0]), false);
			}
			return true;
		} else {
			p.sendMessage(deniedMsg);
			return false;
		}
	}
}