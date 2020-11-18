package com.pwnion.legacycraft.abilities.jumps;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.abilities.Pathfinding;

public class RogueJump2 extends Jump {
	//Initiates the second Rogue jump for a player
	public static void activate(Player p) {
		//long start = System.nanoTime();
		//Location targetLoc = Point.fromLocationInYawDir(p, 6, 4);
		Location targetLoc = p.getLocation().add(Util.vectorCalc(0, p.getLocation().getYaw(), 6).setY(4));
		//Util.br((System.nanoTime() - start) / 1000);
		Location finalLoc = Pathfinding.inFacingDir(p, targetLoc);
		
		
		if(finalLoc.getBlock().equals(p.getLocation().getBlock()) || (p.getLocation().getBlock().getType().isSolid() && p.getTargetBlock(null, 100).getType().isSolid())) {
			p.sendMessage(ChatColor.RED + "Not enough space to blink!");
		} else {
			finalLoc.setPitch(p.getLocation().getPitch());
			finalLoc.setYaw(p.getLocation().getYaw());
			p.teleport(finalLoc);
			
			activate(p, 0.5f, 0.4f, 2, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 0.1f, 1f);
		}
	}
}
