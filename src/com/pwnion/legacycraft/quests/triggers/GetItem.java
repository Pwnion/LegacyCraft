package com.pwnion.legacycraft.quests.triggers;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.quests.Quest;
import com.pwnion.legacycraft.quests.QuestManager;
import com.pwnion.legacycraft.quests.Trigger;
import com.pwnion.legacycraft.quests.TriggerType;

public class GetItem {

	//Called from InventoryClick, InventoryDrag, EntityPickupItem, and when a quest is given to a player (QuestManager)
	public static void updateItemQuests(Player p) {
		Bukkit.getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
			@Override
			public void run() {
				
				for(Quest quest : QuestManager.getActiveQuests(p)) {
					//If quest requires items to be checked
					if(quest.hasTrigger(TriggerType.ITEM)) {
						ArrayList<Trigger> triggers = quest.getTriggers(TriggerType.ITEM);
						for(int i = 0; i < triggers.size(); i++) {
							Trigger trigger = triggers.get(i);
							Material mat = trigger.getItem();
							if(p.getInventory().contains(mat)) {
								int count = 0;
								for(ItemStack items : p.getInventory().all(mat).values()) {
									count += items.getAmount();
								}
								QuestManager.setProgress(p, quest, i, count);
							}
						}
					}
				}
			}
		}, 1);
	}
}
