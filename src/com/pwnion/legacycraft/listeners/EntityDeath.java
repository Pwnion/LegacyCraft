package com.pwnion.legacycraft.listeners;

import java.util.HashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.levelling.Experience;
import com.pwnion.legacycraft.levelling.ExperienceType;
import com.pwnion.legacycraft.quests.triggers.KillEntity;

public class EntityDeath implements Listener {
	
	//Simple example experience system
	@SuppressWarnings("unused")
	private static final HashMap<EntityType, Integer> EntityWorth = new HashMap<EntityType, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put(EntityType.ZOMBIE, 100);
		}
	};

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		try {
			Entity victim = e.getEntity(); //The killed entity
			
			// === MODIFY DROPS ===
			e.setDroppedExp(0); //Mobs don't drop vanilla exp
			
			
			//=== PLAYER KILLS MOB EVENTS ===
			Player p = e.getEntity().getKiller(); //Null if player did not kill entity
			if(p != null) { //If killer is player
				
				KillEntity.onPlayerKilledEntity(p, victim.getType());	//Quest integration
				
				//Experience integration
				Experience playerExperience = PlayerData.getExperience(p.getUniqueId());
				
				playerExperience.addExperience(100, ExperienceType.PLAYER);
				
			}
		} catch(Exception ex) {
			Util.print(ex);
		}
	}

}
