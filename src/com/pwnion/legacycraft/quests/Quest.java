package com.pwnion.legacycraft.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.Util;

public class Quest {
	
	public Quest(String quest) {
		name = "Get a stack of diamonds";
		desc = "lol";
		triggers.add(new Trigger("item", Material.DIAMOND));
		finishCondition.add(64);
		ArrayList<Integer> currentProgress = new ArrayList<Integer>();
		currentProgress.add(0);
		questHolders.put(UUID.fromString("d1584c2b-8baa-401d-ab76-e237d543a23e"), currentProgress);
	}
	
	public String name;
	public String desc;
	
	public ArrayList<Trigger> triggers = new ArrayList<Trigger>();
	
	HashMap<UUID, ArrayList<Integer>> questHolders = new HashMap<UUID, ArrayList<Integer>>();
	HashSet<UUID> finishedQuest = new HashSet<UUID>();
	
	ArrayList<Integer> finishCondition = new ArrayList<Integer>();
	
	public void save() {
		
	}
	
	public void addPlayer(Player p) {
		questHolders.put(p.getUniqueId(), new ArrayList<Integer>(triggers.size()));
	}
	
	public void addProgress(Player p, int index, int amount) {
		setProgress(p, index, getQuestProgress(p, index) + amount);
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
		ArrayList<Integer> progress = getQuestProgress(p);
		progress.set(index, value);
		if(progress.get(index) >= finishCondition.get(index)) {
			
			//FINISHED QUEST
			p.sendMessage("You have competed the '" + name + "' quest");
			
			finishedQuest.add(p.getUniqueId());
			questHolders.remove(p.getUniqueId());
			return;
		}
		questHolders.put(p.getUniqueId(), progress);
	}
	
	public void setProgress(Player p, Trigger trigger, int value) {
		setProgress(p, getIndex(trigger), value);
	}
	
	public int getIndex(Trigger trigger) {
		for(int i = 0; i < triggers.size(); i++) {
			if(trigger.equals(triggers.get(i))) {
				return i;
			}
		}
		return -1;
	}
	
	public int getQuestProgress(Player p, int index) {
		return getQuestProgress(p).get(index);
	}
	
	public ArrayList<Integer> getQuestProgress(Player p) {
		if(hasQuestActive(p)) {
			return questHolders.get(p.getUniqueId());
		} else if(hasQuestFinished(p)) {
			return finishCondition;
		}
		ArrayList<Integer> output = new ArrayList<Integer>();
		for(int i = 0; i < triggers.size(); i++) {
			output.add(0);
		}
		return output;
	}
	
	public double getQuestPercent(Player p, int index) {
		return (getQuestProgress(p, index) / finishCondition.get(index)) * 100;
	}
	
	public double getQuestPercentOverall(Player p) { 
		ArrayList<Integer> progress = getQuestProgress(p);
		int progressTotal = 0;
		int finalTotal = 0;
		for(int i = 0; i < triggers.size(); i++) {
			progressTotal += progress.get(i);
			finalTotal += finishCondition.get(i);
			Util.br(progressTotal + " / " + finalTotal);
		}
		return (progressTotal / finalTotal) * 100;
	}
	
	public boolean hasQuestActive(Player p) {
		return questHolders.containsKey(p.getUniqueId());
	}
	
	public boolean hasQuestFinished(Player p) {
		return finishedQuest.contains(p.getUniqueId());
	}
	
	public boolean hasTrigger(String name) {
		for(Trigger trigger : triggers) {
			if(trigger.getName() == name) {
				return true;
			}
		}
		return false;
	}

	
}
