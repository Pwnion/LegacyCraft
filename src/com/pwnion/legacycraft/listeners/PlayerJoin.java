package com.pwnion.legacycraft.listeners;

import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.items.ItemManager;

public class PlayerJoin implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		
		//Initialise variables and populate with default values to help track the player
		PlayerData.generate(p);
		
		//Activates all item abilities in inventory
		for(ItemStack item : p.getInventory().getContents()) {
			ItemManager.activate(item);
		}
		
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, (float) 1.5, 1);
	}
}