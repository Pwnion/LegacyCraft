package com.pwnion.legacycraft.abilities.jumps;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class VanguardJump1 extends Jump {
	//Initiates the first Vanguard jump for a player
	public static void activate(Player p) {
		activate(p, 1.2f, 0.55f, 1, Sound.ENTITY_BLAZE_SHOOT, 0.2f, 1.2f);
	}
}
