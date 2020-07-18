package com.pwnion.legacycraft.abilities.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.items.ItemData;
import com.pwnion.legacycraft.items.ItemManager;

public class WeaponEnhancementsInv extends Inv {
	//Loads the 'Weapon Enhancements' inventory for the player
	public static void load(Player p) {
		p.openInventory(DeserialiseInventory.get(InvName.WEAPON_ENHANCEMENTS));
	}
	
	//Responds to a player clicking an item in the 'Weapon Enhancements' inventory
	public static void respond(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		int clickedSlot = e.getRawSlot();
		ItemStack item = e.getCurrentItem();
		ItemData itemData = ItemManager.getItemData(item);
		
		Util.br(clickedSlot);
		
		if(itemData != null) {
			//Open item inv
		}
		
		switch(clickedSlot) {
		case 0:
			CharacterBuildMenuInv.load(p);
			click(p);
			break;
		}
	} 
}
