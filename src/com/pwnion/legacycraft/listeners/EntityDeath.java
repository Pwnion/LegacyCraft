package com.pwnion.legacycraft.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.pwnion.legacycraft.quests.triggers.KillEntity;

public class EntityDeath implements Listener {

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		Entity victim = e.getEntity();
		Player killer = e.getEntity().getKiller();
		if(killer != null) {
			KillEntity.onPlayerKilledEntity(killer, victim.getType());
		}
	}

}
