package com.pwnion.legacycraft.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.items.enhancements.Enhancement;

public class ItemData {

	private String desc;
	
	private ArrayList<Enhancement> enhancements = new ArrayList<Enhancement>();
	
	private ItemStack lastItemStack = null;
	
	public ItemData(List<String> desc, ArrayList<Enhancement> enhancements, ItemStack item) {
		this.desc = String.join(" ", desc);
		this.enhancements = enhancements;
		this.lastItemStack = item;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public ArrayList<Enhancement> getEnhancements() {
		return enhancements;
	}

	public void setEnhancements(ArrayList<Enhancement> enhancements) {
		this.enhancements = enhancements;
	}
	
	public void addEnhancement(Enhancement enhancement) {
		enhancements.add(enhancement);
	}
	
	public void addEnhancements(ArrayList<Enhancement> enhancements) {
		this.enhancements.addAll(enhancements);
	}
	
	public void removeEnhancement(Enhancement enhancement) {
		enhancements.remove(enhancement);
	}
	
	public void removeEnhancements(ArrayList<Enhancement> enhancements) {
		enhancements.removeAll(enhancements);
	}
	
	public boolean hasEnhancements() {
		return enhancements.size() > 0;
	}

	public ItemStack getLastItemStack() {
		return lastItemStack;
	}

	public void setLastItemStack(ItemStack lastItemStack) {
		this.lastItemStack = lastItemStack;
	}
	
}
