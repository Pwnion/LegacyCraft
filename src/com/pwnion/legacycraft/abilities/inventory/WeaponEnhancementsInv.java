package com.pwnion.legacycraft.abilities.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class WeaponEnhancementsInv extends Inv {
	//Loads the 'Weapon Enhancements' inventory for the player
	public static void load(Player p) {
		p.openInventory(InventoryFromFile.get(InvName.WEAPON_ENHANCEMENTS, FILE));
	}
	
	//Responds to a player clicking an item in the 'Weapon Enhancements' inventory
	public static void respond(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		int clickedSlot = e.getRawSlot();
		
		switch(clickedSlot) {
		case 0:
			CharacterBuildMenuInv.load(p);
			break;
		}
	} 
}
