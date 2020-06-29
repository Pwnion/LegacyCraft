package com.pwnion.legacycraft.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Multimap;
import com.pwnion.legacycraft.items.enhancements.Enhancement;

import net.md_5.bungee.api.ChatColor;

public class ItemData {

	private String desc;
	
	private HashSet<ItemLoreFlag> flags = new HashSet<ItemLoreFlag>();
	private ArrayList<Enhancement> enhancements = new ArrayList<Enhancement>();
	private LinkedHashMap<ItemStat, Integer> stats = new LinkedHashMap<ItemStat, Integer>();
	
	private ItemStack lastItemStack;
	
	private ItemType type;
	
	/**
	 * Used when activating an item from an inactive state
	 * 
	 * @param desc
	 * @param stats
	 * @param item
	 */
	public ItemData(List<String> desc, @Nullable LinkedHashMap<ItemStat, Integer> stats, ItemStack item) {
		this.desc = ChatColor.stripColor(String.join(" ", desc));
		this.lastItemStack = item;
		
		if(stats != null) {
			setStats(stats);
		} else {
			addFlags(ItemLoreFlag.STATS);
		}
	}
	
	/**
	 * Used when creating an item
	 * 
	 * @param desc
	 * @param item
	 */
	public ItemData(String desc, ItemStack item) {
		this.desc = desc;
		this.lastItemStack = item;
		
		
	}

	/**
	 * @return
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return
	 */
	public ArrayList<Enhancement> getEnhancements() {
		return enhancements;
	}

	/**
	 * @param enhancements
	 */
	public void setEnhancements(ArrayList<Enhancement> enhancements) {
		this.enhancements = enhancements;
	}
	
	/**
	 * @param item
	 * @param enhancements
	 * @param initial
	 */
	public void addEnhancements(ItemStack item, List<Enhancement> enhancements, boolean initial) {
		for(Enhancement enh : enhancements) {
			addEnhancement(item, enh, initial);
		}
		ItemManager.updateLore(item);
	}
	
	/**
	 * Should only be used to add new enhancements
	 * 
	 * @param item
	 * @param enhancements
	 */
	public void addEnhancements(ItemStack item, Enhancement... enhancements) {
		addEnhancements(item, Arrays.asList(enhancements), true);
	}
	
	/**
	 * @param item
	 * @param enhancement
	 * @param initial
	 */
	public void addEnhancement(ItemStack item, Enhancement enhancement, boolean initial) {
		enhancements.add(enhancement);
		lastItemStack = item;
		enhancement.onEquip(item, initial);
	}
	
	/**
	 * @param enhancement
	 */
	public void removeEnhancement(Enhancement enhancement) {
		enhancements.remove(enhancement);
	}
	
	/**
	 * @param enhancements
	 */
	public void removeEnhancements(ArrayList<Enhancement> enhancements) {
		enhancements.removeAll(enhancements);
	}
	
	/**
	 * @return
	 */
	public boolean hasEnhancements() {
		/* 
		if(flags.contains(ItemLoreFlag.ENHANCEMENTS)) {
			return false;
		} //*/
		return enhancements.size() > 0;
	}
	
	/**
	 * Checks if the item has stats
	 * 
	 * @return if stats size is > 0
	 */
	public boolean hasStats() {
		/*
		if(flags.contains(ItemLoreFlag.STATS)) {
			return false;
		} //*/
		return stats.size() > 0;
	}

	/**
	 * Gets the last recorded itemStack associated to this set of Data.
	 * itemStacks are recorded every time they are created, used, enhancements are added, and when players log in with them.
	 * 
	 * @return 		The last itemStack associated to this set of Data
	 */
	public ItemStack getLastItemStack() {
		return lastItemStack;
	}

	/**
	 * Sets the last itemStack associated to this set of Data
	 * 
	 * @param lastItemStack	The last itemStack associated to this set of Data
	 */
	public void setLastItemStack(ItemStack lastItemStack) {
		this.lastItemStack = lastItemStack;
	}

	/**
	 * 
	 * 
	 * @return	All the stats
	 */
	public LinkedHashMap<ItemStat, Integer> getStats() {
		return stats;
	}

	/**
	 * Sets all stats
	 * Must include all stats
	 * 
	 * @param stats	An ordered HashMap of all stats
	 */
	public void setStats(LinkedHashMap<ItemStat, Integer> stats) {
		this.stats = stats;
		updateStats();
	}
	
	public void setStats(int attack, int speed, int range) {
		stats.put(ItemStat.ATTACK, attack);
		stats.put(ItemStat.SPEED, speed);
		stats.put(ItemStat.RANGE, range);
		updateStats();
	}
	
	public static double minSpeed = 0.5;
	public static double speedIncrement = 0.3;
	
	public double calculateSpeed() {
		return stats.getOrDefault(ItemStat.SPEED, 1) * speedIncrement + (minSpeed - speedIncrement);
	}
	
	public void updateStats() {
		final AttributeModifier att = new AttributeModifier(UUID.nameUUIDFromBytes("Base".getBytes()), "Base", calculateSpeed() - 4, Operation.ADD_NUMBER);
		ItemMeta meta = lastItemStack.getItemMeta();
		//Stop duplication
		meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, att);
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, att);
		lastItemStack.setItemMeta(meta);
	}
	
	/**
	 * Gets a stat
	 * 
	 * @param stat	The stat to get
	 * @return		The stat's value
	 */
	public int getStat(ItemStat stat) {
		return stats.get(stat);
	}
	
	/**
	 * Sets a stat to some value
	 * 
	 * @param stat	The stat to set to value
	 * @param val	The value to set the stat to
	 * @return 		This
	 */
	public ItemData setStat(ItemStat stat, int val) {
		stats.put(stat, val);
		updateStats();
		return this;
	}
	
	/**
	 * Adds some value to a stat
	 * 
	 * @param stat 	The stat to add to
	 * @param val	The value to add to the stat
	 */
	public void addToStat(ItemStat stat, int val) {
		stats.put(stat, stats.get(stat) + val);
		updateStats();
	}

	/**
	 * @param flags	The flags to add
	 */
	public void addFlags(ItemLoreFlag... flags) {
		this.flags.addAll(Arrays.asList(flags));
	}

	/**
	 * @return the type
	 */
	public ItemType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ItemType type) {
		this.type = type;
	}
	
}
