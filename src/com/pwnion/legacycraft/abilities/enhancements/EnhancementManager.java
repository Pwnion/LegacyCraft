package com.pwnion.legacycraft.abilities.enhancements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.ConfigAccessor;
import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.quests.Quest;
import com.pwnion.legacycraft.quests.Trigger;
import com.pwnion.legacycraft.quests.TriggerType;

public class EnhancementManager {
	
	private static HashMap<ItemStack, ArrayList<Enhancement>> enhancements = new HashMap<ItemStack, ArrayList<Enhancement>>();
	
	public static void apply(Player wielder, ItemStack item, @Nullable Entity target, EnhancementType type) {
		if(hasEnhancements(item)) {
			for(Enhancement enhancement : enhancements.get(item)) {
				if(type == EnhancementType.WeaponHit) {
					apply(wielder, item, target, EnhancementType.WeaponSwing);
				}
				if(enhancement.getType() == type) {
					enhancement.apply(wielder, target);
				}
			}
		}
	}
	
	public static void addEnhancement(ItemStack item, Enhancement enh) {
		ArrayList<Enhancement> cur = getEnhancements(item);
		cur.add(enh);
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(" === ENHANCEMENTS === ");
		for(Enhancement e : cur) {
			lore.add(e.getName());
		}
		item.setLore(lore);
		
		setEnhancements(item, cur);
	}
	
	public static boolean hasEnhancements(ItemStack item) {
		return enhancements.containsKey(item);
	}
	
	public static ArrayList<Enhancement> getEnhancements(ItemStack item) {
		return enhancements.getOrDefault(item, new ArrayList<Enhancement>());
	}
	
	public static void setEnhancements(ItemStack item, ArrayList<Enhancement> enhs) {
		enhancements.put(item, enhs);
	}
	
	//To be used when an item with enhancements is changed in any way
	public static void transfer(ItemStack oldItemStack, ItemStack newItemStack) {
		if(hasEnhancements(oldItemStack)) {
			setEnhancements(newItemStack, getEnhancements(oldItemStack));
			enhancements.remove(oldItemStack);
		}
	}
}
