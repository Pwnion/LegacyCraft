package com.pwnion.legacycraft.quests;

import java.util.ArrayList;

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
	
	public static ArrayList<Quest> getActiveQuests(Player p) {
		ArrayList<Quest> output = new ArrayList<Quest>();
		for(Quest quest : quests) {
			if(quest.hasQuestActive(p)) {
				output.add(quest);
			}
		}
		return output;
	}
	
	public static ArrayList<Integer> getQuestProgress(Player p, String name) {
		return getQuest(name).getQuestProgress(p);
	}
}
