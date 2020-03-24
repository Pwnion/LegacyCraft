package com.pwnion.legacycraft.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.quests.triggers.FinishQuest;
import com.pwnion.legacycraft.quests.triggers.GetItem;

public class Quest {
	
	public Quest(String name, String desc, Trigger trigger) {
		this.name = name;
		this.desc = desc;
		triggers.add(trigger);
	}
	
	public String name;
	public String desc;
	
	public String questLine = null;
	public int questLineIndex = 0;
	
	ArrayList<Trigger> triggers = new ArrayList<Trigger>();
	
	HashMap<UUID, ArrayList<Integer>> questHolders = new HashMap<UUID, ArrayList<Integer>>();
	HashSet<UUID> finishedQuest = new HashSet<UUID>();
	
	public void save() {
		
	}
	
	public void addPlayer(Player p) {
		ArrayList<Integer> progress = new ArrayList<Integer>(triggers.size());
		for(int i = 0; i < triggers.size(); i++) { //GET CHECKED
			progress.add(0);
		}
		questHolders.put(p.getUniqueId(), progress);
		p.sendMessage(ChatColor.GRAY + "You have recieved the '" + name + "' quest");
		GetItem.updateItemQuests(p);
	}
	
	public void addToQuestLine(String questLine, int questLineIndex) {
		this.questLine = questLine;
		this.questLineIndex = questLineIndex;
	}
	
	public void addProgress(Player p, int index, int amount) {
		setProgress(p, index, getProgress(p, index) + amount);
	}
	
	public void addProgress(Player p, int index) {
		addProgress(p, index, 1);
	}
	
	public void addProgress(Player p, Trigger trigger, int amount) {
		addProgress(p, getIndex(trigger), amount);
	}
	
	public void addProgress(Player p, Trigger trigger) {
		addProgress(p, trigger, 1);
	}
	
	public void setProgress(Player p, int index, int value) {
		ArrayList<Integer> progress = getProgress(p);
		progress.set(index, value);
		if(getPercentOverall(p) >= 100) {
			
			//Give Quest Rewards?
			
			//Remove player as active quest Holder and move to finished list
			finishedQuest.add(p.getUniqueId());
			questHolders.remove(p.getUniqueId());
			
			FinishQuest.onFinishQuest(p, this);
			return;
		}
		questHolders.put(p.getUniqueId(), progress);
	}
	
	public void setProgress(Player p, Trigger trigger, int value) {
		setProgress(p, getIndex(trigger), value);
	}
	
	public void forceComplete(Player p) {
		for(Trigger trigger : triggers) {
			setProgress(p, trigger, trigger.finishCondition);
		}
	}
	
	public ArrayList<Trigger> getTriggers() {
		return triggers;
	}
	
	public ArrayList<Trigger> getTriggers(String name) {
		ArrayList<Trigger> output = new ArrayList<Trigger>();
		for(Trigger trigger : triggers) {
			if(trigger.name == name) {
				output.add(trigger);
			}
		}
		return output;
	}
	
	public int getIndex(Trigger trigger) {
		for(int i = 0; i < triggers.size(); i++) { //GET CHECKED
			if(trigger.equals(triggers.get(i))) {
				return i;
			}
		}
		return -1;
	}
	
	public int getCondition(int index) {
		return triggers.get(index).getFinishCondition();
	}
	
	public int getProgress(Player p, int index) {
		return getProgress(p).get(index);
	}
	
	public ArrayList<Integer> getProgress(Player p) {
		if(hasQuestActive(p)) {
			return questHolders.get(p.getUniqueId());
		} else if(hasQuestFinished(p)) {
			ArrayList<Integer> finishValues = new ArrayList<Integer>();
			for(Trigger trigger : triggers) {
				finishValues.add(trigger.getFinishCondition());
			}
			return finishValues;
		}
		ArrayList<Integer> output = new ArrayList<Integer>();
		for(int i = 0; i < triggers.size(); i++) { //GET CHECKED
			output.add(0);
		}
		return output;
	}
	
	public double getPercent(Player p, int index) {
		return ((double) getProgress(p, index) / (double) getCondition(index)) * 100;
	}
	
	public double getPercentOverall(Player p) { 
		ArrayList<Integer> progress = getProgress(p);
		double progressTotal = 0;
		double finalTotal = 0;
		for(int i = 0; i < triggers.size(); i++) { //GET CHECKED
			progressTotal += progress.get(i);
			finalTotal += getCondition(i);
		}
		return (progressTotal / finalTotal) * 100;
	}
	
	public boolean hasQuestActive(Player p) {
		return questHolders.containsKey(p.getUniqueId());
	}
	
	public boolean hasQuestActive(UUID playerUUID) {
		return questHolders.containsKey(playerUUID);
	}
	
	public boolean hasQuestFinished(Player p) {
		return finishedQuest.contains(p.getUniqueId());
	}
	
	public boolean hasQuestFinished(UUID playerUUID) {
		return finishedQuest.contains(playerUUID);
	}
	
	public boolean gotQuest(Player p) {
		return hasQuestActive(p) || hasQuestFinished(p);
	}
	
	public boolean hasTrigger(String name) {
		for(Trigger trigger : triggers) {
			if(trigger.getName() == name) {
				return true;
			}
		}
		return false;
	}

	public void resetProgress(Player p, boolean fullRemoval) {
		if(fullRemoval) {
			this.questHolders.remove(p.getUniqueId());
			this.finishedQuest.remove(p.getUniqueId());
		} else {
			if(hasQuestActive(p.getUniqueId())) {
				for(int i = 0; i < triggers.size(); i++) { 
					setProgress(p, i, 0);
				}
			}
		}
	}
}
