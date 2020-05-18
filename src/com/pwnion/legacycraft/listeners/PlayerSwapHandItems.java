package com.pwnion.legacycraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.Listener;

public class PlayerSwapHandItems implements Listener {
	@EventHandler
	public void onSwitch(PlayerSwapHandItemsEvent e) {
		e.setCancelled(true);
	}
}
