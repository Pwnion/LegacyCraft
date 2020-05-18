package com.pwnion.legacycraft.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import com.pwnion.legacycraft.quests.triggers.GetItem;

public class EntityPickupItem implements Listener {
	@EventHandler
	private static void onEntityPickupItem(EntityPickupItemEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			//Util.br(p.getName() + " has called onEntityPickupItem");
			GetItem.updateItemQuests(p);
		}
	}
}
