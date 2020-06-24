package com.pwnion.legacycraft.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.abilities.enhancements.EnhancementManager;
import com.pwnion.legacycraft.abilities.enhancements.EnhancementType;

public class PlayerInteract implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.LEFT_CLICK_AIR && e.getAction() == Action.LEFT_CLICK_BLOCK) {
			Player p = e.getPlayer();
			ItemStack item = p.getInventory().getItemInMainHand();
			
			EnhancementManager.apply(p, item, null, EnhancementType.WeaponSwing);
		}
	}
}
