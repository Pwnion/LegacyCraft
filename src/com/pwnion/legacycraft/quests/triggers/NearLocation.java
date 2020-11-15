package com.pwnion.legacycraft.quests.triggers;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;
import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.quests.LocationData;
import com.pwnion.legacycraft.quests.Quest;
import com.pwnion.legacycraft.quests.QuestManager;
import com.pwnion.legacycraft.quests.Trigger;
import com.pwnion.legacycraft.quests.TriggerType;

public class NearLocation {

	private NearLocation() {
	}

	// static HashSet<UUID> activePlayers = new HashSet<UUID>();

	// Called from onMove in Listeners
	public static void onPlayerMove(Player p) {

		try {
			// if(activePlayers.contains(p.getUniqueId())) {
			for (Quest quest : QuestManager.getActiveQuests(p)) {
				if (quest.hasTrigger(TriggerType.LOCATION)) {
					ArrayList<Trigger> triggers = quest.getTriggers(TriggerType.LOCATION);
					for (Trigger trigger : triggers) {
						LocationData locationData = trigger.getLocationData();
						if (locationData.isPlayerHere(p)) {
							QuestManager.addProgress(p, quest, trigger);
							break;
						}
					}
				}
			}
			// }
		} catch (Exception ex) {
			Util.print(ex);
		}
	}

	// Possible resource / speed improvement methods
	public static void playerJoin(UUID playerUUID) {
		// if(QuestManager.hasActiveTrigger(playerUUID, TriggerType.LOCATION)) {
		// activePlayers.add(playerUUID);
		// }
	}

	public static void playerQuit(UUID playerUUID) {
		// if(QuestManager.hasActiveTrigger(playerUUID, TriggerType.LOCATION)) {
		// activePlayers.remove(playerUUID);
		// }
	}
}
