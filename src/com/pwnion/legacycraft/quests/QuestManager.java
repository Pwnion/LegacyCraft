package com.pwnion.legacycraft.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.ConfigAccessor;
import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.quests.triggers.FinishQuest;
import com.pwnion.legacycraft.quests.triggers.GetItem;

public class QuestManager {

	private static ArrayList<Quest> quests = new ArrayList<Quest>();
	
	public static void loadQuests() {
		final ConfigAccessor questDataConfig = new ConfigAccessor("quest-data.yml");
		final ConfigurationSection questDataCS = questDataConfig.getRoot();

		questDataCS.getKeys(false).forEach((name) -> {
			String desc = questDataCS.getString(name + ".description");

			ArrayList<Trigger> triggers = new ArrayList<Trigger>();
			ArrayList<TriggerType> triggerTypes = new ArrayList<TriggerType>();
			ArrayList<String> triggerData = new ArrayList<String>();
			ArrayList<Integer> triggerFinishConditions = new ArrayList<Integer>();
			String nodePrefix = name + ".triggers.";

			questDataCS.getList(nodePrefix + "types").forEach((trigger) -> {
				triggerTypes.add(TriggerType.valueOf((String) trigger));
			});
			
			questDataCS.getList(nodePrefix + "data").forEach((data) -> {
				triggerData.add((String) data);
			});

			questDataCS.getList(nodePrefix + "finish-conditions").forEach((finishCondition) -> {
				triggerFinishConditions.add((int) finishCondition);
			});

			for(int i = 0; i < triggerTypes.size(); i++) {
				triggers.add(new Trigger(triggerTypes.get(i), triggerData.get(i), triggerFinishConditions.get(i)));
			}
			
			String nextQuest = questDataCS.getString(name + ".next-quest");

			quests.add(new Quest(name, desc, triggers, nextQuest));
		});
	}
	
	public static HashMap<Quest, ArrayList<Integer>> getUnfinishedPlayerData(UUID playerUUID) {
		return (HashMap<Quest, ArrayList<Integer>>) LegacyCraft.getPlayerData(playerUUID, PlayerData.UNFINISHED_QUESTS);
	}
	
	public static ArrayList<Quest> getFinishedPlayerData(UUID playerUUID) {
		return (ArrayList<Quest>) LegacyCraft.getPlayerData(playerUUID, PlayerData.FINISHED_QUESTS);
	}

	public static void savePlayerData(Player p) {
		final ConfigAccessor playerDataConfig = new ConfigAccessor("player-data.yml");
		final ConfigurationSection playerDataCS = playerDataConfig.getRoot();
		
		String nodePrefix = "players." + p.getUniqueId().toString() + ".quests.";

		getActiveQuests(p).forEach((quest) -> {
			playerDataCS.set(nodePrefix + "unfinished." + quest.getName(), getProgress(p, quest));
		});
		
		playerDataCS.set(nodePrefix + "finished", getCompletedQuests(p));
		
		playerDataConfig.saveCustomConfig();
	}
	
	public static HashMap<Quest, ArrayList<Integer>> loadUnfinishedPlayerData(UUID playerUUID) {
		HashMap<Quest, ArrayList<Integer>> questProgressFromFile = new HashMap<Quest, ArrayList<Integer>>();
		
		final ConfigAccessor playerDataConfig = new ConfigAccessor("player-data.yml");
		final ConfigurationSection playerDataCS = playerDataConfig.getRoot();
		String nodePrefix = "players." + playerUUID.toString() + ".quests.";
		
		playerDataCS.getConfigurationSection(nodePrefix + "unfinished").getKeys(false).forEach((quest) -> {
			ArrayList<Integer> progress = new ArrayList<Integer>();
			playerDataCS.getList(nodePrefix + "unfinished." + quest).forEach((num) -> {
				progress.add((int) num);
			});
			
			questProgressFromFile.put(getQuest(quest), progress);
		});
		
		return questProgressFromFile;
	}
	
	public static ArrayList<Quest> loadFinishedPlayerData(UUID playerUUID) {
		ArrayList<Quest> finishedQuestsFromFile = new ArrayList<Quest>();
		
		final ConfigAccessor playerDataConfig = new ConfigAccessor("player-data.yml");
		final ConfigurationSection playerDataCS = playerDataConfig.getRoot();
		String nodePrefix = "players." + playerUUID.toString() + ".quests.";
		
		playerDataCS.getList(nodePrefix + "finished").forEach((quest) -> {
			finishedQuestsFromFile.add(getQuest((String) quest));
		});
		
		return finishedQuestsFromFile;
	}
	
	public static void giveQuest(Player p, Quest quest) {
        ArrayList<Integer> progress = new ArrayList<Integer>(quest.triggers.size());
        for(int i = 0; i < quest.triggers.size(); i++) { //GET CHECKED
            progress.add(0);
        }
        getUnfinishedPlayerData(p.getUniqueId()).put(quest, progress);
        p.sendMessage(ChatColor.GRAY + "You have recieved the '" + quest.getName() + "' quest");
        GetItem.updateItemQuests(p);
    }

	public static Quest getQuest(String name) {
		for(Quest quest : quests) {
			if(quest.getName() == name) {
				return quest;
			}
		}
		return null;
	}
	
	public static ArrayList<Quest> getActiveQuests(UUID playerUUID) {
		ArrayList<Quest> output = new ArrayList<Quest>();
		for(Quest quest : getUnfinishedPlayerData(playerUUID).keySet()) {
			output.add(quest);
		}
		return output;
	}

	public static ArrayList<Quest> getCompletedQuests(UUID playerUUID) {
		return getFinishedPlayerData(playerUUID);
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
		getUnfinishedPlayerData(p.getUniqueId()).put(quest, progress);
		if(getPercentOverall(p, quest) >= 100) {

			//Give Quest Rewards?

			//Remove player as active quest Holder and move to finished list
			getFinishedPlayerData(p.getUniqueId()).add(quest);
			getUnfinishedPlayerData(p.getUniqueId()).remove(quest);

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
			return getUnfinishedPlayerData(p.getUniqueId()).get(quest);
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
		return getUnfinishedPlayerData(playerUUID).containsKey(quest);
	}

	public static boolean hasQuestFinished(Player p, Quest quest) {
		return hasQuestFinished(p.getUniqueId(), quest);
	}

	public static boolean hasQuestFinished(UUID playerUUID, Quest quest) {
		return getFinishedPlayerData(playerUUID).contains(quest);
	}

	public static boolean gotQuest(Player p, Quest quest) {
		return hasQuestActive(p, quest) || hasQuestFinished(p, quest);
	}

	/*
	public static void resetQuests(Player p, boolean fullRemoval) {
		if(fullRemoval) {
			questProgress.put(p.getUniqueId(), new HashMap<Quest, ArrayList<Integer>>());
			finishedQuests.remove(p.getUniqueId());
		} else {
			for(ArrayList<Integer> progress : questProgress.get(p.getUniqueId()).values()) {
				progress.clear();
			}
		}
	}*/
}
