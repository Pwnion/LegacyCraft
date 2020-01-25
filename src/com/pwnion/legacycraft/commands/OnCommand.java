package com.pwnion.legacycraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.abilities.TerraVanguard1;
import com.pwnion.legacycraft.abilities.inventory.InventoryFromFile;
import com.pwnion.legacycraft.abilities.targets.Point;

public class OnCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String lbl, String[] args) {
		Player p;
		Point ep;
		if(cs instanceof Player) {
			p = (Player) cs;
		} else {
			//If a player didn't execute the /class command, return false
			return false; 
		}

		if(lbl.equalsIgnoreCase("class")) {
			if(cs.hasPermission("destinymc.class")) {
				p.openInventory(new InventoryFromFile("main", "inventory-gui.yml").inventory);
			} else {
				cs.sendMessage(ChatColor.RED + "You do not have access to that command.");
			}
		} else if(lbl.equalsIgnoreCase("pillar")) {
			p.sendMessage(new TerraVanguard1(p).activate(2));
		} else if(lbl.equalsIgnoreCase("test")) {
			Point.fromEntityInFacingDir(p, 7, 5).getBlock().setType(Material.GLOWSTONE);
		}
		return true;
	}
}
