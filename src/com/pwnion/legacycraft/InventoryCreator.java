package com.pwnion.legacycraft;

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

import com.pwnion.legacycraft.invholders.ArcticRogue;
import com.pwnion.legacycraft.invholders.ArcticShaman;
import com.pwnion.legacycraft.invholders.ArcticStriker;
import com.pwnion.legacycraft.invholders.ArcticVanguard;
import com.pwnion.legacycraft.invholders.Aspect;
import com.pwnion.legacycraft.invholders.IgnisRogue;
import com.pwnion.legacycraft.invholders.IgnisShaman;
import com.pwnion.legacycraft.invholders.IgnisStriker;
import com.pwnion.legacycraft.invholders.IgnisVanguard;
import com.pwnion.legacycraft.invholders.SelectClass;
import com.pwnion.legacycraft.invholders.SelectOption;
import com.pwnion.legacycraft.invholders.TerraRogue;
import com.pwnion.legacycraft.invholders.TerraShaman;
import com.pwnion.legacycraft.invholders.TerraStriker;
import com.pwnion.legacycraft.invholders.TerraVanguard;
import com.pwnion.legacycraft.invholders.VacuousRogue;
import com.pwnion.legacycraft.invholders.VacuousShaman;
import com.pwnion.legacycraft.invholders.VacuousStriker;
import com.pwnion.legacycraft.invholders.VacuousVanguard;
import com.pwnion.legacycraft.invholders.WeaponEnhancements;

public class InventoryCreator {
	private final int size;
	private final String title;
	private final List<Integer> slots;
	private final List<String> names;
	private final List<Material> materials;
	private final List<List<String>> descriptions;
	
	InventoryCreator(int size, String title, List<Integer> slots, List<String> names, List<Material> materials, List<List<String>> descriptions) {
		this.size = size;
		this.title = title;
		this.slots = slots;
		this.names = names;
		this.materials = materials;
		this.descriptions = descriptions;
	}
	
	private ItemStack createItem(String name, Material material, List<String> desc) {
        final ItemStack itemStack = new ItemStack(material, 1);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        
        itemMeta.setDisplayName(name);
        itemMeta.setLore(desc);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
	
	Inventory getInv() {
		Inventory inv;
		InventoryHolder holder;
		
		switch(title) {
		case "Select an Option":
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
		case "Vacuous Striker":
			holder = new VacuousStriker();
			break;
		case "Arctic Striker":
			holder = new ArcticStriker();
			break;
		case "Ignis Vanguard":
			holder = new IgnisVanguard();
			break;
		case "Terra Vanguard":
			holder = new TerraVanguard();
			break;
		case "Vacuous Vanguard":
			holder = new VacuousVanguard();
			break;
		case "Arctic Vanguard":
			holder = new ArcticVanguard();
			break;
		case "Ignis Rogue":
			holder = new IgnisRogue();
			break;
		case "Terra Rogue":
			holder = new TerraRogue();
			break;
		case "Vacuous Rogue":
			holder = new VacuousRogue();
			break;
		case "Arctic Rogue":
			holder = new ArcticRogue();
			break;
		case "Ignis Shaman":
			holder = new IgnisShaman();
			break;
		case "Terra Shaman":
			holder = new TerraShaman();
			break;
		case "Vacuous Shaman":
			holder = new VacuousShaman();
			break;
		case "Arctic Shaman":
			holder = new ArcticShaman();
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

