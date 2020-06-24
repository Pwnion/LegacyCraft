package com.pwnion.legacycraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.abilities.enhancements.EnhancementManager;

public class PlayerItemDamage implements Listener {

	@EventHandler
	public void onPlayerItemDamageEvent(PlayerItemDamageEvent e) {
		ItemStack oldItem = e.getItem().clone();
		Util.br(EnhancementManager.hasEnhancements(oldItem));
		
		Bukkit.getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Util.br(EnhancementManager.hasEnhancements(e.getItem()));
				Util.br(EnhancementManager.hasEnhancements(oldItem));
				EnhancementManager.transfer(oldItem, e.getItem());
			}
		}, 1);
	}
}
