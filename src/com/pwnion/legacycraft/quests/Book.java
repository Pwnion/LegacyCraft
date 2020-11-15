package com.pwnion.legacycraft.quests;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.pwnion.legacycraft.Util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Maximum 50 pages with 256 characters per page.
 */
public class Book {

	    private String title;
	    private String author;
	    private final BookMeta meta = (BookMeta) Bukkit.getItemFactory().getItemMeta(Material.WRITTEN_BOOK);
	    private final ArrayList<BaseComponent[]> pages = new ArrayList<>();
	  
	    public Book(String title, String author) {
	        this.title = title;
	        this.author = author;
	    }
	    
	    public Book() {
	    	this.title = "title";
	        this.author = "author";
	    }
	  
	    public PageBuilder addPage() { return new PageBuilder(this); }
	    
	    public ItemStack build() {
	    	ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
	    	
	    	meta.setTitle(title);
	    	meta.setAuthor(author);
	    	meta.spigot().setPages(pages);
	    	
	    	book.setItemMeta(meta);
	    	return book;
	    }
	    	    
	    public void open(Player p) {
	    	int slot = p.getInventory().getHeldItemSlot();
	    	ItemStack old = p.getInventory().getItem(slot);
	    	ItemStack bookitem = build();
	    	p.getInventory().setItem(slot, bookitem);
	    	try {
	    	    PacketContainer pc = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.OPEN_BOOK);
	    	    
	    	    ProtocolLibrary.getProtocolManager().sendServerPacket(p, pc);
	    	} catch (Exception e) {
	    		//May catch on a lag spike
	    	    e.printStackTrace();
	    	    Util.print(e);
	    	}
	    	p.getInventory().setItem(slot, old);
	    }
	  
	    public final class PageBuilder {
	    	private final Book book;
	        private ArrayList<BaseComponent> page = new ArrayList<>();
	        private final int index;
	      
	        private PageBuilder(Book book) { 
	        	this.book = book; 
	        	index = book.pages.size();
	        	book.pages.add(null);
	        }

	        public Builder add() { return new Builder(this); }
	        public Builder add(String text) { return new Builder(this).setText(text); }
	        
	        public int getPageNumber() {
	        	return index + 1;
	        }
	        
	        public void build() {
	        	book.pages.set(index, page.toArray(new BaseComponent[0]));
	        }
	    }
	    
	    public final class Builder {
	    	private final PageBuilder page;
	    	private TextComponent message = new TextComponent();
	    	
	    	private Builder(PageBuilder page) { this.page = page; }
	    	
	    	public Builder setText(String message) { 
	    		this.message.setText(message); 
	    		return this; 
	    	}
	    	
	    	public Builder setColor(ChatColor color) {
	    		message.setColor(color);
	    		return this;
	    	}
	    	
	    	public Builder clickEvent(ClickAction action, String value) { 
	    		this.message.setClickEvent(new ClickEvent(action.value, value)); 
	    		return this; 
	    	}
	    	
	    	public Builder clickEvent(ClickAction action, int value) {
	    		return clickEvent(action, value + "");
	    	}
	    	
	    	public Builder hoverEvent(HoverAction action, String value) { 
	    		this.message.setHoverEvent(new HoverEvent(action.value, new ComponentBuilder(value).create())); 
	    		return this; 
	    	}
	    	
	    	public Builder hoverEvent(HoverAction action, TextComponent[] value) { 
	    		this.message.setHoverEvent(new HoverEvent(action.value, value)); 
	    		return this; 
	    	}
	    	
	    	public TextComponent getComplex() {
	    		return message;
	    	}
	    	
	    	public PageBuilder build() {
	    		message.setText(message.getText() + "\n");
	    		page.page.add(message);
	    		return page;
	    	}
	    }
	    
	    public enum ClickAction {
	    	RUN_COMMAND(ClickEvent.Action.RUN_COMMAND), 
	    	SUGGEST_COMMAND(ClickEvent.Action.SUGGEST_COMMAND), 
	        OPEN_URL(ClickEvent.Action.OPEN_URL), 
	        CHANGE_PAGE(ClickEvent.Action.CHANGE_PAGE);
	      
	        private ClickEvent.Action value = null;
	        private ClickAction(ClickEvent.Action value) { this.value = value; }
	    }
	  
	    public enum HoverAction {
	    	SHOW_TEXT(HoverEvent.Action.SHOW_TEXT), 
	    	SHOW_ITEM(HoverEvent.Action.SHOW_ITEM), 
	    	SHOW_ENTITY(HoverEvent.Action.SHOW_ENTITY), 
	    	SHOW_ACHIEVEMENT(HoverEvent.Action.SHOW_ACHIEVEMENT);
	      
	        private HoverEvent.Action value = null;
	        private HoverAction(HoverEvent.Action value) { this.value = value; }
	    }
}
