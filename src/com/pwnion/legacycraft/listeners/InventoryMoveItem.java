package com.pwnion.legacycraft.listeners;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import com.pwnion.legacycraft.quests.triggers.GetItem;

public class InventoryMoveItem implements Listener {

	public void onInventoryMoveItem(InventoryMoveItemEvent e) {
		for(HumanEntity entity : e.getInitiator().getViewers()) {
			if(entity instanceof Player) {
				Player p = (Player) entity;
				GetItem.updateItemQuests(p);
			}
		}
	}

}
