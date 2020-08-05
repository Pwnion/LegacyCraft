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
				ArrayList<Trigger> triggers = quest.getTriggers(TriggerType.NPC);
				for(Trigger trigger : triggers) {
					String name = trigger.getNPCName();
					if(name.equalsIgnoreCase(npcName)) {
						//Check if this is a submit to npc quest
						if(trigger.getNPCData().get(name)) {
							Trigger itemTrigger = quest.getTriggers(TriggerType.ITEM).get(0);
							//Remove from player inventory
							Material mat = itemTrigger.getItem();
							int amount = itemTrigger.getFinishCondition();
							if(p.getInventory().contains(mat, amount)) {
								p.getInventory().removeItem(new ItemStack(mat, amount));
							} else {
								p.sendMessage(ChatColor.RED + "You do not have enough '" + mat.toString() + "' to progress in the quest '" + quest.getName() + "'");
								break;
							}
						}
						QuestManager.addProgress(p, quest, trigger);
					}
				}
			}
		}
	}
}
