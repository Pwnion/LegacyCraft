package com.pwnion.legacycraft.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.ConfigAccessor;

public class QuestManager {

	static ArrayList<Quest> quests = new ArrayList<Quest>();
	static HashMap<String, ArrayList<String>> questLines = new HashMap<String, ArrayList<String>>();
	
	public static void load() {
		//Load
		//for(String quest : file) {
		//	quests.add(new Quest(quest));
		//}
		
		quests.add(new Quest("Get 32 oak logs", "mine some trees", new Trigger(TriggerType.ITEM, Material.OAK_LOG, 32)));
		addLastQuestToQuestLine("Starter");
		quests.add(new Quest("Get a stack of diamonds", "you'll need an iron pick for this", new Trigger(TriggerType.ITEM, Material.DIAMOND, 64)));
		addLastQuestToQuestLine("Starter");
		
		HashMap<Location, Integer> hash = new HashMap<Location, Integer>();
		hash.put(new Location(Bukkit.getWorld("Neutral"), 0, 0, 0), 5);
		quests.add(new Quest("Go to 0, 0, 0", "remember the y-level", new Trigger(TriggerType.LOCATION, hash, 1)));
		addLastQuestToQuestLine("Starter");
		
		quests.add(new Quest("Kill some Zombies", "not a lot just 16", new Trigger(TriggerType.KILLENTITY, EntityType.ZOMBIE, 16)));
		addLastQuestToQuestLine("Starter");
		
		quests.add(new Quest("Speak to the Librarian", "brag to the librarian about your achivements", new Trigger(TriggerType.NPC, "Librarian", 1)));
		addLastQuestToQuestLine("Starter");
	}
	
	public static void save() {
		final ConfigAccessor questDataConfig = new ConfigAccessor("quest-data.yml");
		final ConfigurationSection questDataCS = questDataConfig.getRoot();
		
		for(Quest quest : quests) {
			questDataCS.set(path, value);
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
	
	public static ArrayList<Quest> getCompletedQuests(UUID playerUUID) {
		ArrayList<Quest> output = new ArrayList<Quest>();
		for(Quest quest : quests) {
			if(quest.hasQuestFinished(playerUUID)) {
				output.add(quest);
			}
		}
		return output;
	}
	
	public static ArrayList<Quest> getQuests(UUID playerUUID) {
		ArrayList<Quest> output = new ArrayList<Quest>();
		for(Quest quest : quests) {
			if(quest.hasQuestActive(playerUUID) || quest.hasQuestFinished(playerUUID)) {
				output.add(quest);
			}
		}
		return output;
	}
	
	public static ArrayList<Quest> getActiveQuests(Player p) {
		return getActiveQuests(p.getUniqueId());
	}
	
	public static ArrayList<Quest> getCompletedQuests(Player p) {
		return getCompletedQuests(p.getUniqueId());
	}
	
	public static ArrayList<Quest> getQuests(Player p) {
		return getQuests(p.getUniqueId());
	}
	
	public static ArrayList<Integer> getQuestProgress(Player p, String name) {
		return getQuest(name).getProgress(p);
	}
	
	public static ArrayList<String> getQuestLine(String name) {
		return questLines.getOrDefault(name.toLowerCase(), new ArrayList<String>());
	}
	
	public static Quest getQuestLineQuest(String questLineName, int index) {
		return getQuest(getQuestLine(questLineName).get(index));
	}
	
	public static boolean hasActiveTrigger(UUID playerUUID, TriggerType name) {
		for(Quest quest : getActiveQuests(playerUUID)) {
			for(Trigger trigger : quest.getTriggers()) {
				if(trigger.getName() == name) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static void giveQuestLine(Player p, String questLine) {
		getQuestLineQuest(questLine, 0).addPlayer(p);
	}
	
	public static void addLastQuestToQuestLine(String questLineName) {
		addQuestToQuestLine(questLineName, quests.get(quests.size() - 1).name);
	}
	
	public static void addQuestToQuestLine(String questLineName, String questName) {
		ArrayList<String> questLineArray = getQuestLine(questLineName.toLowerCase());
		getQuest(questName).addToQuestLine(questLineName, questLineArray.size());
		questLineArray.add(questName);
		questLines.put(questLineName.toLowerCase(), questLineArray);
	}
	
	public static boolean gotQuestLine(Player p, String questLineName) {
		return getQuestLineQuest(questLineName, 0).gotQuest(p);
	}
	
	public static void resetQuests(Player p, boolean fullRemoval) {
		for(Quest quest : getQuests(p)) {
			quest.resetProgress(p, fullRemoval);
		}
	}
}
