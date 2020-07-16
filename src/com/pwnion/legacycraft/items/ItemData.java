package com.pwnion.legacycraft.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pwnion.legacycraft.items.enhancements.Enhancement;

import net.md_5.bungee.api.ChatColor;

public class ItemData {
	
	//TODO: Test and refine this value (/lc temp, in onCommand may be useful) 
	//Any speed stats below this value are set to this value
	private static final double MIN_SPEED = 0.2;

	private String desc;
	
	private HashSet<ItemLoreFlag> flags = new HashSet<ItemLoreFlag>();
	private ArrayList<Enhancement> enhancements = new ArrayList<Enhancement>();
	private LinkedHashMap<ItemStat, Integer> stats = new LinkedHashMap<ItemStat, Integer>();
	private HashMap<ItemStat, Double> statIncrement = new HashMap<ItemStat, Double>();
	
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
	 * @param uid
	 * @param enhancements
	 * @param initial
	 */
	public void addEnhancements(ItemStack item, String uid, List<Enhancement> enhancements, boolean initial) {
		for(Enhancement enh : enhancements) {
			addEnhancement(item, enh, initial);
		}
		ItemManager.updateLore(item, uid);
	}
	
	/**
	 * Should only be used to add new enhancements
	 * 
	 * @param item
	 * @param enhancements
	 */
	public void addEnhancements(ItemStack item, Enhancement... enhancements) {
		addEnhancements(item, ItemManager.getUID(item), Arrays.asList(enhancements), true);
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
		enhancement.onRemove(lastItemStack);
		ItemManager.updateLore(lastItemStack);
	}
	
	/**
	 * @param enhancements
	 */
	public void removeEnhancements(ArrayList<Enhancement> enhancements) {
		for(Enhancement enh : enhancements) {
			enh.onRemove(lastItemStack);
			this.enhancements.remove(enh);
		}
		ItemManager.updateLore(lastItemStack);
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
	 * getStats().get(ItemStat.ATTACK)
	 * getStats().get(ItemStat.SPEED)
	 * getStats().get(ItemStat.RANGE)
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
	
	//Minimum speed as dictated by minecraft's speed stat 
	//(e.g. Diamond Sword has speed of 1.6, Fist has a speed of 4)
	//(1 LC Speed Stat therefore equals this value)
	private static final double _MIN_SPEED_MC = 0.5; 
	
	private static final double SPEED_INCREMENT = 0.3; //How much each LegacyCraft speed stat should increment speed by
	
	public double calculateSpeed() {
		double lcSpeed = stats.getOrDefault(ItemStat.SPEED, ItemManager.DEFAULT_STATS.get(ItemStat.SPEED)) - statIncrement.getOrDefault(ItemStat.SPEED, 0.0);
		if(lcSpeed < MIN_SPEED) {
			lcSpeed = MIN_SPEED;
		}
		return lcSpeed * SPEED_INCREMENT + (_MIN_SPEED_MC - SPEED_INCREMENT);
	}
	
	/**
	 * Syncs LC speed stats to Minecraft's Speed Attribute
	 * This allows the client to see correct attack speed.
	 */
	public void updateStats() {
		//Speed - 4 as base speed is 4
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
	 * Adds (or subtracts) some value to a stat
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
	
	
	/**
	 * Add invisible stat increments
	 * 
	 * Note that these increments are not permanent and are removed whenever a item is deactivated. (player logs off/server restarts)
	 * 
	 * ATTACK: may heal
	 * 
	 * @param stat
	 * @param val
	 */
	public void incrementStat(ItemStat stat, double val) {
		statIncrement.put(stat, statIncrement.getOrDefault(stat, 0.0) + val);
		updateStats();
	}
	
}
