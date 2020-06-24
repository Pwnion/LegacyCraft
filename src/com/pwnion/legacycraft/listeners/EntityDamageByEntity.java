package com.pwnion.legacycraft.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.abilities.enhancements.Enhancement;
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
			
			if(e.getFinalDamage() > 0) {
				EnhancementManager.apply(p, item, attacked, EnhancementType.WeaponHit);
			} else {
				EnhancementManager.apply(p, item, null, EnhancementType.WeaponSwing);
			}
		}
	}
}
