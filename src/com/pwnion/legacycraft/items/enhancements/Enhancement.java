package com.pwnion.legacycraft.items.enhancements;

import javax.annotation.Nullable;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.items.ItemManager;

public interface Enhancement {
	
	public static Enhancement getEnhancementFromName(String name) {
		try {
			return (Enhancement) Class.forName("com.pwnion.legacycraft.items.enhancements.effects." + name.replace(" ", "")).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			//ERROR: No enhancement found
			//Log error and warn player and admin
			
			//Maybe add a placeholder enhancement adder with the errored name to the players inventory
			//Called from LoreManager.getItemData() which has player
			e.printStackTrace();
		}
		return null;
	}
	
	public static void apply(Player wielder, @Nullable Damageable target, @Nullable ItemStack item, double damage, EnhancementType type) {
		for(Enhancement enhancement : ItemManager.getItemData(item).getEnhancements()) {
			if(type == EnhancementType.WEAPON_HIT) {
				apply(wielder, target, item, damage, EnhancementType.WEAPON_SWING);
			}
			if(enhancement.getType() == type) {
				enhancement.apply(wielder, target, damage);
			}
		}
	}
	
	//IMPORTANT: Names must be their class name or name with spaces added otherwise an exception must be added to EnhancementManager.getEnhancementFromName()
	public default String getName() {
		return this.getName();
	}

	public EnhancementType getType();
	
	public void apply(Entity wielder, @Nullable Damageable target, double damage);
	
}
