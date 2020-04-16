package com.pwnion.legacycraft.levels;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Levelup {

	public static void onPlayerLevelup(Player p) {
		int newLevel = Levels.getLevel(p.getUniqueId());
		int oldLevel = newLevel - 1;
		
		p.sendMessage(ChatColor.GOLD + "Levelup!!! " + oldLevel + " -> " + newLevel);
	}
}
