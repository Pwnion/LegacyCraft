package com.pwnion.legacycraft.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.abilities.Abilities;

public class PlayerItemHeld implements Listener {
	
	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent e) {
		Player p = e.getPlayer();
		int oldSlot = e.getPreviousSlot();
		int newSlot = e.getNewSlot();
		ItemStack item = p.getInventory().getItem(newSlot);
		
		if(item == null || oldSlot == newSlot) return;
		
		if(item.getType().equals(Material.IRON_HOE)) {
			e.setCancelled(true);
			
			try {
				//Abilities.values()[customModelData - 1].activate(p); --> USE THIS WHEN ALL ABILITIES ARE IMPLEMENTED
				for(Abilities ability : Abilities.values()) {
					if(ability.getCustomModelData() == item.getItemMeta().getCustomModelData()) {
						p.sendMessage(ability.activate(p));
						break;
					}
				}
			} catch(Exception ex) {
				//Temp try/catch until all abilities are implemented
			}
			
		}
	}
}
