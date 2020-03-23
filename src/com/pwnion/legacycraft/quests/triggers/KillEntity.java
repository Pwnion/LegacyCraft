package com.pwnion.legacycraft.quests.triggers;

import java.util.ArrayList;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.quests.Quest;
import com.pwnion.legacycraft.quests.QuestManager;
import com.pwnion.legacycraft.quests.Trigger;

public class KillEntity {
	
	private final static String triggerName = "kill";
	
	public static void onPlayerKilledEntity(Player p, EntityType dead) {
		Util.br(p.getName() + " has called onPlayerKilledEntity for " + dead.toString());
		for(Quest quest : QuestManager.getActiveQuests(p)) {
			if(quest.hasTrigger(triggerName)) {
				ArrayList<Trigger> triggers = quest.getTriggers();
				for(int i = 0; i < triggers.size(); i++) { //GET CHECKED
					if(triggers.get(i).getName() == triggerName) {
						if(triggers.get(i).getKillEntity() == dead) {
							quest.addProgress(p, i);
						}
					}
				}
			}
		}
	}
}
