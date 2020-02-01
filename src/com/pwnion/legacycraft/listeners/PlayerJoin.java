package com.pwnion.legacycraft.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.abilities.SkillTree;
import com.pwnion.legacycraft.abilities.SkillTree.PlayerClass;

public class PlayerJoin implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		
		//Initialise variables and populate with default values to help track the player
		LegacyCraft.setPlayerData(playerUUID, new HashMap<PlayerData, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put(PlayerData.SKILL_TREE, new SkillTree(p));
				put(PlayerData.JUMP_COUNTER, 0);
				put(PlayerData.FALL_DISTANCE, 0f);
				put(PlayerData.CLASS_INVENTORY_OPEN, PlayerClass.NONE);
				put(PlayerData.ASPECT_INVENTORY_OPEN, SkillTree.Aspect.NONE);
				put(PlayerData.ADVENTURE_MODE, p.getGameMode().equals(GameMode.ADVENTURE) ? true : false);
				put(PlayerData.UNDER_NO_FALL_DAMAGE_LIMIT, false);
			}
		});
	}
}