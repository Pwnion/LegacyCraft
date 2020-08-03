package com.pwnion.legacycraft.items.enhancements;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.Util;
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
 *  This allows storing of data in memory (but not files)
 * 
 * @author Zephreo
 *
 */
public interface Enhancement {
	
	/**
	 * Multi-word enhancement names must have spaces <p>
	 * 
	 * Example Effect is ok, <br>
	 * but ExampleEffect is not, <br>
	 * EXAMPLE EFFECT is ok, <p>
	 * 
	 * leading/trailing spaces are ignored
	 * 
	 * @param name	Name of enhancement
	 * @return 		New instance of respective enhancement class
	 */
	public static Enhancement fromName(String name) {
		
		String input = name;
		name = Util.toTitleCase(name).replace(" ", "").replace("_", "");
		
		/* Add name here (in title case) if name is not class name (ignoring spaces)
		switch(name) {
		case "ExampleEff":
			return new ExampleEffect();
			break;
		} //*/
		
		try {
			return (Enhancement) Class.forName("com.pwnion.legacycraft.items.enhancements.effects." + name).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			//ERROR: No enhancement found
			//Log error and warn player and admin
			
			//Maybe add a placeholder enhancement adder with the errored name to the players inventory
			//Called from ItemManager.activate()
			//Which is called from PlayerJoin which has player
			Util.br("Input: \"" + input + "\"");
			Util.br("Name: \"" + name + "\"");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * IMPORTANT: Names must be their class name or name with spaces added otherwise an exception must be added to Enhancement.fromName() <br>
	 * by default returns Class Name (Simple)
	 * 
	 * @return	name
	 */
	public default String getName() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * What items can equip this enhancement.
	 * 
	 * @return	The type of items that can equip
	 */
	public ItemType getRestriction();
	
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
	 * Called when removing an enhancement from an item <br>
	 * Should be called only on an active item
	 * 
	 * @param item
	 */
	public default void onRemove(ItemStack item) {};
	
}
