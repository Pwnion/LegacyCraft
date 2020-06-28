package com.pwnion.legacycraft.items.enhancements;

import javax.annotation.Nullable;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.items.ItemData;
import com.pwnion.legacycraft.items.ItemManager;

public interface Enhancement {
	
	public static Enhancement fromName(String name) {
		try {
			return (Enhancement) Class.forName("com.pwnion.legacycraft.items.enhancements.effects." + name.replace(" ", "")).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			//ERROR: No enhancement found
			//Log error and warn player and admin
			
			//Maybe add a placeholder enhancement adder with the errored name to the players inventory
			//Called from ItemManager.getItemData() which has player
			Util.br("Name: \"" + name + "\"");
			e.printStackTrace();
		}
		return null;
	}
	
	public static void apply(LivingEntity wielder, @Nullable LivingEntity target, @Nullable ItemStack item, double damage, EnhancementType type) {
		for(Enhancement enhancement : ItemManager.getEnhancements(item)) {
			if(enhancement.getType() == type) {
				enhancement.apply(wielder, target, damage);
			}
		}
	}
	
	//IMPORTANT: Names must be their class name or name with spaces added otherwise an exception must be added to Enhancement.fromName()
	public default String getName() {
		return this.getClass().getSimpleName();
	}

	public EnhancementType getType();
	
	public void apply(LivingEntity wielder, @Nullable LivingEntity target, double damage);
	
	/**
	 * 
	 * @param item
	 * @param initial	False when reactivating item e.g after restart/relog
	 */
	public void onEquip(ItemStack item, boolean initial);
	
}
