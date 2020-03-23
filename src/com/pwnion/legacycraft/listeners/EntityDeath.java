package com.pwnion.legacycraft.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.quests.triggers.KillEntity;

public class EntityDeath implements Listener {

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		Entity dead = e.getEntity();
		Player p = e.getEntity().getKiller();
		if(!p.equals(null)) {
			KillEntity.onPlayerKilledEntity(p, dead.getType());
		}
	}

}
