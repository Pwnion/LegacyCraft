package com.pwnion.legacycraft.quests;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.quests.Book.Builder;
import com.pwnion.legacycraft.quests.Book.ClickAction;
import com.pwnion.legacycraft.quests.Book.PageBuilder;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;


public class QuestBook {
	
	public static void open(Player p) {
		create(p).open(p);
	}

	final static int LinksPerPage = 15;
	private static Book create(Player p) {
		//Books 14 rows
		//<= 19 columns
		
		Book questBook = new Book();
		
		int LinkPageCount = (int) (((QuestManager.getActiveQuestCount(p) + QuestManager.getCompletedQuestCount(p)) / LinksPerPage) + 1);

		ArrayList<PageBuilder> LinkPages = new ArrayList<PageBuilder>(LinkPageCount);
		
		for(int i = 0; i < LinkPageCount; i++) {
			LinkPages.add(questBook.addPage());
		}
		
		PageBuilder currentPage = LinkPages.get(0);
		int i = 1; int pageNum = 0;
		for(Quest quest : QuestManager.getActiveQuests(p)) { //*/
			
			buildQuest(p, questBook, quest, currentPage, false);
			
			if(i == LinksPerPage) {
				currentPage.build(); 
				i = 1;
				pageNum++;
				currentPage = LinkPages.get(pageNum);
			}
			i++;
		} //*/
		
		for(Quest quest : QuestManager.getCompletedQuests(p)) { //*/
			
			buildQuest(p, questBook, quest, currentPage, true);
			
			if(i == LinksPerPage) {
				currentPage.build(); 
				i = 1;
				pageNum++;
				currentPage = LinkPages.get(pageNum);
			}
			i++;
		} //*/
		currentPage.build(); 
		return questBook;
	}
	
	private static void buildQuest(Player p, Book questBook, Quest quest, PageBuilder currentPage, boolean finished) {
		
		final int maxLineLength = 19;
		String questName = quest.getName();
		if(questName.length() > maxLineLength) {
			//add ellipse
			questName = questName.substring(0, maxLineLength - 2) + "...";
		}
		
		//TODO: Setup quest page, Add Back button?
		PageBuilder questPage = questBook.addPage();
		
		questPage.add("Quest Name:\n" + quest.getName()).setColor(ChatColor.BOLD).build();
		
		Builder questText = currentPage.add(questName);
		
		//questText.hoverEvent(HoverAction.SHOW_TEXT, quest.getName());
		
		questText.clickEvent(ClickAction.CHANGE_PAGE, questPage.getPageNumber() + "");
		
		TextComponent text = questText.getComplex();
		
		if(finished) {
			text.setColor(ChatColor.GRAY);
		} else {
			text.setColor(ChatColor.DARK_BLUE);
		}
		
		TextComponent hover[] = {
				new TextComponent(quest.getName()), 
				new TextComponent("\n" + quest.getDesc() + "\n" + QuestManager.getProgressString(p, quest))};
		hover[0].setBold(true);
		//hover[0].setColor(ChatColor.RED);
		hover[1].setBold(false);
		
		text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover));

		questText.build();
		questPage.build();
	}
}
