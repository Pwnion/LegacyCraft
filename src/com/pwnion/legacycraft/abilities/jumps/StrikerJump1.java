package com.pwnion.legacycraft.abilities.jumps;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class StrikerJump1 extends Jump {
	//Initiates the first Striker jump for a player
	public static void activate(Player p) {
		activate(p, 0.4f, 0.8f, 2, Sound.ENTITY_BLAZE_SHOOT, 0.2f, 1.5f);
	}
}
