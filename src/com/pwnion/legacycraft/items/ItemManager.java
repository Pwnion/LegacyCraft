package com.pwnion.legacycraft.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.items.enhancements.Enhancement;

import net.md_5.bungee.api.ChatColor;

public class ItemManager {
	
	private static HashMap<String, ItemData> activeItems = new HashMap<String, ItemData>();
	
	//Where uid is Unique identifier
	//Not uuid as it is not universally unique
		
	//uid sizes can change with no effect to old uids
		
	private static String generateNewUID(@Nullable String preferredUID) {
		while (true) {
			if(!activeItems.containsKey(preferredUID) && preferredUID != null) {
				return preferredUID;
			}
			preferredUID = Util.generateUID(4); //1.6 million
		}
	}
	
	/**
	 * Output:
	 * Description
	 * Stats
	 * Enhancements
	 * UID
	 * 
	 * @param item 	item to get lore from
	 * @return		an arraylist of segments of lore split by title
	 */
	private static LinkedHashMap<TitleType, List<String>> stripTitles(ItemStack item) {
		LinkedHashMap<TitleType, List<String>> out = new LinkedHashMap<TitleType, List<String>>();
		List<String> lore = item.getLore();
		TitleType lastTitle = TitleType.DESCRIPTION;
		int last = 0;
		for(int i = 0; i < lore.size(); i++) {
			if(lore.get(i).contains("===")) {
				out.put(lastTitle, lore.subList(last, i - 1));
				last = i + 1;
				lastTitle = TitleType.valueOf(ChatColor.stripColor(lore.get(i)).replace("=", "").replace(" ", ""));
			}
		}
		out.put(lastTitle, lore.subList(last, lore.size() - 3));
		out.put(TitleType.UID, lore.subList(lore.size() - 1, lore.size()));
		return out;
	}
	
	private enum TitleType {
		DESCRIPTION,
		STATS,
		ENHANCEMENTS,
		UID
	}
	
	/**
	 * Deactivates a players inventory to save memory
	 * @param p
	 */
	public static void deactivate(Player p) {
		for(ItemStack item : p.getInventory().getContents()) {
			activeItems.remove(getUID(item));
		}
	}
	
	public static ItemData generateItem(ItemStack item, String description, int attack, int speed, int range) {
		String uid = generateNewUID(null);
		ItemData data = new ItemData(description, item);
		data.setStats(attack, speed, range);
		activeItems.put(uid, data);
		//item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		updateLore(item, uid);
		return data;
	}
	
	public static ItemData getItemData(@Nullable ItemStack item, @Nonnull String uid) {
		if(activeItems.containsKey(uid)) {
			return activeItems.get(uid);
		}
		return getItemData(item);
	}
	
	public static ItemData getItemData(@Nullable ItemStack item) {
		String uid = getUID(item);
		if(uid == null) {
			return null;
		}
		if(activeItems.containsKey(uid)) {
			ItemData data = getItemData(item, getUID(item));
			data.setLastItemStack(item);
			return data;
		}
		HashMap<TitleType, List<String>> lore = stripTitles(item);
		
		Util.br(lore);
		
		LinkedHashMap<Stats, Integer> stats = new LinkedHashMap<Stats, Integer>();
		for(String line : lore.getOrDefault(TitleType.STATS, Collections.emptyList())) {
			String[] statLine = ChatColor.stripColor(line).replace(" ", "").split(":");
			Stats stat = Stats.valueOf(statLine[0].toUpperCase());
			if(stat != null) {
				stats.put(stat, Integer.parseInt(statLine[1]));
			}
		}
		
		ArrayList<Enhancement> enhancements = new ArrayList<Enhancement>();
		for(String line : lore.getOrDefault(TitleType.ENHANCEMENTS, Collections.emptyList())) {
			Enhancement enh = Enhancement.fromName(ChatColor.stripColor(line).replace(" - ", ""));
			if(enh != null) {
				enhancements.add(enh);
			}
		}
		
		ItemData data = new ItemData(lore.get(TitleType.DESCRIPTION), stats, item);
		activeItems.put(uid, data);
		data.addEnhancements(item, enhancements);
		
		return data;
	}
	
	private static boolean isActive(@Nullable String uid) {
		if(uid == null) {
			return false;
		}
		return activeItems.containsKey(uid);
	}
	
	private static boolean isActive(@Nullable ItemStack item) {
		return isActive(getUID(item));
	}
	
	//gets the UID of item
	public static String getUID(ItemStack item) {
		if(item == null || item.getLore() == null || item.getLore().size() < 3) {
			return null;
		}
		List<String> lore = item.getLore();
		return lore.get(lore.size() - 1).split("@")[1];
	}
	
	//gets the UID of item or generates a new one
	public static String getUID(ItemStack item, @Nullable String preferredUID) {
		String uid = getUID(item);
		if(uid == null) {
			return generateNewUID(preferredUID);
		}
		return uid;
	}
	
	public static ArrayList<Enhancement> getEnhancements(ItemStack item) {
		ItemData data = getItemData(item);
		if(data == null) {
			return new ArrayList<Enhancement>(0);
		}
		return data.getEnhancements();
	}
	
	private static final HashMap<Stats, Integer> DEFAULT_STATS;
	static {
		DEFAULT_STATS = new HashMap<Stats, Integer>();
		DEFAULT_STATS.put(Stats.ATTACK, 1);
		DEFAULT_STATS.put(Stats.SPEED, 1);
		DEFAULT_STATS.put(Stats.RANGE, 1);
	}
	
	public static HashMap<Stats, Integer> getStats(ItemStack item) {
		ItemData data = getItemData(item);
		if(data == null) {
			return DEFAULT_STATS;
		}
		return data.getStats();
	}
	
	//Forces item to use uid
	public static void updateLore(ItemStack item, String uid) {
		ItemData itemData = getItemData(item, uid);
		 
		ArrayList<String> lore = new ArrayList<String>();
		
		lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + itemData.getDesc());
		lore.add("");
		
		if(itemData.hasStats()) {
			lore.add(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " === STATS === ");
			for(Stats stat : itemData.getStats().keySet()) {
				String name = stat.toString().substring(0, 1) + stat.toString().substring(1).toLowerCase();
				lore.add(ChatColor.GRAY + " " + name + ": " + itemData.getStat(stat));
			}
			lore.add("");
		}
		
		//Enhancements
		if(itemData.hasEnhancements()) {
			lore.add(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " === ENHANCEMENTS === ");
			for(Enhancement e : itemData.getEnhancements()) {
				lore.add(ChatColor.GRAY + " - " + e.getName());
			}
			lore.add("");
		}
		
		lore.add("");
		lore.add(ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + "@" + uid);
		item.setLore(lore);
	}
	
	//Updates lore with same uid
	public static void updateLore(ItemStack item) {
		String uid = getUID(item, null);
		updateLore(item, uid);
	}
	
	public static boolean changeUID(ItemStack item, String newUID) {
		if(isActive(newUID) || !isActive(item)) {
			return false;
		}
		String oldUID = getUID(item);
		activeItems.put(newUID, getItemData(item));
		activeItems.remove(oldUID);
		updateLore(item, newUID);
		return true;
	}
}
