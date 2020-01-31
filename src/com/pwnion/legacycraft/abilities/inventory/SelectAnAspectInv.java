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

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.abilities.SkillTree;
import com.pwnion.legacycraft.abilities.SkillTree.Aspect;
import com.pwnion.legacycraft.abilities.SkillTree.PlayerClass;

public class SelectAnAspectInv extends Inv {
	//Loads the 'Select An Aspect' inventory for a player
	public static void load(Player p) {
		InventoryView inv = p.openInventory(InventoryFromFile.get(InvName.SELECT_AN_ASPECT, FILE));
		UUID playerUUID = p.getUniqueId();
		SkillTree skillTree = (SkillTree) LegacyCraft.getPlayerData(playerUUID, PlayerData.SKILL_TREE);
		PlayerClass openedClass = (PlayerClass) LegacyCraft.getPlayerData(playerUUID, PlayerData.CLASS_INVENTORY_OPEN);
		Aspect aspect = skillTree.getAspect(openedClass);
		
		if(!aspect.equals(SkillTree.Aspect.NONE)) {
			ItemStack item = inv.getItem(aspectToSlot.get(aspect));
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
			itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			
			item.setItemMeta(itemMeta);
			
			inv.getItem(aspectToSlot.get(aspect) - 9).setType(Material.WHITE_STAINED_GLASS_PANE);
			inv.getItem(aspectToSlot.get(aspect) + 9).setType(Material.WHITE_STAINED_GLASS_PANE);
		}
	}
	
	//Responds to a player clicking an item in the 'Select An Aspect' inventory
	public static void respond(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		Player p = (Player) e.getWhoClicked();
		UUID playerUUID = p.getUniqueId();
		ItemStack clickedItem = e.getCurrentItem();
		ClickType clickType = e.getClick();
		int clickedSlot = e.getRawSlot();
		
		SkillTree skillTree = (SkillTree) LegacyCraft.getPlayerData(p.getUniqueId(), PlayerData.SKILL_TREE);
		
		switch(clickedSlot) {
		case 9:
			SelectAClassInv.load(p);
			break;
		case 11:
		case 13:
		case 15:
		case 17:
			PlayerClass openedClass = (PlayerClass) LegacyCraft.getPlayerData(playerUUID, PlayerData.CLASS_INVENTORY_OPEN);
			SkillTree.Aspect clickedAspect = SkillTree.Aspect.valueOf(clickedItem.getItemMeta().getDisplayName().toUpperCase());
			
			LegacyCraft.setPlayerData(playerUUID, PlayerData.ASPECT_INVENTORY_OPEN, clickedAspect);
			
			if(clickType.isLeftClick()) {
				skillTree.setAspect(openedClass, clickedAspect);
				
				for(int slot : aspectToSlot.values()) {
					inv.getItem(slot).removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
				}
				
				for(int slot : selectedSlots) {
					inv.getItem(slot).setType(Material.BLACK_STAINED_GLASS_PANE);
				}
				
				ItemStack item = inv.getItem(aspectToSlot.get(clickedAspect));
				ItemMeta itemMeta = item.getItemMeta();
				itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
				itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				
				item.setItemMeta(itemMeta);
				
				inv.getItem(aspectToSlot.get(clickedAspect) - 9).setType(Material.WHITE_STAINED_GLASS_PANE);
				inv.getItem(aspectToSlot.get(clickedAspect) + 9).setType(Material.WHITE_STAINED_GLASS_PANE);
			} else if(clickType.isRightClick()) {
				BuildInv.load(p);
			}
			break;
		}
	}

}
