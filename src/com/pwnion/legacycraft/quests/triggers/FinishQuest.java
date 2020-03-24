package com.pwnion.legacycraft.quests.triggers;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.pwnion.legacycraft.quests.Quest;
import com.pwnion.legacycraft.quests.QuestManager;

import net.md_5.bungee.api.ChatColor;

public class FinishQuest {

	//This is called after the player gets a quest marked as finished
	public static void onFinishQuest(Player p, Quest quest) {
		//FINISHED QUEST
		p.sendMessage(ChatColor.YELLOW + "You have competed the '" + quest.name + "' quest");
		
		/*
		//Give next quest line quest
		ArrayList<String> questLine = QuestManager.getQuestLine(quest.questLine);
		if(quest.questLineIndex >= questLine.size() - 1) {
			onFinishQuestLine(p, quest.questLine);
		} else {
			//Give the player the next quest in the quest line
			QuestManager.getQuestLineQuest(quest.questLine, quest.questLineIndex + 1).addPlayer(p);
		} //*/
	}
	
	/*
	//Called from above
	public static void onFinishQuestLine(Player p, String questLine) {
		p.sendMessage(ChatColor.GOLD + "You have competed the '" + questLine + "' quest line");
	} //*/
}
