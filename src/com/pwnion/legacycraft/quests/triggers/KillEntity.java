package com.pwnion.legacycraft.quests.triggers;

import java.util.ArrayList;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.quests.Quest;
import com.pwnion.legacycraft.quests.QuestManager;
import com.pwnion.legacycraft.quests.Trigger;
import com.pwnion.legacycraft.quests.TriggerType;

public class KillEntity {

	private KillEntity() {
	}

	// Called from EntityDeath in Listeners
	public static void onPlayerKilledEntity(Player p, EntityType dead) {
		try {
			// Util.br(p.getName() + " has called onPlayerKilledEntity for " +
			// dead.toString());
			for (Quest quest : QuestManager.getActiveQuests(p)) {
				if (quest.hasTrigger(TriggerType.KILL_ENTITY)) {
					ArrayList<Trigger> triggers = quest.getTriggers(TriggerType.KILL_ENTITY);
					for (Trigger trigger : triggers) {
						if (trigger.getKillEntity() == dead) {
							QuestManager.addProgress(p, quest, trigger);
						}
					}
				}
			}
		} catch (Exception ex) {
			Util.print(ex);
		}
	}
}
