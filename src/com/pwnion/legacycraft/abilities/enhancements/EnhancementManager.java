package com.pwnion.legacycraft.abilities.enhancements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nullable;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.abilities.enhancements.effects.Puncture;

import net.md_5.bungee.api.ChatColor;

public class EnhancementManager {
	
	//Where uid is Unique identifier
	//Not uuid as it is not universally unique
	
	//uid sizes can change with no effect to old uids
	
	//Stores the 'active' enhancements in memory
	//which allows them to be used by a player
	//HashMap of UID to Enhancement
	private static HashMap<String, ArrayList<Enhancement>> enhancements = new HashMap<String, ArrayList<Enhancement>>();
	
	private static String generateNewUID() {
	    while (true) {
	        String uid = Util.generateUID(4); //1.6 million
	        if(!enhancements.containsKey(uid)) {
	        	return uid;
	        }
	    }
	}
	
	private static String getUID(@Nullable ItemStack item) {
		if(item == null || item.getLore() == null || item.getLore().size() < 3) {
			return null;
		}
		List<String> lore = item.getLore();
		return lore.get(lore.size() - 1).split("@")[1];
	}
	
	//Unflattens enhancement lore into memory
	//Will throw errors if server crashes without saving
	public static void loadEnhancements(Player p) {
		for(ItemStack item : p.getInventory().getContents()) {
			if(item != null && item.getLore() != null && item.getLore().size() > 1) {
				List<String> lore = item.getLore();
				int count = 0;
				while(!lore.get(count).contains("@")) count++;
				
				try {
					String uid = lore.get(count).replaceFirst("@", ""); //Items will prefer to keep old uids
					String[] enhNames = lore.get(count + 1).split("|"); //May throw out of bounds during testing
					
					for(String enhName : enhNames) {
						Enhancement enh = getEnhancementFromName(enhName);
						if(enh == null) {
							//ERROR: No enhancement found
							//Log error and warn player and admin
							
							//Maybe add a placeholder enhancement adder with the errored name to the players inventory
							continue;
						}
						addEnhancement(enh, item, uid);
					}
				} catch (Exception e) {
					//Wrong format
					//Most likely server crash where saveEnhancements was not called
					Util.print(e);
				}
				
			}
		}
	}
	
	//Flattens all enhancement lore for machine readability
	public static void saveEnhancements(Player p) {
		for(ItemStack item : p.getInventory().getContents()) {
			String uid = getUID(item);
			if(uid != null) {
				ArrayList<Enhancement> enhs = getEnhancements(uid);
				ArrayList<String> lore = new ArrayList<String>();
				lore.add("@" + uid);
				String str = "";
				for(Enhancement e : enhs) {
					str += "|" + e.getName();
				}
				lore.add(str.replaceFirst("|", ""));
				item.setLore(lore);
			}
		}
	}
	
	public static void apply(Player wielder, @Nullable Damageable target, ItemStack item, double damage, EnhancementType type) {
		if(hasEnhancements(item)) {
			for(Enhancement enhancement : getEnhancements(item)) {
				if(type == EnhancementType.WeaponHit) {
					apply(wielder, target, item, damage, EnhancementType.WeaponSwing);
				}
				if(enhancement.getType() == type) {
					enhancement.apply(wielder, target, damage);
				}
			}
		}
	}
	
	private static void updateLore(ItemStack item, String uid, ArrayList<Enhancement> enhs) {
		ArrayList<String> lore = new ArrayList<String>();
		if(hasEnhancements(uid)) {
			lore.add(ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + " === ENHANCEMENTS === ");
			for(Enhancement e : enhs) {
				lore.add(ChatColor.GRAY + " - " + e.getName());
			}
			lore.add(ChatColor.DARK_GRAY + "@" + uid);
		}
		item.setLore(lore);
	}
	
	//Forces item to have given uid
	private static void updateLore(ItemStack item, String uid) {
		updateLore(item, uid, getEnhancements(uid));
	}
	
	//Updates lore if any enhancements have changed
	private static void updateLore(ItemStack item) {
		String uid = getUID(item);
		updateLore(item, uid);
	}
	
	//Adds an enhancement with a preferred uid
	//Allows adding non-conventional uids
	public static boolean addEnhancement(Enhancement enh, ItemStack item, @Nullable String uid, boolean force) {
		boolean newID = false;
		
		if(uid == null || (enhancements.containsKey(uid) && !hasEnhancements(item) && !force)) {
			if(force) {
				
			}
			uid = generateNewUID();
			newID = true;
		}
		
		ArrayList<Enhancement> cur = getEnhancements(uid);
		
		cur.add(enh);
		
		updateLore(item, uid, cur);
		
		setEnhancements(uid, cur);
		
		Util.br("Added " + enh.getName() + " to " + item);
		
		return newID;
	}
	
	public static boolean addEnhancement(Enhancement enh, ItemStack item, @Nullable String uid) {
		return addEnhancement(enh, item, uid, false);
	}
	
	public static void addEnhancement(Enhancement enh, ItemStack item) {
		addEnhancement(enh, item, getUID(item), false);
	}
	
	public static boolean hasEnhancements(String uid) {
		if(uid == null) {
			return false;
		}
		return enhancements.containsKey(uid);
	}
	
	public static boolean hasEnhancements(ItemStack item) {
		return hasEnhancements(getUID(item));
	}
	
	public static ArrayList<Enhancement> getEnhancements(String uid) {
		return enhancements.getOrDefault(uid, new ArrayList<Enhancement>());
	}
	
	public static ArrayList<Enhancement> getEnhancements(ItemStack item) {
		return getEnhancements(getUID(item));
	}
	
	public static void setEnhancements(String uid, ArrayList<Enhancement> enhs) {
		enhancements.put(uid, enhs);
	}
	
	public static void setEnhancements(ItemStack item, ArrayList<Enhancement> enhs) {
		setEnhancements(getUID(item), enhs);
	}
	
	public static void removeEnhancement(String uid, Enhancement enh) {
		ArrayList<Enhancement> cur = getEnhancements(uid);
		cur.remove(enh);
		if(cur.isEmpty()) {
			enhancements.remove(uid);
		} else {
			setEnhancements(uid, cur);
		}
	}
	
	public static void removeEnhancements(String uid, ArrayList<Enhancement> enhs) {
		for(Enhancement enh : enhs) {
			removeEnhancement(uid, enh);
		}
	}
	
	public static void removeEnhancements(String uid) {
		enhancements.remove(uid);
	}
	
	private static Enhancement getEnhancementFromName(String name) {
		switch(name) {
		case "Puncture":
			return new Puncture();
		}
		return null;
	}
	
	public static boolean changeUID(ItemStack item, String oldUID, String newUID) {
		if(hasEnhancements(newUID) || !hasEnhancements(oldUID)) {
			return false;
		}
		setEnhancements(newUID, getEnhancements(oldUID));
		removeEnhancements(oldUID);
		updateLore(item, newUID);
		return true;
	}
	
	public static boolean changeUID(ItemStack item, String newUID) {
		return changeUID(item, getUID(item), newUID);
	}
}
