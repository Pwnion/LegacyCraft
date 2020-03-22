package com.pwnion.legacycraft.abilities.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BlacksmithInv extends Inv {
	//Loads the 'Character Build Menu' inventory for a player
	public static void load(Player p) {
		p.openInventory(DeserialiseInventory.get(InvName.BLACKSMITH));
		click(p);
	}
	
	//Responds to a player clicking an item in the 'Character Build Menu' inventory
	public static void respond(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		int clickedSlot = e.getRawSlot();
	}
}
