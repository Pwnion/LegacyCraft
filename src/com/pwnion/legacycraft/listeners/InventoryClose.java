package com.pwnion.legacycraft.listeners;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;

public class InventoryClose implements Listener {
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		InventoryView inventoryView = e.getView();
		ItemStack cursorItem = inventoryView.getCursor();
		
		if(cursorItem.getType().equals(Material.IRON_HOE)) {
			int swapSlot = (int) LegacyCraft.getPlayerData(playerUUID, PlayerData.SWAP_SLOT);
			inventoryView.setItem(swapSlot, cursorItem);
			p.setItemOnCursor(null);
		}
	}
}
