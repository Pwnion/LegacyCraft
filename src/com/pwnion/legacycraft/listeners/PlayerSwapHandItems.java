package com.pwnion.legacycraft.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerSwapHandItems {
	@EventHandler
	public void onSwitch(PlayerSwapHandItemsEvent e) {
		e.setCancelled(true);
	}
}
