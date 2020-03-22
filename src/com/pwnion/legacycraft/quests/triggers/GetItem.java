package com.pwnion.legacycraft.quests.triggers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.quests.Quest;
import com.pwnion.legacycraft.quests.QuestManager;
import com.pwnion.legacycraft.quests.Trigger;

public class GetItem implements Listener {
	
	@EventHandler
	private static void onInventoryInteract(InventoryInteractEvent e) {
		Player p = (Player) e.getWhoClicked();
		Util.br(p.getName() + " has called onInventoryInteract");
		for(Quest quest : QuestManager.getActiveQuests(p)) {
			if(quest.hasTrigger("item")) {
				for(Trigger trigger : quest.triggers) {
					Material mat = trigger.getItem();
					if(p.getInventory().contains(mat)) {
						int count = 0;
						for(ItemStack items : p.getInventory().all(mat).values()) {
							count += items.getAmount();
						}
						quest.setProgress(p, trigger, count);
					}
				}
			}
		}
	}
}
