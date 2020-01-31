package com.pwnion.legacycraft.listeners;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;

public class PlayerGameModeChange implements Listener {
	
	@EventHandler
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		GameMode gm = e.getNewGameMode();
		
		//Handles switching between adventure mode and other game modes
		boolean wasInAdventure = (boolean) LegacyCraft.getPlayerData(playerUUID, PlayerData.ADVENTURE_MODE);
		if(!wasInAdventure && gm.equals(GameMode.ADVENTURE)) {
			LegacyCraft.setPlayerData(playerUUID, PlayerData.ADVENTURE_MODE, true);
		} else if(wasInAdventure) {
			LegacyCraft.setPlayerData(playerUUID, PlayerData.ADVENTURE_MODE, false);
		}
	}
}
