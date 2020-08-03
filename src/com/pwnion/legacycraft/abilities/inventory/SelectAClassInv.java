package com.pwnion.legacycraft.abilities.inventory;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.abilities.SkillTree;
import com.pwnion.legacycraft.abilities.SkillTree.Jump;
import com.pwnion.legacycraft.abilities.SkillTree.PlayerClass;

public class SelectAClassInv extends Inv {
	//Loads the 'Select A Class' inventory for a player
	public static void load(Player p) {
		InventoryView inv = p.openInventory(DeserialiseInventory.get(InvName.SELECT_A_CLASS));
		SkillTree skillTree = PlayerData.getSkillTree(p.getUniqueId());
		
    	if(!skillTree.getPlayerClass().equals(PlayerClass.NONE)) {
			ItemMeta itemMeta = inv.getItem(classToSlot.get(skillTree.getPlayerClass())).getItemMeta();
			itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
			itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			
			inv.getItem(classToSlot.get(skillTree.getPlayerClass())).setItemMeta(itemMeta);
			
			inv.getItem(classToSlot.get(skillTree.getPlayerClass()) - 9).setType(Material.WHITE_STAINED_GLASS_PANE);
			inv.getItem(classToSlot.get(skillTree.getPlayerClass()) + 9).setType(Material.WHITE_STAINED_GLASS_PANE);
    	}
	}
	
	//Responds to a player clicking an item in the 'Select A Class' inventory
	public static void respond(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		Player p = (Player) e.getWhoClicked();
		UUID playerUUID = p.getUniqueId();
		ItemStack clickedItem = e.getCurrentItem();
		ClickType clickType = e.getClick();
		int clickedSlot = e.getRawSlot();
		
		switch(clickedSlot) {
		case 9:
			CharacterBuildMenuInv.load(p);
			break;
		case 11:
		case 13:
		case 15:
		case 17:
			SkillTree skillTree = PlayerData.getSkillTree(playerUUID);
			PlayerClass clickedClass = PlayerClass.valueOf(clickedItem.getItemMeta().getDisplayName().toUpperCase());
			
			if(clickType.isLeftClick()) {
				if(skillTree.getPlayerClass().equals(clickedClass)) return;
				
				p.closeInventory();
				
				skillTree.saveClass();
				skillTree.setPlayerClass(clickedClass);
				skillTree.loadClass(clickedClass);
				
				Jump jump = skillTree.getEquippedJump(clickedClass);
				if(!jump.equals(Jump.NONE)) {
					skillTree.setEquippedJump(clickedClass, jump);
				}
				
				load(p);
			} else if(clickType.isRightClick()) {
				PlayerData.setClassInventoryOpen(playerUUID, clickedClass);
				
				SelectAnAspectInv.load(p);
			}
			click(p);
			
			break;
		}
	}
}
