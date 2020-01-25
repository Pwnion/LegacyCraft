package com.pwnion.legacycraft.abilities.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;

import com.pwnion.legacycraft.ConfigAccessor;

public class InventoryFromFile {
	final int size;
	final String title;
	final List<Integer> slots;
	final List<String> names;
	final List<Material> materials;
	final List<List<String>> descriptions;
	public final Inventory inventory;
	
	private List<Material> stringsToMaterials(List<String> stringList) {
		List<Material> materialList = new ArrayList<>();
		for(String str : stringList) {
			materialList.add(Material.getMaterial(str));
		}
		return materialList;
	}
	
	private List<List<String>> getTwoBranches(String parentNode, ConfigurationSection targetCS) {
		List<List<String>> returnList = new ArrayList<>();
		ConfigurationSection section = targetCS.getConfigurationSection(parentNode);
		Set<String> sectionKeys = section.getKeys(false);
		sectionKeys.forEach((key) -> returnList.add(section.getStringList(key)));
		return returnList;
	}
	
	public InventoryFromFile(String targetRootKey, String targetFile) {
		ConfigurationSection targetCS = new ConfigAccessor(targetFile).getCustomConfig().getConfigurationSection(targetRootKey);
		
		this.size = targetCS.getInt("size");
		this.title = targetCS.getString("title");
		this.slots = targetCS.getIntegerList("slots");
		this.names = targetCS.getStringList("names");
		this.materials = stringsToMaterials(targetCS.getStringList("materials"));
		this.descriptions = getTwoBranches("descriptions", targetCS);
		this.inventory = new InventoryCreator(size, title, slots, names, materials, descriptions).getInv();
	}
}
