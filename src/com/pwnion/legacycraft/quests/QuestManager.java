package com.pwnion.legacycraft.quests;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

public class QuestManager {

	static ArrayList<Quest> quests = new ArrayList<Quest>();;
	
	public static void load() {
		//Load
		//for(String quest : file) {
		//	quests.add(new Quest(quest));
		//}
		quests.add(new Quest("test"));
	}
	
	public static void save() {
		for(Quest quest : quests) {
			quest.save();
		}
	}
	
	public static Quest getQuest(String name) {
		for(Quest quest : quests) {
			if(quest.name == name) {
				return quest;
			}
		}
		return null;
	}
	
	public static ArrayList<Quest> getActiveQuests(UUID playerUUID) {
		ArrayList<Quest> output = new ArrayList<Quest>();
		for(Quest quest : quests) {
			if(quest.hasQuestActive(playerUUID)) {
				output.add(quest);
			}
		}
		return output;
	}
	
	public static ArrayList<Quest> getActiveQuests(Player p) {
		return getActiveQuests(p.getUniqueId());
	}
	
	public static ArrayList<Integer> getQuestProgress(Player p, String name) {
		return getQuest(name).getQuestProgress(p);
	}
	
	public static boolean hasActiveTrigger(UUID playerUUID, String name) {
		for(Quest quest : getActiveQuests(playerUUID)) {
			for(Trigger trigger : quest.getTriggers()) {
				if(trigger.getName() == name) {
					return true;
				}
			}
		}
		return false;
	}
}
