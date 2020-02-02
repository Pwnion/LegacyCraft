package com.pwnion.legacycraft.listeners;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.abilities.SkillTree;
import com.pwnion.legacycraft.abilities.SkillTree.PlayerClass;

public class PlayerGameModeChange implements Listener {
	
	@EventHandler
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		GameMode oldGameMode = p.getGameMode();
		GameMode newGameMode = e.getNewGameMode();
		
		SkillTree skillTree = (SkillTree) LegacyCraft.getPlayerData(playerUUID, PlayerData.SKILL_TREE);
		
		//Handles switching between adventure mode and other game modes
		if(!oldGameMode.equals(GameMode.ADVENTURE) && newGameMode.equals(GameMode.ADVENTURE)) {
			skillTree.saveOther();
			
			if(!skillTree.getPlayerClass().equals(PlayerClass.NONE)) {
				skillTree.loadClass(skillTree.getPlayerClass());
			}
			
		} else if(oldGameMode.equals(GameMode.ADVENTURE)) {
			if(!skillTree.getPlayerClass().equals(PlayerClass.NONE)) {
				skillTree.saveClass();
				skillTree.loadOther();
			}
		}
	}
}