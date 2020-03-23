package com.pwnion.legacycraft.quests.triggers;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.quests.Quest;
import com.pwnion.legacycraft.quests.QuestManager;
import com.pwnion.legacycraft.quests.Trigger;

public class SpeakToNPC {
	
	private final static String triggerName = "npc";
	
	public static void onSpeakToNPC(Player p, String npcName) {
		Util.br(p.getName() + " has called onSpeakToNPC for NPC " + npcName);
		for(Quest quest : QuestManager.getActiveQuests(p)) {
			if(quest.hasTrigger(triggerName)) {
				ArrayList<Trigger> triggers = quest.getTriggers();
				for(int i = 0; i < triggers.size(); i++) { //GET CHECKED
					Trigger trigger = triggers.get(i);
					if(trigger.getName() == triggerName) {
						String name = trigger.getNPCName();
						if(name == npcName) {
							if(trigger.getNPCData().get(name)) {
								//Remove from player inventory
							} else {
								quest.addProgress(p, i);
							}
						}
					}
				}
			}
		}
	}
}
