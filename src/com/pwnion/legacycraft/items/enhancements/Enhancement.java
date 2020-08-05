package com.pwnion.legacycraft.items.enhancements;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.items.ItemManager;
import com.pwnion.legacycraft.items.ItemType;

/**
 * Enhancements allow running code when: <br>
 * 	Player swings item 				(onSwing())	<br>	
 *  Player hits entity with item	(onHit()) <br>
 *  Player equips enhancement		(onEquip(), initial = true) <br>
 *  Player removes enhancement		(onRemove()) <br>
 *  Player joins server with enhanced item in inventory (onEquip(), initial = false) <br>
 *  <br>
 *  This allows storing of data in memory (but not files) <p>
 *  
 *  Enhancements are effectively static as only one instance should be stored at once.
 *  To keep track of item specific values use {@code ItemManager.getUID(item)}
 * 
 * @author Zephreo
 *
 */
public interface Enhancement {
	
	static final HashMap<String, Enhancement> registeredEnhancements = new HashMap<String, Enhancement>();
	
	public static void register(Enhancement... enhancements) {
		for(Enhancement enh : enhancements) {
			Enhancement overridden = registeredEnhancements.put(enh.getName().toLowerCase(), enh);
			if(overridden != null) {
				Bukkit.getLogger().warning("Enhancement '" + overridden.getName() + "' was overidden by another enhancement of the same name!");
			}
		}
	}
	
	/**
	 * Gets the enhancement with the same name or null. <br>
	 * Ignores Case, Underscores "_" treated as spaces.
	 * 
	 * @param name	Name of enhancement
	 * @return 		The enhancement classes instance
	 */
	public static Enhancement fromName(String name) {
		
		String input = name;
		name = name.replace("_", " ").toLowerCase().trim();
		
		return registeredEnhancements.get(name);
		
		/*
		//ERROR: No enhancement found
		//Log error and warn player and admin
		
		//Maybe add a placeholder enhancement adder with the errored name to the players inventory
		//Called from ItemManager.activate()
		//Which is called from PlayerJoin which has player
		Util.br("Input: \"" + input + "\"");
		Util.br("Name: \"" + name + "\"");
		
		return null; //*/
	}
	
	public static Set<String> allNames() {
		return registeredEnhancements.keySet();
	}

	/**
	 * by default returns Class Name (Simple) with spaces before capital letters and numbers
	 * 
	 * @return	name
	 */
	public default String getName() {
		return this.getClass().getSimpleName().replaceAll("(.)([A-Z0-9]\\w)", "$1 $2");
	}
	
	/**
	 * What items can equip this enhancement.
	 * 
	 * @return	The type of items that can equip
	 */
	public ItemType getRestriction();
	
	/**
	 * Sends onHit (unofficial) events to each enhancement on the item
	 * @param item
	 * @param wielder
	 * @param target
	 * @param damage
	 */
	public static void applyHit(ItemStack item, LivingEntity wielder, LivingEntity target, double damage) {
		for(Enhancement enhancement : ItemManager.getEnhancements(item)) {
			enhancement.onHit(item, wielder, target, damage);
		}
	}
	
	/**
	 * Sends onSwing (unofficial) events to each enhancement on the item
	 * @param item
	 * @param wielder
	 */
	public static void applySwing(ItemStack item, LivingEntity wielder) {
		for(Enhancement enhancement : ItemManager.getEnhancements(item)) {
			enhancement.onSwing(item, wielder);
		}
	}
	
	/**
	 * Calls whenever the weapon hits an enemy
	 * @param item 		The item used to hit
	 * @param wielder	Who is holding this weapon
	 * @param target	Who got hit
	 * @param damage	Damage dealt to enemy
	 */
	public default void onHit(ItemStack item, LivingEntity wielder, LivingEntity target, double damage) {}
	
	/**
	 * Calls whenever the weapon is swung/left-clicked
	 * @param item 		The item swung
	 * @param wielder	Who is holding this weapon
	 */
	public default void onSwing(ItemStack item, LivingEntity wielder) {}
	
	/**
	 * Called when the enhancement is equipped and when the item is activated 
	 * 
	 * @param item
	 * @param initial	False when reactivating item e.g after restart/relog
	 */
	public default void onEquip(ItemStack item, boolean initial) {};
	
	/**
	 * Called when removing an enhancement from an item <br>
	 * Should be called only on an active item
	 * 
	 * @param item
	 */
	public default void onRemove(ItemStack item) {};
	
}
