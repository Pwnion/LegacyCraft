package com.pwnion.legacycraft.items;

import java.util.ArrayList;
import java.util.HashMap;
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
		
	private static String generateNewUID(@Nullable String uid) {
		while (true) {
			if(!activeItems.containsKey(uid) && uid != null) {
				return uid;
			}
			uid = Util.generateUID(4); //1.6 million
		}
	}
	
	/*
	private static String generateNewUID() {
		return generateNewUID(null);
	} //*/
	
	/**
	 * Output Order:
	 * Description
	 * Stats
	 * Enhancements
	 * UID
	 * 
	 * @param item 	item to get lore from
	 * @return		an arraylist of segments of lore split by title
	 */
	private static ArrayList<List<String>> stripTitles(ItemStack item) {
		ArrayList<List<String>> out = new ArrayList<List<String>>();
		List<String> lore = item.getLore();
		int last = 0;
		for(int i = 0; i < lore.size(); i++) {
			if(lore.get(i).contains("===")) {
				out.add(lore.subList(last, i));
				last = i + 1;
			}
		}
		return out;
	}
	
	/**
	 * Deactivates a players inventory to save memory
	 * @param p
	 */
	public static void deactivate(Player p) {
		for(ItemStack item : p.getInventory().getContents()) {
			activeItems.remove(getUIDnoGen(item));
		}
	}
	
	public static ItemData getItemData(@Nullable ItemStack item, @Nonnull String uid) {
		if(activeItems.containsKey(uid)) {
			return activeItems.get(uid);
		}
		return getItemData(item);
	}
	
	public static ItemData getItemData(@Nullable ItemStack item) {
		String uid = getUIDnoGen(item);
		if(uid == null) {
			return null;
		}
		if(activeItems.containsKey(uid)) {
			return getItemData(item, getUIDnoGen(item));
		}
		ArrayList<List<String>> lore = stripTitles(item);
		
		ArrayList<Enhancement> enhancements = new ArrayList<Enhancement>();
		for(String line : stripTitles(item).get(2)) {
			Enhancement enh = Enhancement.getEnhancementFromName(ChatColor.stripColor(line).replace(" - ", ""));
			if(enh != null) {
				enhancements.add(enh);
			}
		}
		
		ItemData data = new ItemData(lore.get(0), enhancements, item);
		activeItems.put(uid, data);
		return data;
	}
	
	private static boolean isActive(@Nullable String uid) {
		if(uid == null) {
			return false;
		}
		return activeItems.containsKey(uid);
	}
	
	private static boolean isActive(@Nullable ItemStack item) {
		return isActive(getUIDnoGen(item));
	}
	
	//gets the UID of item
	public static String getUIDnoGen(@Nullable ItemStack item) {
		if(item == null || item.getLore() == null || item.getLore().size() < 3) {
			return null;
		}
		List<String> lore = item.getLore();
		return lore.get(lore.size() - 1).split("@")[1];
	}
	
	//gets the UID of item or generates a new one
	public static String getUID(@Nullable ItemStack item, @Nullable String preferredUID) {
		String uid = getUIDnoGen(item);
		if(uid == null) {
			return generateNewUID(preferredUID);
		}
		return uid;
	}
	
	public static String getUID(@Nullable ItemStack item) {
		return getUID(item, null);
	}
	
	public static ArrayList<Enhancement> getEnhancements(ItemStack item) {
		ItemData data = getItemData(item);
		if(data == null) {
			return new ArrayList<Enhancement>();
		}
		return data.getEnhancements();
	}
	
	//Forces item to use uid
	public static void updateLore(ItemStack item, String uid) {
		ItemData itemData = getItemData(item, uid);
		 
		ArrayList<String> lore = new ArrayList<String>();
		
		lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + itemData.getDesc());
		lore.add("");
		
		lore.add(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + " === STATS ===");
		lore.add(ChatColor.GRAY + " Attack: 5");
		lore.add("");
		
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
		item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	}
	
	//Updates lore with same uid
	public static void updateLore(ItemStack item) {
		String uid = getUID(item);
		updateLore(item, uid);
	}
	
	public static boolean changeUID(ItemStack item, String newUID) {
		if(isActive(newUID) || !isActive(item)) {
			return false;
		}
		String oldUID = getUIDnoGen(item);
		activeItems.put(newUID, getItemData(item));
		activeItems.remove(oldUID);
		updateLore(item, newUID);
		return true;
	}
}
