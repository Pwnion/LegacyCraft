package com.pwnion.legacycraft.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class QuestManager {

	static ArrayList<Quest> quests = new ArrayList<Quest>();;
	
	public static void load() {
		//Load
		//for(String quest : file) {
		//	quests.add(new Quest(quest));
		//}
		
		quests.add(new Quest("1", "Get 32 of oak logs", new Trigger("item", Material.OAK_WOOD), 32));
		quests.add(new Quest("3", "Get a stack of diamonds", new Trigger("item", Material.DIAMOND), 64));
		
		HashMap<Location, Integer> hash = new HashMap<Location, Integer>();
		hash.put(new Location(Bukkit.getWorld("Neutral"), 0, 0, 0), 10);
		quests.add(new Quest("2", "Go to 0, 0, 0", new Trigger("location", hash), 1));
		quests.add(new Quest("4", "Kill some Zombies", new Trigger("kill", EntityType.ZOMBIE), 16));
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
