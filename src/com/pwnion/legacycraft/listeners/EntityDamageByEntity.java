package com.pwnion.legacycraft.listeners;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.abilities.enhancements.EnhancementManager;
import com.pwnion.legacycraft.abilities.enhancements.EnhancementType;

public class EntityDamageByEntity implements Listener {
	

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		Entity attacked = e.getEntity();
		Entity attacker = e.getDamager();
		
		if(attacker instanceof Player) {
			Player p = (Player) attacker;
			ItemStack item = p.getInventory().getItemInMainHand();
			
			double damage = e.getFinalDamage();
			if(damage > 0) {
				EnhancementManager.apply(p, (Damageable) attacked, item, damage, EnhancementType.WeaponHit);
			} else {
				EnhancementManager.apply(p, null, item, 0, EnhancementType.WeaponSwing);
			}
		}
	}
}
