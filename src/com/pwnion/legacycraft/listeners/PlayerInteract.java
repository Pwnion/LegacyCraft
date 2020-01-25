package com.pwnion.legacycraft.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.abilities.inventory.InventoryFromFile;

public class PlayerInteract implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(p.getInventory().getItemInMainHand().equals(new ItemStack(Material.COMPASS, 1))) {
			p.openInventory(new InventoryFromFile("main", "inventory-gui.yml").inventory);
		}
	}
}
