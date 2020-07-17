package com.pwnion.legacycraft.items.enhancements;

import javax.annotation.Nullable;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.items.ItemManager;

/**
 * Enhancements allow running code when:
 * 	Player swings item 				(EnhancementType.WEAPON_SWING)
 *  Player hits entity with item	(EnhancementType.WEAPON_HIT)
 *  Player equips enhancement		(onEquip(), initial = true)
 *  Player removes enhancement		(onRemove())
 *  Player joins server with enhanced item in inventory (onEquip(), initial = false)
 *  
 *  This allows storing of data in memory (but not files)
 * 
 * @author Zephreo
 *
 */
public interface Enhancement {
	
	public static Enhancement fromName(String name) {
		
		try {
			return (Enhancement) Class.forName("com.pwnion.legacycraft.items.enhancements.effects." + name.replace(" ", "")).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			//ERROR: No enhancement found
			//Log error and warn player and admin
			
			//Maybe add a placeholder enhancement adder with the errored name to the players inventory
			//Called from ItemManager.activate()
			//Which is called from PlayerJoin which has player
			Util.br("Name: \"" + name + "\"");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Sends onHit (unofficial) events to each enhancement on the item
	 * 
	 * @param wielder
	 * @param target
	 * @param item
	 * @param damage
	 */
	public static void applyHit(LivingEntity wielder, LivingEntity target, ItemStack item, double damage) {
		for(Enhancement enhancement : ItemManager.getEnhancements(item)) {
			enhancement.onHit(wielder, target, damage);
		}
	}
	
	/**
	 * Sends onSwing (unofficial) events to each enhancement on the item
	 * 
	 * @param wielder
	 * @param item
	 */
	public static void applySwing(LivingEntity wielder, ItemStack item) {
		for(Enhancement enhancement : ItemManager.getEnhancements(item)) {
			enhancement.onSwing(wielder);
		}
	}
	
	/**
	 * IMPORTANT: Names must be their class name or name with spaces added otherwise an exception must be added to Enhancement.fromName()
	 * by default returns Class Name (Simple)
	 * 
	 * @return	name
	 */
	public default String getName() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * Calls whenever the weapon hits an enemy
	 * 
	 * @param wielder	Who is holding this weapon
	 * @param target	Who got hit
	 * @param damage	Damage dealt to enemy
	 */
	public default void onHit(LivingEntity wielder, LivingEntity target, double damage) {}
	
	/**
	 * Calls whenever the weapon is swung/left-clicked
	 * 
	 * @param wielder	Who is holding this weapon
	 */
	public default void onSwing(LivingEntity wielder) {}
	
	/**
	 * Called when the enhancement is equipped and when the item is activated 
	 * 
	 * @param item
	 * @param initial	False when reactivating item e.g after restart/relog
	 */
	public default void onEquip(ItemStack item, boolean initial) {};
	
	/**
	 * Called when removing an enhancement from an item
	 * Should be called only on an active item
	 * 
	 * @param item
	 */
	public default void onRemove(ItemStack item) {};
	
}
