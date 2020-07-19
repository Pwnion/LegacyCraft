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
	
	private ArrayList<Enhancement> enhancements = new ArrayList<Enhancement>();
	private LinkedHashMap<ItemStat, Integer> stats = new LinkedHashMap<ItemStat, Integer>();
	private HashMap<ItemStat, Double> statIncrement = new HashMap<ItemStat, Double>();
	
	private ItemStack lastItemStack;
	
	private ItemTier tier = ItemTier.NONE;
	private ItemType type = ItemType.NONE;
	
	@Override
	public String toString() {
		return "[Description: " + desc +
			  ", Stats: " + stats +
			  ", StatIncrement: " + statIncrement +
			  ", ItemType: " + type +
			  ", LastItemStack: " + lastItemStack + "]";
	}
	
	/**
	 * Used when activating an item from an inactive state
	 * 
	 * @param desc
	 * @param stats
	 * @param item
	 */
	public ItemData(List<String> desc, ItemTier tier, ItemType type, @Nullable LinkedHashMap<ItemStat, Integer> stats, ItemStack item) {
		setDesc(ChatColor.stripColor(String.join(" ", desc)));
		this.lastItemStack = item;
		
		this.tier = tier;
		this.type = type;
		
		if(stats != null) {
			setStats(stats);
		}
	}
	
	/**
	 * Used when creating an item
	 * 
	 * @param desc
	 * @param item
	 * 
	 * @throws IllegalArgumentException if desc contains " | "
	 */
	public ItemData(String desc, ItemStack item) throws IllegalArgumentException {
		setDesc(desc);
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
	 * 
	 * @throws IllegalArgumentException if desc contains " | "
	 */
	public void setDesc(String desc) throws IllegalArgumentException {
		if(desc.contains(" | ")) {
			throw new IllegalArgumentException("description cannot contain \" | \"");
		}
		this.desc = desc;
	}
	
	/**
	 * Gets the first enhancement with the name <br>
	 * Ignores caps, Ignores spaces.
	 * 
	 * @param name
	 * @return
	 * 
	 * @Nullable if no enhancement with name
	 */
	public Enhancement getEnhancement(String name) {
		for(Enhancement enh : enhancements) {
			if(enh.getName().toLowerCase().replace(" ", "") == name.toLowerCase().replace(" ", "")) {
				return enh;
			}
		}
		return null;
	}

	/**
	 * @return Enhancements
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
	public void addEnhancements(String uid, List<Enhancement> enhancements, boolean initial) {
		for(Enhancement enh : enhancements) {
			addEnhancement(enh, initial);
		}
		ItemManager.updateLore(lastItemStack, uid);
	}
	
	/**
	 * Should only be used to add new enhancements
	 * 
	 * @param item
	 * @param enhancements
	 */
	public void addEnhancements(Enhancement... enhancements) {
		addEnhancements(ItemManager.getUID(lastItemStack), Arrays.asList(enhancements), true);
	}
	
	/**
	 * @param item
	 * @param enhancement
	 * @param initial
	 */
	public void addEnhancement(Enhancement enhancement, boolean initial) {
		enhancements.add(enhancement);
		enhancement.onEquip(lastItemStack, initial);
	}
	
	/**
	 * Get enhancement from getEnhancements()
	 * 
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
	 * Gets the last recorded itemStack associated to this set of Data. <br>
	 * itemStacks are recorded every time they are created, used, and when players log in with them.
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
	 * getStats().get(ItemStat.ATTACK) <br>
	 * getStats().get(ItemStat.SPEED) <br>
	 * getStats().get(ItemStat.RANGE) <br>
	 * 
	 * @return	All the stats
	 */
	public LinkedHashMap<ItemStat, Integer> getStats() {
		return stats;
	}

	/**
	 * Sets all stats <br>
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
	 * Syncs LC speed stats to Minecraft's Speed Attribute <br>
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
	
	public boolean hasType() {
		return type != ItemType.NONE;
	}
	
	/**
	 * @return the tier
	 */
	public ItemTier getTier() {
		return tier;
	}

	/**
	 * @param tier the tier to set
	 */
	public void getTier(ItemTier tier) {
		this.tier = tier;
	}
	
	
	/**
	 * Add invisible stat increments <br>
	 * 
	 * Note that these increments are not permanent and are removed whenever a item is deactivated. (player logs off/server restarts)
	 * 
	 * @param stat
	 * @param val
	 */
	public void incrementStat(ItemStat stat, double val) {
		statIncrement.put(stat, statIncrement.getOrDefault(stat, 0.0) + val);
		updateStats();
	}
	
	
}
