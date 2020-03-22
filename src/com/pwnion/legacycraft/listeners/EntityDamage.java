package com.pwnion.legacycraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
		if(e.getEntityType().equals(EntityType.PLAYER) && e.getCause().equals(DamageCause.FALL)) {
			Player p = (Player) entity;
			
			//Divides normal fall damage by 4
			e.setDamage(e.getDamage() / 4);
			
			//Schedules flight to be enabled one tick after taking fall damage
			if(p.getGameMode().equals(GameMode.ADVENTURE)) {
				Bukkit.getScheduler().runTask(LegacyCraft.getPlugin(), new Runnable() {

		            @Override
		            public void run() {
		                p.setAllowFlight(true);
		            }
		        });
			}
			return;
		}
	}
}