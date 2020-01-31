package com.pwnion.legacycraft.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.abilities.SkillTree;
import com.pwnion.legacycraft.abilities.SkillTree.PlayerClass;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerMove implements Listener {
	
	@SuppressWarnings("unused")
	private static final int distanceToGround(Player p) {
		Location loc = p.getLocation();
		
		int counter = 0;
		while(true) {
			loc.subtract(0, counter, 0);
			if(loc.getBlock().getType().isSolid() || counter > 255) {
				return counter;
			}
			counter++;
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		
		SkillTree skillTree = (SkillTree) LegacyCraft.getPlayerData(playerUUID, PlayerData.SKILL_TREE);
		
		//Resets jump ability usage every time a player touches the ground
		if(p.isOnGround()) {
			if((int) LegacyCraft.getPlayerData(playerUUID, PlayerData.JUMP_COUNTER) != 0) { 
				//LegacyCraft.setJumpCounter(playerUUID, 0);
				LegacyCraft.setPlayerData(playerUUID, PlayerData.JUMP_COUNTER, 0);
				if(skillTree.getPlayerClass().equals(PlayerClass.SHAMAN)) {
					p.removePotionEffect(PotionEffectType.SLOW_FALLING);
				}
			}
			if(p.getGameMode().equals(GameMode.ADVENTURE) && !p.getAllowFlight() && !skillTree.getPlayerClass().equals(PlayerClass.NONE)) p.setAllowFlight(true);
			
			/*
			int distanceToGround = distanceToGround(p);
			if(distanceToGround > 7 && LegacyCraft.getPlayerUnderNoFallDamageLimit(playerUUID)) {
				LegacyCraft.setPlayerUnderNoFallDamageLimit(playerUUID, false);
			} else if(distanceToGround >= 3 && distanceToGround <= 7 && p.getFallDistance() <= 10 - distanceToGround && !LegacyCraft.getPlayerUnderNoFallDamageLimit(playerUUID)) {
				LegacyCraft.setPlayerUnderNoFallDamageLimit(playerUUID, true);
			} else if(distanceToGround < 6 && LegacyCraft.getPlayerUnderNoFallDamageLimit(playerUUID)) {
				p.setFallDistance(0);
				LegacyCraft.setPlayerUnderNoFallDamageLimit(playerUUID, false);
			}
			*/
		}
	}
}