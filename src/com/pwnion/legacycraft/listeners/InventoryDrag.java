package com.pwnion.legacycraft.listeners;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.abilities.SkillTree;
import com.pwnion.legacycraft.abilities.SkillTree.PlayerClass;
import com.pwnion.legacycraft.quests.triggers.GetItem;

public class InventoryDrag implements Listener {

	@EventHandler
	private static void onInventoryDrag(InventoryDragEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) return;
		Player p = (Player) e.getWhoClicked();
		UUID playerUUID = p.getUniqueId();
		ItemStack oldCursor = e.getOldCursor();
		ItemStack newCursor = p.getItemOnCursor();
		SkillTree skillTree = PlayerData.getSkillTree(playerUUID);
		
		//Util.br(p.getName() + " has called onInventoryDrag");
		GetItem.updateItemQuests(p);
		
		if(!(p.getGameMode().equals(GameMode.ADVENTURE) && !skillTree.getPlayerClass().equals(PlayerClass.NONE))) return;
		
		e.setCancelled(true);
	}
}
