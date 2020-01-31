package com.pwnion.legacycraft.abilities.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CharacterBuildMenuInv extends Inv {
	//Loads the 'Character Build Menu' inventory for a player
	public static void load(Player p) {
		p.openInventory(InventoryFromFile.get(InvName.CHARACTER_BUILD_MENU, FILE));
	}
	
	//Responds to a player clicking an item in the 'Character Build Menu' inventory
	public static void respond(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		int clickedSlot = e.getRawSlot();
		
		switch(clickedSlot) {
		case 1:
			SelectAClassInv.load(p);
			break;
		case 3:
			WeaponEnhancementsInv.load(p);
			break;
		}
	}

}
