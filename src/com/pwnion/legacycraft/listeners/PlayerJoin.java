package com.pwnion.legacycraft.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.abilities.SkillTree;
import com.pwnion.legacycraft.abilities.SkillTree.PlayerClass;
import com.pwnion.legacycraft.levelling.Experience;
import com.pwnion.legacycraft.quests.QuestManager;

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
				put(PlayerData.UNFINISHED_QUESTS, QuestManager.loadUnfinishedPlayerData(p.getUniqueId()));
				put(PlayerData.FINISHED_QUESTS, QuestManager.loadFinishedPlayerData(p.getUniqueId()));
				put(PlayerData.SWAP_SLOT, -1);
				put(PlayerData.EXPERIENCE, new Experience(p));
			}
		});
	}
}