package com.pwnion.legacycraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.pwnion.legacycraft.LegacyCraft;

public class EntityDamage implements Listener {
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		Entity entity = e.getEntity();
		if(e.getEntityType() == EntityType.PLAYER && e.getCause() == DamageCause.FALL) {
			Player p = (Player) entity;
			
			//Halves normal fall damage
			e.setDamage((p.getFallDistance() / 4f - 1.5f));
			
			//Schedules flight to be enabled one tick after taking fall damage
			Bukkit.getScheduler().runTask(LegacyCraft.getPlugin(), new Runnable() {

	            @Override
	            public void run() {
	                p.setAllowFlight(true);
	            }
	        });
		}	
	}
}