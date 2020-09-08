package com.pwnion.legacycraft.quests;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.pwnion.legacycraft.quests.Book.Builder;
import com.pwnion.legacycraft.quests.Book.ClickAction;
import com.pwnion.legacycraft.quests.Book.HoverAction;
import com.pwnion.legacycraft.quests.Book.PageBuilder;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;


public class QuestBook {
	
	public static void open(Player p) {
		create(p).open(p);
	}

	final static int LINKS_PER_PAGE = 15;
	private static Book create(Player p) {
		//Books 14 rows
		//<= 19 columns
		
		Book questBook = new Book();
		
		int linkPageCount = (int) (((QuestManager.getActiveQuestCount(p) + QuestManager.getCompletedQuestCount(p)) / LINKS_PER_PAGE) + 1);

		ArrayList<PageBuilder> linkPages = new ArrayList<PageBuilder>(linkPageCount);
		
		for(int i = 0; i < linkPageCount; i++) {
			linkPages.add(questBook.addPage());
		}
		
		PageBuilder currentPage = linkPages.get(0);
		int i = 1; int pageNum = 0;
		
		for(Quest quest : QuestManager.getActiveQuests(p)) {
			buildQuest(p, questBook, quest, currentPage, false);
			
			if(i == LINKS_PER_PAGE) {
				currentPage.build(); 
				i = 1;
				pageNum++;
				currentPage = linkPages.get(pageNum);
			}
			i++;
		}
		
		for(Quest quest : QuestManager.getCompletedQuests(p)) {
			buildQuest(p, questBook, quest, currentPage, true);
			
			if(i == LINKS_PER_PAGE) {
				currentPage.build(); 
				i = 1;
				pageNum++;
				currentPage = linkPages.get(pageNum);
			}
			i++;
		}
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
		
		//Quest page should contain:
		//Name, Description, Triggers, Trigger Hints?
		questPage.add("Quest Name:\n" + quest.getName()).setColor(ChatColor.BOLD).build();
		
		Builder questText = currentPage.add(questName);
		
		questText.clickEvent(ClickAction.CHANGE_PAGE, questPage.getPageNumber());
		
		if(finished) {
			questText.setColor(ChatColor.GRAY);
		} else {
			questText.setColor(ChatColor.DARK_BLUE);
		}
		
		TextComponent hover[] = {
				new TextComponent(quest.getName() + "\n"), 
				new TextComponent(quest.getDesc() + 
						   "\n" + QuestManager.getProgressString(p, quest))};
		hover[0].setBold(true);
		//hover[0].setColor(ChatColor.RED);
		hover[1].setBold(false);
		
		questText.hoverEvent(HoverAction.SHOW_TEXT, hover);
		
		questText.build();
		questPage.build();
	}
}
