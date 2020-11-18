package com.pwnion.legacycraft.abilities.jumps;

import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.Util;

//Super class for jump classes
public abstract class Jump {
	static void activate(Player p, float vMod, float vModY, int maxJumps, Sound sound, float volume, float pitch) {
		UUID playerUUID = p.getUniqueId();
		float yaw = p.getLocation().getYaw();
		
		//Make sure the player jumps towards the direction they're facing and set velocity vectors for the jump
		p.setVelocity(Util.vectorCalc(0, yaw, vMod).setY(vModY));
		
		//Play sound when jumping
		p.getWorld().playSound(p.getLocation(), sound, SoundCategory.PLAYERS, volume, pitch);
		
		//Increment jump counter
	    PlayerData.setJumpCount(playerUUID, PlayerData.getJumpCount(playerUUID) + 1);
	    
	    //Set fall distance to 0 so ability jumps soften falls mid-air
	    p.setFallDistance(0f);
	
	    //If a player reaches their max jumps, disable flight
	    if(PlayerData.getJumpCount(playerUUID) >= maxJumps) p.setAllowFlight(false);
	}
}
