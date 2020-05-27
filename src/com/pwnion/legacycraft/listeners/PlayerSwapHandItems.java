package com.pwnion.legacycraft.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.Listener;

public class PlayerSwapHandItems implements Listener {
	@EventHandler
	public void onSwitch(PlayerSwapHandItemsEvent e) {
		if(e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)){
			e.setCancelled(true);
		}
	}
}
