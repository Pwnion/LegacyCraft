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
import com.pwnion.legacycraft.items.ItemManager;
import com.pwnion.legacycraft.levelling.Experience;
import com.pwnion.legacycraft.quests.QuestManager;

public class PlayerQuit implements Listener {
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		
		SkillTree skillTree = PlayerData.getSkillTree(playerUUID);
		if(p.getGameMode().equals(GameMode.ADVENTURE)) {
			skillTree.saveClass();
		} else {
			skillTree.saveOther();
		}
		
		Experience experience = PlayerData.getExperience(playerUUID);
		experience.save();
		
		QuestManager.savePlayerData(p);
		
		ItemManager.deactivate(p);
		
		//Remove the player data for the player that left the server
		PlayerData.remove(playerUUID);
	}
}
