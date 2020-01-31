package com.pwnion.legacycraft.listeners;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.pwnion.legacycraft.LegacyCraft;

public class PlayerQuit implements Listener {
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		
		//Remove the player data for the player that left the server
		LegacyCraft.removePlayerData(playerUUID);
	}
}
