package com.pwnion.legacycraft.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.abilities.Abilities;
import com.pwnion.legacycraft.abilities.HotbarAbility;

public class PlayerItemHeld implements Listener {
	
	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent e) {
		Player p = e.getPlayer();
		int oldSlot = e.getPreviousSlot();
		int newSlot = e.getNewSlot();
		ItemStack item = p.getInventory().getItem(newSlot);
		
		if(p.getGameMode() == GameMode.ADVENTURE) {
			if(item == null || oldSlot == newSlot) return;
			
			if(!HotbarAbility.Type.getMaterials().contains(item.getType())) return;
			
			e.setCancelled(true);
			
			try {
				//Abilities.values()[customModelData - 1].activate(p); --> USE THIS WHEN ALL ABILITIES ARE IMPLEMENTED
				for(Abilities ability : Abilities.values()) {
					if(ability.getCustomModelData() == item.getItemMeta().getCustomModelData() && ability.getType().getMaterial() == item.getType()) {
						if(p.getCooldown(item.getType()) == 0) {
							p.sendMessage(ability.activate(p));
							break;
						} else {
							p.sendMessage(ChatColor.DARK_RED + "Ability on cooldown!");
						}	
					}
				}
			} catch(Exception ex) {
				//Temp try/catch until all abilities are implemented
			}
		}
	}
}
