package com.pwnion.legacycraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.abilities.areas.Circle;
import com.pwnion.legacycraft.abilities.areas.Sphere;
import com.pwnion.legacycraft.abilities.inventory.CharacterBuildMenuInv;
import com.pwnion.legacycraft.abilities.proficiencies.TerraVanguardProficiency1;
import com.pwnion.legacycraft.abilities.targets.Point;

public class OnCommand implements CommandExecutor {
	private static String deniedMsg = ChatColor.DARK_RED + "I'm sorry, but you do not have permission to perform this command.";
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String lbl, String[] args) {
		Player p;
		Point ep;
		//Ensure the command sender is a player
		if(cs instanceof Player) {
			p = (Player) cs;
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
						CharacterBuildMenuInv.load(p);
						break;
					default:
						return false;
					}
				}
			} else if(lbl.equals("test")) {
				for(Block block : Sphere.get(p.getLocation().add(0, 5, 0), 4)) {
					block.setType(Material.STONE);
				}
			}
			return true;
		} else {
			p.sendMessage(deniedMsg);
			return false;
		}
	}
}