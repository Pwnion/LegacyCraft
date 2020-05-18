package com.pwnion.legacycraft.abilities.inventory;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.ConfigAccessor;
import com.pwnion.legacycraft.abilities.inventory.holders.AerRogue;
import com.pwnion.legacycraft.abilities.inventory.holders.AerShaman;
import com.pwnion.legacycraft.abilities.inventory.holders.AerStriker;
import com.pwnion.legacycraft.abilities.inventory.holders.AerVanguard;
import com.pwnion.legacycraft.abilities.inventory.holders.AquaRogue;
import com.pwnion.legacycraft.abilities.inventory.holders.AquaShaman;
import com.pwnion.legacycraft.abilities.inventory.holders.AquaStriker;
import com.pwnion.legacycraft.abilities.inventory.holders.AquaVanguard;
import com.pwnion.legacycraft.abilities.inventory.holders.Aspect;
import com.pwnion.legacycraft.abilities.inventory.holders.Blacksmith;
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
import com.pwnion.legacycraft.abilities.inventory.holders.WarpGates;
import com.pwnion.legacycraft.abilities.inventory.holders.WeaponEnhancements;

public class DeserialiseInventory {
	//Returns an inventory saved in a file
	public static final Inventory get(InvName invToOpen) {
		ConfigurationSection targetCS = new ConfigAccessor("inventory-menus.yml").getCustomConfig().getConfigurationSection(invToOpen.toString());
		
		String title = targetCS.getString("title");
		int size = targetCS.getInt("size");
		ItemStack contents[] = targetCS.getList("contents").toArray(new ItemStack[0]);
		InventoryHolder holder;
		
		switch(title.replace("\u00A75\u00A7l", "")) {
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
		case "Warp Gates":
			holder = new WarpGates();
			break;
		case "Blacksmith":
			holder = new Blacksmith();
			break;
		default:
			holder = null;
		}
		
		Inventory inv;
		if(size % 9 == 0) {
			inv = Bukkit.createInventory(holder, size, title);
		} else {
			inv = Bukkit.createInventory(holder, InventoryType.HOPPER, title);
		}
		inv.setContents(contents);
		
		return inv;
	}
}
