package com.pwnion.legacycraft.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.items.enhancements.Enhancement;

import net.md_5.bungee.api.ChatColor;

public class ItemManager {
	
	private static HashMap<String, ItemData> activeItems = new HashMap<String, ItemData>();
	
	private enum TitleType {
		TIER_ITEMTYPE,
		DESCRIPTION,
		STATS,
		ENHANCEMENTS,
		UID
	}
	
	//Where uid is Unique identifier
	//Not uuid as it is not universally unique
		
	//uid sizes can change with no effect to old uids
	
	/**
	 * Generates a UID with collision checking <br>
	 *  <br>
	 * Colour Codes and Spaces may be stripped from the preferredUID <br>
	 * 
	 * @param preferredUID	null if no preferred
	 * @return				4-character alphanumeric string (or preferredUID)
	 * 
	 * @throws IllegalArgumentException if preferredUID contains the strings "===" or "@"
	 * 
	 * @NonNull
	 */
	private static String generateNewUID(@Nullable String preferredUID) throws IllegalArgumentException {
		if(preferredUID != null && (preferredUID.contains("===") || preferredUID.contains("@"))) {
			throw new IllegalArgumentException("uid cannont contain the strings '===' or '@'");
		}
		while (true) {
			if(!activeItems.containsKey(preferredUID) && preferredUID != null) {
				return preferredUID;
			}
			preferredUID = Util.generateUID(4); //1.6 million
		}
	}
	
