package com.pwnion.legacycraft;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.abilities.areas.Selection;
import com.pwnion.legacycraft.abilities.inventory.CharacterBuildMenuInv;
import com.pwnion.legacycraft.abilities.ooc.Portal;
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
				try {
					Portal.activate(p, Portal.valueOf(args[0].toUpperCase()));
				} catch(CommandException e) {
					p.sendMessage(ChatColor.DARK_RED + "Invalid portal type!");
				}
			}
			return true;
		} else {
			p.sendMessage(deniedMsg);
			return false;
		}
	}
}