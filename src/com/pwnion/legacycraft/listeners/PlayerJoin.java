package com.pwnion.legacycraft.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.abilities.SkillTree;
import com.pwnion.legacycraft.abilities.SkillTree.PlayerClass;
import com.pwnion.legacycraft.items.ItemManager;
import com.pwnion.legacycraft.levelling.Experience;
import com.pwnion.legacycraft.quests.QuestManager;

public class PlayerJoin implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		
		//Initialise variables and populate with default values to help track the player
		PlayerData.generate(p);
		
		//Activates all item abilities in inventory
		for(ItemStack item : p.getInventory().getContents()) {
			ItemManager.activate(item);
		}
		
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, (float) 1.5, 1);
	}
}