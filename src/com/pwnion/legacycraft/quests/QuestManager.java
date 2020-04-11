package com.pwnion.legacycraft.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.quests.triggers.FinishQuest;
import com.pwnion.legacycraft.quests.triggers.GetItem;

public class QuestManager {

	private static ArrayList<Quest> quests = new ArrayList<Quest>();
	//static HashMap<String, ArrayList<String>> questLines = new HashMap<String, ArrayList<String>>();
	
	private static HashMap<UUID, HashMap<Quest, ArrayList<Integer>>> questProgress = new HashMap<UUID, HashMap<Quest, ArrayList<Integer>>>();
	private static HashMap<UUID, ArrayList<Quest>> finishedQuests = new HashMap<UUID, ArrayList<Quest>>();
	
	public static void load() {
		
		//Load
		//for(String quest : file) {
		//	quests.add(new Quest(quest));
		//}
		
		quests.add(new Quest("Get 32 oak logs", "mine some trees", new Trigger(TriggerType.ITEM, Material.OAK_LOG, 32)));
		//addLastQuestToQuestLine("Starter");
		quests.add(new Quest("Get a stack of diamonds", "you'll need an iron pick for this", new Trigger(TriggerType.ITEM, Material.DIAMOND, 64)));
		//addLastQuestToQuestLine("Starter");
		
		HashMap<Location, Integer> hash = new HashMap<Location, Integer>();
		hash.put(new Location(Bukkit.getWorld("Neutral"), 0, 0, 0), 5);
		quests.add(new Quest("Go to 0, 0, 0", "remember the y-level", new Trigger(TriggerType.LOCATION, hash, 1)));
		//addLastQuestToQuestLine("Starter");
		
		quests.add(new Quest("Kill some Zombies", "not a lot just 16", new Trigger(TriggerType.KILLENTITY, EntityType.ZOMBIE, 16)));
		//addLastQuestToQuestLine("Starter");
		
		quests.add(new Quest("Speak to the Librarian", "brag to the librarian about your achivements", new Trigger(TriggerType.NPC, "Librarian", 1)));
		//addLastQuestToQuestLine("Starter");
	}
	
	public static void save() {
		
	}
	
	public static void giveQuest(Player p, Quest quest) {
		ArrayList<Integer> progress = new ArrayList<Integer>(quest.triggers.size());
		for(int i = 0; i < quest.triggers.size(); i++) { //GET CHECKED
			progress.add(0);
		}
		questProgress.get(p.getUniqueId()).put(quest, progress);
		p.sendMessage(ChatColor.GRAY + "You have recieved the '" + quest.name + "' quest");
		GetItem.updateItemQuests(p);
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
		for(Quest quest : questProgress.get(playerUUID).keySet()) {
			output.add(quest);
		}
		return output;
	}
	
	public static ArrayList<Quest> getCompletedQuests(UUID playerUUID) {
		return finishedQuests.get(playerUUID);
	}
	
	public static ArrayList<Quest> getQuests(UUID playerUUID) {
		ArrayList<Quest> output = new ArrayList<Quest>();
		for(Quest quest : quests) {
			if(hasQuestActive(playerUUID, quest) || hasQuestFinished(playerUUID, quest)) {
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
	
	public static void addProgress(Player p, Quest quest, int index, int amount) {
		setProgress(p, quest, index, getProgress(p, quest, index) + amount);
	}
	
	public static void addProgress(Player p, Quest quest, int index) {
		addProgress(p, quest, index, 1);
	}
	
	public static void addProgress(Player p, Quest quest, Trigger trigger, int amount) {
		addProgress(p, quest, quest.getIndex(trigger), amount);
	}
	
	public static void addProgress(Player p, Quest quest, Trigger trigger) {
		addProgress(p, quest, trigger, 1);
	}
	
	public static void setProgress(Player p, Quest quest, int index, int value) {
		ArrayList<Integer> progress = getProgress(p, quest);
		progress.set(index, value);
		questProgress.get(p.getUniqueId()).put(quest, progress);
		if(getPercentOverall(p, quest) >= 100) {
			
			//Give Quest Rewards?
			
			//Remove player as active quest Holder and move to finished list
			finishedQuests.get(p.getUniqueId()).add(quest);
			questProgress.get(p.getUniqueId()).remove(quest);
			
			FinishQuest.onFinishQuest(p, quest);
			return;
		}
	}
	
	public static void setProgress(Player p, Quest quest, Trigger trigger, int value) {
		setProgress(p, quest, quest.getIndex(trigger), value);
	} 
	
	public static void forceComplete(Player p, Quest quest) {
		for(Trigger trigger : quest.triggers) {
			setProgress(p, quest, trigger, trigger.finishCondition);
		}
	}
	
	public static int getProgress(Player p, Quest quest, int index) {
		return getProgress(p, quest).get(index);
	}
	
	public static ArrayList<Integer> getProgress(Player p, Quest quest) {
		if(hasQuestActive(p, quest)) {
			return questProgress.get(p.getUniqueId()).get(quest);
		} else if(hasQuestFinished(p, quest)) {
			ArrayList<Integer> finishValues = new ArrayList<Integer>();
			for(Trigger trigger : quest.triggers) {
				finishValues.add(trigger.getFinishCondition());
			}
			return finishValues;
		}
		ArrayList<Integer> output = new ArrayList<Integer>();
		for(int i = 0; i < quest.triggers.size(); i++) { //GET CHECKED
			output.add(0);
		}
		return output;
	}
	
	public static double getPercent(Player p, Quest quest, int index) {
		return ((double) getProgress(p, quest, index) / (double) quest.getCondition(index)) * 100;
	}
	
	public static double getPercentOverall(Player p, Quest quest) { 
		ArrayList<Integer> progress = getProgress(p, quest);
		double progressTotal = 0;
		double finalTotal = 0;
		for(int i = 0; i < quest.triggers.size(); i++) { //GET CHECKED
			progressTotal += progress.get(i);
			finalTotal += quest.getCondition(i);
		}
		return (progressTotal / finalTotal) * 100;
	}
	
	public static boolean hasQuestActive(Player p, Quest quest) {
		return hasQuestActive(p.getUniqueId(), quest);
	}
	
	public static boolean hasQuestActive(UUID playerUUID, Quest quest) {
		return questProgress.get(playerUUID).containsKey(quest);
	}
	
	public static boolean hasQuestFinished(Player p, Quest quest) {
		return hasQuestFinished(p.getUniqueId(), quest);
	}
	
	public static boolean hasQuestFinished(UUID playerUUID, Quest quest) {
		return finishedQuests.get(playerUUID).contains(quest);
	}
	
	public static boolean gotQuest(Player p, Quest quest) {
		return hasQuestActive(p, quest) || hasQuestFinished(p, quest);
	}
	
	public static void resetQuests(Player p, boolean fullRemoval) {
		if(fullRemoval) {
			questProgress.put(p.getUniqueId(), new HashMap<Quest, ArrayList<Integer>>());
			finishedQuests.remove(p.getUniqueId());
		} else {
			for(ArrayList<Integer> progress : questProgress.get(p.getUniqueId()).values()) {
				progress.clear();
			}
		}
	}
}
