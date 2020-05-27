package com.pwnion.legacycraft.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.levelling.Experience;
import com.pwnion.legacycraft.levelling.ExperienceType;
import com.pwnion.legacycraft.quests.triggers.KillEntity;

public class EntityDeath implements Listener {

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		try {
			Entity victim = e.getEntity();
			Player p = e.getEntity().getKiller();
			if(p != null) { //If killer is player
				
				KillEntity.onPlayerKilledEntity(p, victim.getType());	//Quest integration
				
				Experience playerExperience = (Experience) LegacyCraft.getPlayerData(p.getUniqueId(), PlayerData.EXPERIENCE);
				
				playerExperience.addExperience(100, ExperienceType.PLAYER);
				
			}
		} catch(Exception ex) {
			Util.print(ex);
		}
	}

}
