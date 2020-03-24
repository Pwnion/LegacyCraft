package com.pwnion.legacycraft.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.quests.triggers.GetItem;

public class InventoryDrag implements Listener {

	@EventHandler
	private static void onInventoryDrag(InventoryDragEvent e) {
		if(e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			Util.br(p.getName() + " has called onInventoryDrag");
			GetItem.updateItemQuests(p);
		}
	}

}
