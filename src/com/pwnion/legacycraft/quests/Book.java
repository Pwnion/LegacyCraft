package com.pwnion.legacycraft.quests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.comphenix.protocol.wrappers.nbt.NbtList;

public class Book {

	    private String title;
	    private String author;
	    private List<String> pages = new ArrayList<String>();
	  
	    public Book(String title, String author) {
	        this.title = title;
	        this.author = author;
	    }
	  
	    public PageBuilder addPage() { return new PageBuilder(this); }
	  
	    public ItemStack build() {
	        ItemStack book = MinecraftReflection.getBukkitItemStack(new ItemStack(Material.WRITTEN_BOOK));

	        NbtCompound tag = NbtFactory.ofCompound("null");
	        
	        tag.put("author", author);
	        tag.put("title", title);
	        
	        NbtList<String> pages = NbtFactory.ofList("name", this.pages);
	        
	        /*
	        for (String page : this.pages) {
	        	pages.add(page);
	        } //*/
	        
	        tag.put("pages", pages);
	        
	        NbtFactory.setItemTag(book, tag);
	        return book;
	    }
	  
	    public final class PageBuilder {
	        private String page = "{text:\"\", extra:[";
	        private boolean first = true;
	        private Book book;
	      
	        public PageBuilder(Book book) { this.book = book; }

	        public Builder add() { return new Builder(this); }
	        public Builder add(String text) { return new Builder(this).setText(text); }
	        public PageBuilder newPage() { return add("\n").build(); }
	      
	        public Book build() { book.pages.add(page += "]}"); return book; }
	    }
	  
	    public final class Builder {
	        private String text = null;
	        private ClickAction click = null;
	        private HoverAction hover = null;
	        private PageBuilder builder;
	      
	        public Builder(PageBuilder builder) { this.builder = builder; }
	      
	        public Builder setText(String text) { this.text = text; return this; }
	        public Builder clickEvent(ClickAction action) { click = action; return this; }
	        public Builder hoverEvent(HoverAction action) { hover = action; return this; }
	        public Builder clickEvent(ClickAction action, String value) { click = action; click.value = value; return this; }
	        public Builder hoverEvent(HoverAction action, String value) { hover = action; hover.value = value; return this; }
	      
	        public PageBuilder build() {
	            String extra = "{text:\"" + text + "\"";
	          
	            if (click != null) extra += ", clickEvent:{action:" + click.getString() + ", value:\"" + click.value + "\"}";
	            if (hover != null) extra += ", hoverEvent:{action:" + hover.getString() + ", value:\"" + hover.value + "\"}";
	          
	            extra += "}";
	          
	            if (builder.first) {
	            	builder.first = false;
	            } else {
	            	extra = ", " + extra;
	            }
	          
	            builder.page += extra;
	            return builder;
	        }
	    }
	  
	    public static enum ClickAction {
	        Run_Command("run_command"), Suggest_Command("suggest_command"), Open_Url("open_url"), Change_Page("change_page");
	      
	        public String value = null;
	        private String str;
	        private ClickAction(String str) { this.str = str; }
	        public String getString() { return str; }
	    }
	  
	    public static enum HoverAction {
	        Show_Text("show_text"), Show_Item("show_item"), Show_Entity("show_entity"), Show_Achievement("show_achievement");
	      
	        public String value = null;
	        private String str;
	        private HoverAction(String str) { this.str = str; }
	        public String getString() { return str; }
	    }
}
