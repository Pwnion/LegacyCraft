package com.pwnion.legacycraft.abilities.jumps;

import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;

//Super class for jump classes
public abstract class Jump {
	static void activate(Player p, float vMod, float vModY, int maxJumps, Sound sound, float volume, float pitch) {
		UUID playerUUID = p.getUniqueId();
		float yaw = p.getLocation().getYaw();
		yaw = (yaw < 0) ? (yaw + 360) : (yaw == 360) ? 0 : yaw;
		
		//Make sure the player jumps towards the direction they're facing and set velocity vectors for the jump
		switch(Math.round((yaw / 90) - 0.5f)) {
			case 0:
				p.setVelocity(new Vector(-vMod * Math.sin(Math.toRadians(yaw)), vModY, vMod * Math.cos(Math.toRadians(yaw))));
				break;
			case 1:
				yaw -= 90;
				p.setVelocity(new Vector(-vMod * Math.cos(Math.toRadians(yaw)), vModY, -vMod * Math.sin(Math.toRadians(yaw))));
				break;
			case 2:
				yaw -= 180;
				p.setVelocity(new Vector(vMod * Math.sin(Math.toRadians(yaw)), vModY, -vMod * Math.cos(Math.toRadians(yaw))));
				break;
			case 3:
				yaw -= 270;
				p.setVelocity(new Vector(vMod * Math.cos(Math.toRadians(yaw)), vModY, vMod * Math.sin(Math.toRadians(yaw))));
				break;
		}
		
		//Play sound when jumping
		p.getWorld().playSound(p.getLocation(), sound, SoundCategory.PLAYERS, volume, pitch);
		
		//Increment jump counter
	    LegacyCraft.setPlayerData(playerUUID, PlayerData.JUMP_COUNTER, (Integer) LegacyCraft.getPlayerData(playerUUID, PlayerData.JUMP_COUNTER) + 1);
	    
	    //Set fall distance to 0 so ability jumps soften falls mid-air
	    p.setFallDistance(0f);
	
	    //If a player reaches their max jumps, disable flight
	    if((Integer) LegacyCraft.getPlayerData(playerUUID, PlayerData.JUMP_COUNTER) >= maxJumps) p.setAllowFlight(false);
	}
}