	/**
	 * Output: <br>
	 * Rarity and ItemType <br>
	 * Description <br>
	 * Stats <br>
	 * Enhancements <br>
	 * UID <br>
	 * 
	 * @param item 	item to get lore from
	 * @return		an arraylist of segments of lore split by title
	 */
	private static LinkedHashMap<TitleType, List<String>> stripTitles(ItemStack item) {
		LinkedHashMap<TitleType, List<String>> out = new LinkedHashMap<TitleType, List<String>>();
		List<String> lore = item.getLore();
		TitleType lastTitle = TitleType.DESCRIPTION;
		int last = 0;
		
		if(lore.get(0).contains(" | ")) {
			out.put(TitleType.TIER_ITEMTYPE, lore.subList(0, 1));
			last = 1; //Description is one line down
		}
		
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
	
	/**
	 * Deactivates a players inventory to save memory
	 * @param p
	 */
	public static void deactivate(Player p) {
		for(ItemStack item : p.getInventory().getContents()) {
			activeItems.remove(getUID(item));
		}
	}
	
	/**
	 * Activates an inactive item. <br>
	 * If item was already active creates a new ItemData with a new UID. <br>
	 * 
	 * @Nullable if item does not have a uid in lore
	 * 
	 * @param item
	 * @return
	 */
	public static ItemData activate(ItemStack item) {
		String uid = getUID(item);
		if(uid == null) {
			return null;
		}
		uid = generateNewUID(uid);
		
		HashMap<TitleType, List<String>> lore = stripTitles(item);
		
		Util.br(lore);
		
		ItemType type = ItemType.NONE;
		ItemTier tier = ItemTier.NONE;
		if(lore.containsKey(TitleType.TIER_ITEMTYPE)) {
			String spl[] = ChatColor.stripColor(lore.get(TitleType.TIER_ITEMTYPE).get(0)).split(" | ");
			String splstr = ChatColor.stripColor(lore.get(TitleType.TIER_ITEMTYPE).get(0));
			Util.br(splstr);
			tier = ItemTier.fromString(spl[0]);
			type = ItemType.fromString(spl[1]);
		}
		
		LinkedHashMap<ItemStat, Integer> stats = new LinkedHashMap<ItemStat, Integer>();
		for(String line : lore.getOrDefault(TitleType.STATS, Collections.emptyList())) {
			String[] statLine = ChatColor.stripColor(line).replace(" ", "").split(":");
			ItemStat stat = ItemStat.valueOf(statLine[0].toUpperCase());
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
		
		ItemData data = new ItemData(lore.get(TitleType.DESCRIPTION), tier, type, stats, item);
		activeItems.put(uid, data);
		data.addEnhancements(uid, enhancements, false);
		
		return data;
	}
	
	/**
	 * Adds stats and a description to an item. Generates a random UID for the item.
	 * 
	 * @param item			The item to add stats for
	 * @param description	The description for the item
	 * @param attack		The damage per hit the item should do
	 * @param speed			The speed (> 0) used for attack cooldown
	 * @param range			The range (> 0) used for range for 3+ blocks
	 * @return				The itemData unique to that item
	 */
	public static ItemData generateItem(ItemStack item, String description, int attack, int speed, int range) {
		String uid = generateNewUID(null);
		ItemData data = new ItemData(description, item);
		data.setStats(attack, speed, range);
		activeItems.put(uid, data);
		item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		updateLore(item, uid);
		return data;
	}
	
	/**
	 * Generates stats and a description for an item. Generates a random UID for the item.
	 * 
	 * @param item
	 * @param tier
	 * @param type
	 * @return
	 */
	public static ItemData generateItem(ItemStack item, ItemTier tier, ItemType type) {
		String uid = generateNewUID(null);
		ItemData data = new ItemData("Some randomised description.", item);
		
		data.setTier(tier);
		data.setType(type);
		data.setStats(generateStats(tier, type, 2));
		activeItems.put(uid, data);
		item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		updateLore(item, uid);
		return data;
	}
	
	/**
	 * Generated stats may be negative. (ItemData handles this). <br>
	 * Stats generated from tier base power + type modifiers +- range
	 * 
	 * @param tier
	 * @param type
	 * @param range		Range from the value to generate
	 * @return
	 */
	private static LinkedHashMap<ItemStat,Integer> generateStats(ItemTier tier, ItemType type, int range) {
		LinkedHashMap<ItemStat,Integer> out = new LinkedHashMap<ItemStat,Integer>();
		Random rnd = new Random();
		for(ItemStat stat : ItemStat.values()) {
			int mid = tier.power + type.get(stat);
			int gen = Util.randomInt(mid - range,  mid + range);
			if(gen < stat.getMin()) {
				gen = stat.getMin();
			}
			out.put(stat, gen);
		}
		return out;
	}
	
	/**
	 *  Gets the item data for a uid. If inactive activates the item from the itemStack.
	 * 
	 * @param item
	 * @param uid
	 * @return
	 * 
	 * @Nullable if item is inactive and has no uid
	 */
	public static ItemData getItemData(@Nullable ItemStack item, String uid) {
		if(activeItems.containsKey(uid)) {
			return activeItems.get(uid);
		}
		return getItemData(item);
	}
	
	/**
	 * Gets the item data for an item. If inactive activates the item.
	 * 
	 * @param item	
	 * @return 		The itemData of the item
	 * 
	 * @Nullable If item is null or has a lore size less than 3
	 */
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
		return activate(item);
	}
	
	/**
	 * Checks if an ItemData instance has been created for this uid. <br>
	 * If null returns false.
	 * 
	 * @param uid
	 * @return
	 */
	private static boolean isActive(@Nullable String uid) {
		if(uid == null) {
			return false;
		}
		return activeItems.containsKey(uid);
	}
	
	/**
	 * Checks the item for a uid and checks if that uid has an ItemData associated with it
	 * 
	 * @param item
	 * @return
	 */
	private static boolean isActive(@Nullable ItemStack item) {
		return isActive(getUID(item));
	}
	
	/**
	 * Gets the UID from the last line in the items lore. <br>
	 * The UID must be after an @ symbol
	 * 
	 * @param item		
	 * @return			UID
	 * 
	 * @throws IndexOutOfBoundsException if lore size is >= 3 and has no text after @ symbol in last line of lore 
	 * @throws IndexOutOfBoundsException if lore size is >= 3 and no @ symbol is found in last line of lore
	 * 
	 * @Nullable If item is null or has a lore size less than 3
	 */
	public static String getUID(ItemStack item) throws IndexOutOfBoundsException {
		if(item == null || item.getLore() == null || item.getLore().size() < 3) {
			return null;
		}
		List<String> lore = item.getLore();
		return lore.get(lore.size() - 1).split("@")[1];
	}
	
	/**
	 * gets the UID of item or generates a new one. <br>
	 * 
	 * If item is null generates a new UID
	 * 
	 * @param item			
	 * @param preferredUID	null if no preferred
	 * @return
	 * 
	 * @NonNull
	 */
	public static String getUID(ItemStack item, @Nullable String preferredUID) {
		String uid = getUID(item);
		if(uid == null) {
			return generateNewUID(preferredUID);
		}
		return uid;
	}
	
	/**
	 * Gets Enhancements or returns empty arraylist
	 * 
	 * @param item
	 * @return
	 */
	public static ArrayList<Enhancement> getEnhancements(ItemStack item) {
		ItemData data = getItemData(item);
		if(data == null) {
			return new ArrayList<Enhancement>(0);
		}
		return data.getEnhancements();
	}
	
	/**
	 * Gets stats or returns default
	 * 
	 * @param item
	 * @return
	 */
	public static int getStat(ItemStack item, ItemStat stat) {
		ItemData data = getItemData(item);
		if(data == null) {
			return stat.getDefault();
		}
		return data.getStat(stat);
	}
	
	/**
	 * Forces item to use uid
	 * 
	 * @param item
	 * @param uid
	 * 
	 * @throws NullPointerException if uid is null
	 */
	public static void updateLore(ItemStack item, @NonNull String uid) throws NullPointerException {
		ItemData itemData = getItemData(item, uid);
		 
		ArrayList<String> lore = new ArrayList<String>();
		
		Util.br(itemData);
		
		//Tier - Type
		if(itemData.hasTier() && itemData.hasType()) {
			lore.add(ChatColor.DARK_GRAY.toString() + itemData.getTier() + " | " + itemData.getType());
		}
		
		//Description
		lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + itemData.getDesc());
		lore.add("");
		
		if(itemData.hasStats()) {
			lore.add(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " === STATS === ");
			for(ItemStat stat : itemData.getStats()) {
				lore.add(ChatColor.GRAY + " " + stat + ": " + itemData.getStat(stat));
			}
			lore.add("");
		}
		
		//Enhancements
		if(itemData.hasEnhancements()) {
			lore.add(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " === ENHANCEMENTS === ");
			Util.br(itemData.getEnhancements());
			for(Enhancement enh : itemData.getEnhancements()) {
				lore.add(ChatColor.GRAY + " - " + enh.getName());
			}
			lore.add("");
		}
		
		lore.add("");
		lore.add(ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + "@" + uid);
		item.setLore(lore);
	}
	
	/**
	 * Updates lore with same uid
	 * 
	 * @param item
	 */
	public static void updateLore(ItemStack item) {
		String uid = getUID(item, null);
		updateLore(item, uid);
	}
	
	/**
	 * Changes an items UID. <br>
	 * newUID must be unused, item must be active
	 * 
	 * @param item		the ItemStack to change
	 * @param newUID	the uid to change to
	 * @return			if item successfully changed uid
	 */
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
