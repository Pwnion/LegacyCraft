package com.pwnion.legacycraft.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import com.pwnion.legacycraft.LegacyCraft;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PlayerMove implements Listener {

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		//Resets jump ability usage every time a player touches the ground
		if(p.isOnGround()) {
			if(LegacyCraft.getJumpCounter(playerUUID) != 0) { 
				LegacyCraft.setJumpCounter(playerUUID, 0); 
				if(LegacyCraft.getClass(playerUUID).equals("shaman")) {
					p.removePotionEffect(PotionEffectType.SLOW_FALLING);
				}
			}
			if(p.getGameMode().equals(GameMode.ADVENTURE) && !p.getAllowFlight() && !LegacyCraft.getClass(playerUUID).equals("")) p.setAllowFlight(true);
		}
	}
}