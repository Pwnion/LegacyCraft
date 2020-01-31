package com.pwnion.legacycraft.abilities.jumps;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class RogueJump1 extends Jump {
	//Initiates the first Rogue jump for a player
	public static void activate(Player p) {
		activate(p, 0.2f, 1f, 2, Sound.ENTITY_BLAZE_SHOOT, 0.2f, 1.5f);
	}
}
