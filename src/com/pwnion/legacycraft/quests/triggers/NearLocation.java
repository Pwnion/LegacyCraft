package com.pwnion.legacycraft.quests.triggers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.pwnion.legacycraft.quests.Quest;
import com.pwnion.legacycraft.quests.QuestManager;
import com.pwnion.legacycraft.quests.Trigger;


public class NearLocation implements Listener {
	
	private final static String triggerName = "location";
	//static HashSet<UUID> activePlayers = new HashSet<UUID>();
	
	//Called from onMove in Listeners
	public static void onPlayerMove(Player p) {
		//if(activePlayers.contains(p.getUniqueId())) {
			for(Quest quest : QuestManager.getActiveQuests(p)) {
				if(quest.hasTrigger(triggerName)) {
					ArrayList<Trigger> triggers = quest.getTriggers();
					for(int i = 0; i < triggers.size(); i++) { //GET CHECKED
						Trigger trigger = triggers.get(i);
						if(trigger.getName() == triggerName) {
							HashMap<Location, Integer> LocationData = trigger.getLocationData();
							Location playerLoc = p.getLocation();
							for(Location loc : LocationData.keySet()) {
								if(loc.getWorld() == playerLoc.getWorld()) {
									int requiredDistance = LocationData.get(loc);
									if(requiredDistance >= loc.distance(playerLoc)) {
										quest.addProgress(p, i);
										break;
									}
								}
							}
						}
					}
				}
			}
		//}
	}
	
	//Possible resource / speed improvement methods
	public static void playerJoin(UUID playerUUID) {
		//if(QuestManager.hasActiveTrigger(playerUUID, triggerName)) {
		//	activePlayers.add(playerUUID);
		//}
	}
	
	public static void playerQuit(UUID playerUUID) {
		//if(QuestManager.hasActiveTrigger(playerUUID, triggerName)) {
		//	activePlayers.remove(playerUUID);
		//}
	}
}
