package com.pwnion.legacycraft.listeners;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import com.pwnion.legacycraft.LegacyCraft;

public class PlayerGameModeChange implements Listener {
	
	@EventHandler
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		GameMode gm = e.getNewGameMode();
		
		boolean wasInAdventure = LegacyCraft.getPlayerAdventureMode(playerUUID);
		if(!wasInAdventure && gm.equals(GameMode.ADVENTURE)) {
			LegacyCraft.setPlayerAdventureMode(playerUUID, true);
			
			
		} else if(wasInAdventure) {
			LegacyCraft.setPlayerAdventureMode(playerUUID, false);
		}
	}
}
