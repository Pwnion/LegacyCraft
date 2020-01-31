package com.pwnion.legacycraft.abilities.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;

import com.pwnion.legacycraft.ConfigAccessor;

public class InventoryFromFile {
	//Converts a given list of Strings to a list of Materials
	private static final List<Material> stringsToMaterials(List<String> stringList) {
		List<Material> materialList = new ArrayList<>();
		for(String str : stringList) {
			materialList.add(Material.getMaterial(str));
		}
		return materialList;
	}
	
	//Returns a list of a list of strings after going two layers deep into a yml file relative to a given parent node
	private static final List<List<String>> getTwoBranches(String parentNode, ConfigurationSection targetCS) {
		List<List<String>> returnList = new ArrayList<>();
		ConfigurationSection section = targetCS.getConfigurationSection(parentNode);
		Set<String> sectionKeys = section.getKeys(false);
		sectionKeys.forEach((key) -> returnList.add(section.getStringList(key)));
		return returnList;
	}
	
	//Returns an inventory based on the parameters given
	public static final Inventory get(InvName invToOpen, String targetFile) {
		ConfigurationSection targetCS = new ConfigAccessor(targetFile).getCustomConfig().getConfigurationSection(invToOpen.toString());
		
		return InventoryCreator.getInv(
			targetCS.getInt("size"),
			targetCS.getString("title"),
			targetCS.getIntegerList("slots"),
			targetCS.getStringList("names"),
			stringsToMaterials(targetCS.getStringList("materials")),
			getTwoBranches("descriptions", targetCS)
		);
	}
}
