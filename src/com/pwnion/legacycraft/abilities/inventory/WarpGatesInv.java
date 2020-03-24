package com.pwnion.legacycraft.abilities.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.pwnion.legacycraft.abilities.ooc.Portal;

public class WarpGatesInv extends Inv {
	public static void load(Player p) {
		p.openInventory(DeserialiseInventory.get(InvName.WARP_GATES));
		click(p);
	}
	
	public static void respond(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		int customModelData = e.getCurrentItem().getItemMeta().getCustomModelData();
		
		Portal.values()[customModelData - 2].activate(p);
		
		p.closeInventory();
	}
}
