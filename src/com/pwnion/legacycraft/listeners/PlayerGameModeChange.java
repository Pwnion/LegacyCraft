package com.pwnion.legacycraft.listeners;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.abilities.SkillTree;
import com.pwnion.legacycraft.abilities.SkillTree.PlayerClass;

import net.md_5.bungee.api.ChatColor;

public class PlayerGameModeChange implements Listener {
	
	@EventHandler
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		GameMode oldGameMode = p.getGameMode();
		GameMode newGameMode = e.getNewGameMode();
		
		SkillTree skillTree = PlayerData.getSkillTree(playerUUID);
		
		//Handles switching between adventure mode and other game modes
		if(!oldGameMode.equals(GameMode.ADVENTURE) && newGameMode.equals(GameMode.ADVENTURE)) {
			skillTree.saveOther();
			
			if(!skillTree.getPlayerClass().equals(PlayerClass.NONE)) {
				skillTree.loadClass(skillTree.getPlayerClass());
			}
			
			p.sendMessage(ChatColor.GREEN + "Entered " + ChatColor.GOLD + "Legacy" + ChatColor.WHITE + "Craft " + ChatColor.GREEN + "Player Mode!");
		} else if(oldGameMode.equals(GameMode.ADVENTURE)) {
			if(!skillTree.getPlayerClass().equals(PlayerClass.NONE)) {
				skillTree.saveClass();
				skillTree.loadOther();
			}
			
			p.sendMessage(ChatColor.RED + "Exited " + ChatColor.GOLD + "Legacy" + ChatColor.WHITE + "Craft " + ChatColor.RED + "Player Mode!");
		}
	}
}