package com.pwnion.legacycraft.listeners;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.abilities.SkillTree;
import com.pwnion.legacycraft.quests.QuestManager;
import com.pwnion.legacycraft.quests.triggers.NearLocation;

public class PlayerQuit implements Listener {
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		
		SkillTree skillTree = (SkillTree) LegacyCraft.getPlayerData(playerUUID, PlayerData.SKILL_TREE);
		if(p.getGameMode().equals(GameMode.ADVENTURE)) {
			skillTree.saveClass();
		} else {
			skillTree.saveOther();
		}
		
		QuestManager.savePlayerData(p);
		
		//Remove the player data for the player that left the server
		LegacyCraft.removePlayerData(playerUUID);
	}
}
