package com.pwnion.legacycraft.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.pwnion.legacycraft.abilities.Enhancment;

public class EntityDamageByEntity {
	
	//Simple
	
	HashMap<Entity, ArrayList<Enhancment>> activeEnhancments = new HashMap<Entity, ArrayList<Enhancment>>();

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		Entity attacked = e.getEntity();
		Entity attacker = e.getDamager();
		
		if(activeEnhancments.containsKey(attacker)) {
			e.setDamage(10);
		}
	}
}
