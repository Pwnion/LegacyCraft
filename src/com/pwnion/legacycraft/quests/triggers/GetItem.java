package com.pwnion.legacycraft.quests.triggers;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.quests.Quest;
import com.pwnion.legacycraft.quests.QuestManager;
import com.pwnion.legacycraft.quests.Trigger;

public class GetItem implements Listener {

	private final static String triggerName = "item";
	
	public static void updateItemQuests(Player p) {
		for(Quest quest : QuestManager.getActiveQuests(p)) {
			if(quest.hasTrigger(triggerName)) {
				ArrayList<Trigger> triggers = quest.getTriggers();
				for(int i = 0; i < triggers.size(); i++) { //GET CHECKED
					Trigger trigger = triggers.get(i);
					if(trigger.getName() == triggerName) {
						Material mat = trigger.getItem();
						if(p.getInventory().contains(mat)) {
							int count = 0;
							for(ItemStack items : p.getInventory().all(mat).values()) {
								count += items.getAmount();
							}
							quest.setProgress(p, i, count);
						}
					}
				}
			}
		}
	}

}
