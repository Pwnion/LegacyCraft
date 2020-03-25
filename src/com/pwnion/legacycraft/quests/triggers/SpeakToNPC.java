package com.pwnion.legacycraft.quests.triggers;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.quests.Quest;
import com.pwnion.legacycraft.quests.QuestManager;
import com.pwnion.legacycraft.quests.Trigger;
import com.pwnion.legacycraft.quests.TriggerType;

public class SpeakToNPC {
	
	//Called manually when a player speaks to an npc
	public static void onSpeakToNPC(Player p, String npcName) {
		Util.br(p.getName() + " has called onSpeakToNPC for NPC " + npcName);
		
		for(Quest quest : QuestManager.getActiveQuests(p)) {
			if(quest.hasTrigger(TriggerType.NPC)) {
				ArrayList<Trigger> triggers = quest.getTriggers();
				for(int i = 0; i < triggers.size(); i++) { //GET CHECKED
					Trigger trigger = triggers.get(i);
					if(trigger.getName() == TriggerType.NPC) {
						String name = trigger.getNPCName();
						if(name.equalsIgnoreCase(npcName)) {
							//Check if this is a submit to npc quest
							if(trigger.getNPCData().get(name)) {
								//Remove from player inventory
								Material mat = triggers.get(i - 1).getItem();
								int count = 0;
								if(p.getInventory().contains(mat)) {
									for(ItemStack items : p.getInventory().all(mat).values()) {
										count += items.getAmount();
									}
								}
								
								if(count < triggers.get(i - 1).getFinishCondition()) {
									p.sendMessage(ChatColor.RED + "You do not have enough '" + mat.toString() + "' to progress in the quest '" + quest.name + "'");
									break;
								} 
							}
							QuestManager.addProgress(p, quest, i);
						}
					}
				}
			}
		}
	}
}
