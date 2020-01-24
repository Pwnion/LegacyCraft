package com.pwnion.legacycraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIFunctionality implements Listener {
	private static final String FILE = "inventory-gui.yml";
	private static final List<Inventory> INVENTORIES = getAllInventories();
	
	private static final List<Inventory> getAllInventories() {
		List<Inventory> inventories = new ArrayList<>(20);
		
		Set<String> keys = new ConfigAccessor(FILE).getCustomConfig().getKeys(false);
		for(String key : keys) {
			inventories.add(new InventoryFromFile(key, FILE).inventory);
		}
		return inventories;
	}
	
	private static final HashMap<String, Integer> classToSlot = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put("striker", 11);
			put("vanguard", 13);
			put("rogue", 15);
			put("shaman", 17);
		}
	};
	
	HashMap<String, Integer> aspectToSlot = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put("ignis", 11);
			put("terra", 13);
			put("vacuous", 15);
			put("arctic", 17);
		}
	};
	
	ArrayList<Integer> selectedSlots = new ArrayList<Integer>() {
		private static final long serialVersionUID = 1L;
		{
			add(2);
			add(4);
			add(6);
			add(8);
			add(20);
			add(22);
			add(24);
			add(26);
		}
	};
	
	@EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        final Inventory currentInv = e.getInventory();
        final Player p = (Player) e.getWhoClicked();
        final UUID playerUUID = p.getUniqueId();
        final int clickedSlot = e.getRawSlot();
        final ItemStack clickedItem = e.getCurrentItem();
        
        int invIndex = -1;
        for(int i = 0; i < 20; i++) {
        	String fileHolder = INVENTORIES.get(i).getHolder().toString();
        	
        	String currentHolder;
        	try {
        		currentHolder = currentInv.getHolder().toString();
        	} catch(Exception ex) {
        		break;
        	}

        	if(!currentHolder.contains("@")) break;
        	
        	if(fileHolder.substring(0, fileHolder.indexOf("@")).equals(currentHolder.substring(0, currentHolder.indexOf("@")))) {
        		invIndex = i;
        		e.setCancelled(true);
        		break;
        	}
        }
        
        switch(invIndex) {
        case 0:
        	switch(clickedSlot) {
        	case 1:
        		InventoryView newInv = p.openInventory(new InventoryFromFile("class", FILE).inventory);
        		
        		if(!LegacyCraft.getClass(playerUUID).equals("")) {
        			ItemMeta itemMeta = newInv.getItem(classToSlot.get(LegacyCraft.getClass(playerUUID))).getItemMeta();
    				itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    				itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    				
        			newInv.getItem(classToSlot.get(LegacyCraft.getClass(playerUUID))).setItemMeta(itemMeta);
        			
        			newInv.getItem(classToSlot.get(LegacyCraft.getClass(playerUUID)) - 9).setType(Material.WHITE_STAINED_GLASS_PANE);
        			newInv.getItem(classToSlot.get(LegacyCraft.getClass(playerUUID)) + 9).setType(Material.WHITE_STAINED_GLASS_PANE);
        		}
        		
        		break;
        	case 3:
        		p.openInventory(new InventoryFromFile("weapon-enhancements", FILE).inventory);
        		break;
        	default:
        		return;
        	}
        	break;
        case 1:
        	if(clickedSlot == 0) {
        		p.openInventory(new InventoryFromFile("main", FILE).inventory);
        	}
        	break;
        case 2:
        case 3:
        	if(clickedSlot == 9) {
        		switch(invIndex) {
        		case 2:
        			p.openInventory(new InventoryFromFile("main", FILE).inventory);
        			break;
        		case 3:
        			InventoryView newInv = p.openInventory(new InventoryFromFile("class", FILE).inventory);
        			
        			if(!LegacyCraft.getClass(playerUUID).equals("")) {
        				ItemMeta itemMeta = newInv.getItem(classToSlot.get(LegacyCraft.getClass(playerUUID))).getItemMeta();
        				itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        				itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        				
        				newInv.getItem(classToSlot.get(LegacyCraft.getClass(playerUUID))).setItemMeta(itemMeta);
        				
        				newInv.getItem(classToSlot.get(LegacyCraft.getClass(playerUUID)) - 9).setType(Material.WHITE_STAINED_GLASS_PANE);
            			newInv.getItem(classToSlot.get(LegacyCraft.getClass(playerUUID)) + 9).setType(Material.WHITE_STAINED_GLASS_PANE);
            		}

        			break;
        		}
        	} else {
        		if(clickedSlot == 11 || clickedSlot == 13 || clickedSlot == 15 || clickedSlot == 17) {
        			LegacyCraft.setPlayerInventorySave(playerUUID, invIndex - 2, clickedItem.getItemMeta().getDisplayName().toLowerCase());
        			
        			ConfigAccessor playerData = new ConfigAccessor("player-data.yml");
        			FileConfiguration playerDataFile = playerData.getCustomConfig();
        			ConfigurationSection playerDataCS = playerDataFile.getRoot();
        			
        			if(invIndex == 2) {
        				if(e.isLeftClick()) {
        					LegacyCraft.setClass(playerUUID, LegacyCraft.getPlayerInventorySave(playerUUID).get(0));
        					playerDataCS.set("players." + playerUUID.toString() + ".selected.class", LegacyCraft.getPlayerInventorySave(playerUUID).get(0));
        					playerData.saveCustomConfig();
        					
        					for(int slot : classToSlot.values()) {
        						currentInv.getItem(slot).removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
        					}
        					
        					for(int slot : selectedSlots) {
        						currentInv.getItem(slot).setType(Material.BLACK_STAINED_GLASS_PANE);
        					}
        					
        					ItemMeta itemMeta = currentInv.getItem(classToSlot.get(LegacyCraft.getClass(playerUUID))).getItemMeta();
            				itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            				itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        					
        					currentInv.getItem(classToSlot.get(LegacyCraft.getClass(playerUUID))).setItemMeta(itemMeta);
        					
        					currentInv.getItem(classToSlot.get(LegacyCraft.getClass(playerUUID)) - 9).setType(Material.WHITE_STAINED_GLASS_PANE);
                			currentInv.getItem(classToSlot.get(LegacyCraft.getClass(playerUUID)) + 9).setType(Material.WHITE_STAINED_GLASS_PANE);
        					
        				} else if(e.isRightClick()) {
        					InventoryView newInv = p.openInventory(new InventoryFromFile("aspect", FILE).inventory);
            				
        					if(!playerDataCS.getString("players." + playerUUID.toString() + ".selected." + LegacyCraft.getPlayerInventorySave(playerUUID).get(0)).equals("")) {
        						ItemMeta itemMeta = newInv.getItem(aspectToSlot.get(playerDataCS.getString("players." + playerUUID.toString() + ".selected." + LegacyCraft.getPlayerInventorySave(playerUUID).get(0)))).getItemMeta();
                				itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                				itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        						
        						newInv.getItem(aspectToSlot.get(playerDataCS.getString("players." + playerUUID.toString() + ".selected." + LegacyCraft.getPlayerInventorySave(playerUUID).get(0)))).setItemMeta(itemMeta);
        						
        						newInv.getItem(aspectToSlot.get(playerDataCS.getString("players." + playerUUID.toString() + ".selected." + LegacyCraft.getPlayerInventorySave(playerUUID).get(0))) - 9).setType(Material.WHITE_STAINED_GLASS_PANE);
                    			newInv.getItem(aspectToSlot.get(playerDataCS.getString("players." + playerUUID.toString() + ".selected." + LegacyCraft.getPlayerInventorySave(playerUUID).get(0))) + 9).setType(Material.WHITE_STAINED_GLASS_PANE);
        					}
        				}
        			} else {
        				if(e.isLeftClick()) {
            				playerDataCS.set("players." + playerUUID.toString() + ".selected." + LegacyCraft.getPlayerInventorySave(playerUUID).get(0), LegacyCraft.getPlayerInventorySave(playerUUID).get(1));
            				playerData.saveCustomConfig();
            				
            				for(int slot : aspectToSlot.values()) {
        						currentInv.getItem(slot).removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
        					}
            				
            				for(int slot : selectedSlots) {
        						currentInv.getItem(slot).setType(Material.BLACK_STAINED_GLASS_PANE);
        					}
            				
            				ItemMeta itemMeta = currentInv.getItem(aspectToSlot.get(LegacyCraft.getPlayerInventorySave(playerUUID).get(1))).getItemMeta();
            				itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            				itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            				
            				currentInv.getItem(aspectToSlot.get(LegacyCraft.getPlayerInventorySave(playerUUID).get(1))).setItemMeta(itemMeta);
            				
    						currentInv.getItem(aspectToSlot.get(LegacyCraft.getPlayerInventorySave(playerUUID).get(1)) - 9).setType(Material.WHITE_STAINED_GLASS_PANE);
    						currentInv.getItem(aspectToSlot.get(LegacyCraft.getPlayerInventorySave(playerUUID).get(1)) + 9).setType(Material.WHITE_STAINED_GLASS_PANE);
            			} else if(e.isRightClick()) {
            				String newInvName = LegacyCraft.getPlayerInventorySave(playerUUID).get(1) + "-" + LegacyCraft.getPlayerInventorySave(playerUUID).get(0);
            				InventoryView newInv = p.openInventory(new InventoryFromFile(newInvName, FILE).inventory);
            				
            				Set<String> keys = new ConfigAccessor("inventory-gui.yml").getCustomConfig().getKeys(false);
                			for(int i = 0; i < 20; i++) {
                				if(keys.toArray()[i].equals(newInvName)) {
                					int aptitudeSlot = playerDataCS.getInt("players." + playerUUID.toString() + "." + newInvName + ".aptitude");
                        			int jumpSlot = playerDataCS.getInt("players." + playerUUID.toString() + "." + newInvName + ".jump");
                        			int aspectAbilitySlot = playerDataCS.getInt("players." + playerUUID.toString() + "." + newInvName + ".aspect-ability");
                        			
                        			if(aptitudeSlot != 0) newInv.getItem(aptitudeSlot).setType(Material.ENCHANTED_BOOK);
                        			if(jumpSlot != 0) newInv.getItem(jumpSlot).setType(Material.ENCHANTED_BOOK);
                        			if(aspectAbilitySlot != 0) newInv.getItem(aspectAbilitySlot).setType(Material.ENCHANTED_BOOK);
                				}
                			}
            			}
        			}
        		}
        	}
        	break;
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
        case 12:
        case 13:
        case 14:
        case 15:
        case 16:
        case 17:
        case 18:
        case 19:
        case 20:
        	ConfigAccessor playerData = new ConfigAccessor("player-data.yml");
			FileConfiguration playerDataFile = playerData.getCustomConfig();
			ConfigurationSection playerDataCS = playerDataFile.getRoot();
        	
        	if(clickedSlot == 0) {
        		InventoryView newInv = p.openInventory(new InventoryFromFile("aspect", FILE).inventory);
        		
        		if(!playerDataCS.getString("players." + playerUUID.toString() + ".selected." + LegacyCraft.getPlayerInventorySave(playerUUID).get(0)).equals("")) {
        			ItemMeta itemMeta = newInv.getItem(aspectToSlot.get(playerDataCS.getString("players." + playerUUID.toString() + ".selected." + LegacyCraft.getPlayerInventorySave(playerUUID).get(0)))).getItemMeta();
    				itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
    				itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    				
    				newInv.getItem(aspectToSlot.get(playerDataCS.getString("players." + playerUUID.toString() + ".selected." + LegacyCraft.getPlayerInventorySave(playerUUID).get(0)))).setItemMeta(itemMeta);
    				
    				newInv.getItem(aspectToSlot.get(playerDataCS.getString("players." + playerUUID.toString() + ".selected." + LegacyCraft.getPlayerInventorySave(playerUUID).get(0))) - 9).setType(Material.WHITE_STAINED_GLASS_PANE);
					newInv.getItem(aspectToSlot.get(playerDataCS.getString("players." + playerUUID.toString() + ".selected." + LegacyCraft.getPlayerInventorySave(playerUUID).get(0))) + 9).setType(Material.WHITE_STAINED_GLASS_PANE);
        		}
        	} else {
        		String key = (String) new ConfigAccessor("inventory-gui.yml").getCustomConfig().getKeys(false).toArray()[invIndex];
        		
        		switch(clickedSlot) {
        		case 28:
        		case 37:
        			currentInv.getItem(28).setType(Material.BOOK);
        			currentInv.getItem(37).setType(Material.BOOK);
        			
        			currentInv.getItem(clickedSlot).setType(Material.ENCHANTED_BOOK);
        			
        			playerDataCS.set("players." + playerUUID.toString() + "." + key + ".aptitude", clickedSlot);
        			playerData.saveCustomConfig();
        			
        			break;
        		case 31:
        		case 40:
        			currentInv.getItem(31).setType(Material.BOOK);
        			currentInv.getItem(40).setType(Material.BOOK);
        			
        			currentInv.getItem(clickedSlot).setType(Material.ENCHANTED_BOOK);
        			
        			playerDataCS.set("players." + playerUUID.toString() + "." + key + ".jump", clickedSlot);
        			playerData.saveCustomConfig();
        			
        			if(LegacyCraft.getPlayerInventorySave(playerUUID).get(0).equals(playerDataCS.getString("players." + playerUUID.toString() + ".selected.class"))) {
        				if(LegacyCraft.getPlayerInventorySave(playerUUID).get(1).equals(playerDataCS.getString("players." + playerUUID.toString() + ".selected." + playerDataCS.getString("players." + playerUUID.toString() + ".selected.class")))) {
                			LegacyCraft.setJumpSlot(playerUUID, playerDataCS.getInt("players." + playerUUID.toString() + "." + LegacyCraft.getPlayerInventorySave(playerUUID).get(1) + "-" + LegacyCraft.getPlayerInventorySave(playerUUID).get(0) + ".jump"));
        				}
        			}
        			
        			break;
        		case 34:
        		case 43:
        			currentInv.getItem(34).setType(Material.BOOK);
        			currentInv.getItem(43).setType(Material.BOOK);
        			
        			currentInv.getItem(clickedSlot).setType(Material.ENCHANTED_BOOK);
        			
        			playerDataCS.set("players." + playerUUID.toString() + "." + key + ".aspect-ability", clickedSlot);
        			playerData.saveCustomConfig();
        			
        			break;
        		}
        	}
        	break;
        default:
        	return;
        }
    }
}
