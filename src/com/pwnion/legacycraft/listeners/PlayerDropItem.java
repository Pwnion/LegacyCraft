package com.pwnion.legacycraft.listeners;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.abilities.SkillTree;
import com.pwnion.legacycraft.abilities.SkillTree.PlayerClass;

public class PlayerDropItem implements Listener {
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		SkillTree skillTree = PlayerData.getSkillTree(playerUUID);
		
		if(!(p.getGameMode().equals(GameMode.ADVENTURE) && !skillTree.getPlayerClass().equals(PlayerClass.NONE))) return;
		
		e.setCancelled(true);
		
		//Cast ultimate
	}
}