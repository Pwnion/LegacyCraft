package com.pwnion.legacycraft.abilities.jumps;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class VanguardJump2 extends Jump {
	//Initiates the second Vanguard jump for a player
	public static void activate(Player p) {
		activate(p, 0f, 0f, 0, Sound.ENTITY_BLAZE_SHOOT, 0f, 0f);
	}
}
