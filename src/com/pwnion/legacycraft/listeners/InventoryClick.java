package com.pwnion.legacycraft.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pwnion.legacycraft.ConfigAccessor;
import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.abilities.inventory.DeserialiseInventory;
import com.pwnion.legacycraft.abilities.inventory.SelectAClassInv;
import com.pwnion.legacycraft.abilities.inventory.SelectAnAspectInv;
import com.pwnion.legacycraft.abilities.inventory.WarpGatesInv;
import com.pwnion.legacycraft.abilities.inventory.WeaponEnhancementsInv;
import com.pwnion.legacycraft.quests.triggers.GetItem;
import com.pwnion.legacycraft.abilities.SkillTree;
import com.pwnion.legacycraft.abilities.SkillTree.Aspect;
import com.pwnion.legacycraft.abilities.SkillTree.PlayerClass;
import com.pwnion.legacycraft.abilities.inventory.BlacksmithInv;
import com.pwnion.legacycraft.abilities.inventory.BuildInv;
import com.pwnion.legacycraft.abilities.inventory.CharacterBuildMenuInv;
import com.pwnion.legacycraft.abilities.inventory.Inv;
import com.pwnion.legacycraft.abilities.inventory.InvName;

public class InventoryClick implements Listener {
	private static final HashMap<String, Class<? extends Inv>> holderToInvClass = getHolderToInvClass();
	private static final HashMap<String, Class<? extends Inv>> getHolderToInvClass() {
		int invCount = InvName.values().length;
		HashMap<String, Class<? extends Inv>> holderToInvClass = new HashMap<String, Class<? extends Inv>>(invCount);
		List<Inventory> inventories = new ArrayList<>(invCount);
		String fileName = "inventory-menus.yml";
		
		ArrayList<Class<? extends Inv>> invClasses = new ArrayList<Class<? extends Inv>>() {
			private static final long serialVersionUID = 1L;
			{
				add(CharacterBuildMenuInv.class);
				add(WeaponEnhancementsInv.class);
				add(SelectAClassInv.class);
				add(SelectAnAspectInv.class);
				add(BuildInv.class);
				add(WarpGatesInv.class);
				add(BlacksmithInv.class);
			}
		};
		
		Set<String> keys = new ConfigAccessor(fileName).getCustomConfig().getKeys(false);
		for(String key : keys) {
			inventories.add(DeserialiseInventory.get(InvName.valueOf(key)));
		}

		for(int i = 0; i < invCount; i++) {
			String invHolder = inventories.get(i).getHolder().toString();
			int index = i < 4 ? i : i < 20 ? 4 : i - 15;
			holderToInvClass.put(invHolder.substring(0, invHolder.indexOf("@")), invClasses.get(index));
		}
		
		return holderToInvClass;
	}
	
	@EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
		final Player p = (Player) e.getWhoClicked();
		final UUID playerUUID = p.getUniqueId();
        final Inventory currentInv = e.getInventory();
        final int clickedSlot = e.getRawSlot();
        final ItemStack clickedItem = e.getCurrentItem();
        final ItemStack cursorItem = e.getCursor();
        SkillTree skillTree = (SkillTree) LegacyCraft.getPlayerData(playerUUID, PlayerData.SKILL_TREE);
        
        //Handles Quest Updates for items in inventory
        GetItem.updateItemQuests(p);
        
        //Invokes the 'respond' method of the inventory class that matches the players open inventory
        //Returns false if the player is not in the inventory GUI
        Supplier<Boolean> handleGUI = () -> {
        	try {
            	String currentHolder = currentInv.getHolder().toString();
            	currentHolder = currentHolder.substring(0, currentHolder.indexOf("@"));
            	
            	if(!holderToInvClass.keySet().contains(currentHolder)) return false;
            	
            	if(!p.getGameMode().equals(GameMode.ADVENTURE)) {
            		p.closeInventory();
            		p.sendMessage(ChatColor.RED + "You must be in adventure mode to do that!");
            		return true;
            	}
            	
            	e.setCancelled(true);
            	
            	holderToInvClass.get(currentHolder).getMethod("respond", new Class[] {InventoryClickEvent.class}).invoke(null, e);
            } catch(Exception ex) {
            	return false;
            };
            return true;
        };
        
        if(!handleGUI.get()) {
        	if(!(p.getGameMode().equals(GameMode.ADVENTURE) && !skillTree.getPlayerClass().equals(PlayerClass.NONE))) return;
        	if(clickedItem == null) {
        		e.setCancelled(true);
        		return;
        	}
        	
    		if(clickedItem.getType().equals(Material.IRON_HOE) && cursorItem.getType().equals(Material.AIR)) {
	        	//Picking up ability
	        	LegacyCraft.getPlayerData(playerUUID).put(PlayerData.SWAP_SLOT, clickedSlot);
	        } else if((clickedItem.getType().equals(Material.IRON_HOE) && cursorItem.getType().equals(Material.IRON_HOE))) {
    			//Swapping abilities
	        	Bukkit.getServer().getScheduler().runTask(LegacyCraft.getPlugin(), new Runnable() {
	        		public void run() {
	        			p.getOpenInventory().setItem((int) LegacyCraft.getPlayerData(playerUUID, PlayerData.SWAP_SLOT), p.getItemOnCursor());
	    	        	p.setItemOnCursor(null);
	        		}
	        	});
	        } else if (cursorItem.getType().equals(Material.IRON_HOE)) {
	        	e.setCancelled(true);
	        	p.getOpenInventory().setItem((int) LegacyCraft.getPlayerData(playerUUID, PlayerData.SWAP_SLOT), cursorItem);
	        	p.setItemOnCursor(null);
	        } else {
	        	e.setCancelled(true);
	    		
	    		Material itemMaterial = clickedItem.getType();
	    		ItemMeta itemMeta = clickedItem.getItemMeta();
	    		
	    		if(itemMaterial.equals(Material.COMPASS)) {
	    			CharacterBuildMenuInv.load(p);
	    		} else if(itemMaterial.equals(Material.GLASS_PANE) && itemMeta.getCustomModelData() == 1) {
	    			WarpGatesInv.load(p);
	    		} else if(itemMaterial.equals(Material.STICK) && itemMeta.getCustomModelData() == 1 && cursorItem.getType().equals(Material.AIR)) {
	    			if(skillTree.getAspect().equals(Aspect.NONE)) return;
	     			
	    			Inventory inv = p.getInventory();
	    			
	    			ItemStack hotbar[] = new ItemStack[9];
	    			for(int i = 0; i < 9; i++) {
	    				hotbar[i] = inv.getItem(i);
	    			}
	    			
	    			for(int i = 0; i < 8; i++) {
	    				inv.setItem(i + 1, hotbar[i]);
	    			}
	    			inv.setItem(0, hotbar[8]);
	    		}
	        }
    		/*
    		 * if (!(clickedItem.getType().equals(Material.IRON_HOE) && cursorItem.getType().equals(Material.IRON_HOE) || clickedItem.getType().equals(Material.IRON_HOE) && cursorItem.getType().equals(Material.AIR))) {
	               e.setCancelled(true);
	        } else 
    		 */
        }
	}
}
