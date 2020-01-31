package com.pwnion.legacycraft.abilities.jumps;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ShamanJump1 extends Jump {
	//Initiates the first Shaman jump for a player
	public static void activate(Player p) {
		activate(p, 0.4f, 0.7f, 2, Sound.ENTITY_BLAZE_SHOOT, 0.2f, 1.8f);

		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 120, 3, false, false, false), true);
	}
}
