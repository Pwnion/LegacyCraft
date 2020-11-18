package com.pwnion.legacycraft.listeners;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.abilities.HotbarAbility;

public class InventoryClose implements Listener {
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		InventoryView inventoryView = e.getView();
		ItemStack cursorItem = inventoryView.getCursor();
		
		if(HotbarAbility.Type.getMaterials().contains(cursorItem.getType())) {
			int swapSlot = PlayerData.getSwapSlot(playerUUID);
			inventoryView.setItem(swapSlot, cursorItem);
			p.setItemOnCursor(null);
		}
	}
}
