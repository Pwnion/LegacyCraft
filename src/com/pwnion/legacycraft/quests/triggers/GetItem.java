package com.pwnion.legacycraft.quests.triggers;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.quests.Quest;
import com.pwnion.legacycraft.quests.QuestManager;
import com.pwnion.legacycraft.quests.Trigger;

public class GetItem implements Listener {

	private final static String triggerName = "item";
	
	public static void updateItemQuests(Player p) {
		Bukkit.getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
			@Override
			public void run() {
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
		}, 1);
	}
}
