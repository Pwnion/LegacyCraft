package com.pwnion.legacycraft.abilities.inventory;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pwnion.legacycraft.abilities.inventory.holders.AquaRogue;
import com.pwnion.legacycraft.abilities.inventory.holders.AquaShaman;
import com.pwnion.legacycraft.abilities.inventory.holders.AquaStriker;
import com.pwnion.legacycraft.abilities.inventory.holders.AquaVanguard;
import com.pwnion.legacycraft.abilities.inventory.holders.Aspect;
import com.pwnion.legacycraft.abilities.inventory.holders.IgnisRogue;
import com.pwnion.legacycraft.abilities.inventory.holders.IgnisShaman;
import com.pwnion.legacycraft.abilities.inventory.holders.IgnisStriker;
import com.pwnion.legacycraft.abilities.inventory.holders.IgnisVanguard;
import com.pwnion.legacycraft.abilities.inventory.holders.SelectClass;
import com.pwnion.legacycraft.abilities.inventory.holders.SelectOption;
import com.pwnion.legacycraft.abilities.inventory.holders.TerraRogue;
import com.pwnion.legacycraft.abilities.inventory.holders.TerraShaman;
import com.pwnion.legacycraft.abilities.inventory.holders.TerraStriker;
import com.pwnion.legacycraft.abilities.inventory.holders.TerraVanguard;
import com.pwnion.legacycraft.abilities.inventory.holders.AerRogue;
import com.pwnion.legacycraft.abilities.inventory.holders.AerShaman;
import com.pwnion.legacycraft.abilities.inventory.holders.AerStriker;
import com.pwnion.legacycraft.abilities.inventory.holders.AerVanguard;
import com.pwnion.legacycraft.abilities.inventory.holders.WeaponEnhancements;

public class InventoryCreator {
	//Builds and returns an ItemStack based on the parameters
	private static final ItemStack createItem(String name, Material material, List<String> desc) {
        final ItemStack itemStack = new ItemStack(material, 1);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        
        itemMeta.setDisplayName(name);
        itemMeta.setLore(desc);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
	
	//Builds and returns an inventory based on the parameters
	public static final Inventory getInv(int size, String title, List<Integer> slots, List<String> names, List<Material> materials, List<List<String>> descriptions) {
		Inventory inv;
		InventoryHolder holder;
		
		switch(title) {
		case "Character Build Menu":
			holder = new SelectOption();
			break;
		case "Weapon Enhancements":
			holder = new WeaponEnhancements();
			break;
		case "Select a Class":
			holder = new SelectClass();
			break;
		case "Select an Aspect":
			holder = new Aspect();
			break;
		case "Ignis Striker":
			holder = new IgnisStriker();
			break;
		case "Terra Striker":
			holder = new TerraStriker();
			break;
		case "Aer Striker":
			holder = new AerStriker();
			break;
		case "Aqua Striker":
			holder = new AquaStriker();
			break;
		case "Ignis Vanguard":
			holder = new IgnisVanguard();
			break;
		case "Terra Vanguard":
			holder = new TerraVanguard();
			break;
		case "Aer Vanguard":
			holder = new AerVanguard();
			break;
		case "Aqua Vanguard":
			holder = new AquaVanguard();
			break;
		case "Ignis Rogue":
			holder = new IgnisRogue();
			break;
		case "Terra Rogue":
			holder = new TerraRogue();
			break;
		case "Aer Rogue":
			holder = new AerRogue();
			break;
		case "Aqua Rogue":
			holder = new AquaRogue();
			break;
		case "Ignis Shaman":
			holder = new IgnisShaman();
			break;
		case "Terra Shaman":
			holder = new TerraShaman();
			break;
		case "Aer Shaman":
			holder = new AerShaman();
			break;
		case "Aqua Shaman":
			holder = new AquaShaman();
			break;
		default:
			holder = null;
		}
		
		if(size == 5) {
			inv = Bukkit.createInventory(holder, InventoryType.HOPPER, ChatColor.DARK_BLUE + "" + ChatColor.BOLD + title); 
		} else {
			inv = Bukkit.createInventory(holder, size, ChatColor.DARK_BLUE + "" + ChatColor.BOLD + title); 
		}
		
		for(int i = 0; i < size; i++) {
			inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
		}

		for(int i = 0; i < slots.size(); i++) {
			inv.setItem(slots.get(i), createItem(names.get(i), materials.get(i), descriptions.get(i)));
		}
		return inv;
	}
}

