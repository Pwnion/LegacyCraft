package com.pwnion.legacycraft.abilities.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.abilities.SkillTree;
import com.pwnion.legacycraft.abilities.SkillTree.Aptitude;
import com.pwnion.legacycraft.abilities.SkillTree.Aspect;
import com.pwnion.legacycraft.abilities.SkillTree.Build;
import com.pwnion.legacycraft.abilities.SkillTree.Jump;
import com.pwnion.legacycraft.abilities.SkillTree.PlayerClass;
import com.pwnion.legacycraft.abilities.SkillTree.Proficiency;

public class BuildInv extends Inv {
	//Loads the relevant Build inventory based on the class and aspect a player previously clicked
	public static void load(Player p) {
		UUID playerUUID = p.getUniqueId();
		
		SkillTree skillTree = PlayerData.getSkillTree(playerUUID);
		PlayerClass openedClass = PlayerData.getClassInventoryOpen(playerUUID);
		Aspect openedAspect = PlayerData.getAspectInventoryOpen(playerUUID);
		
		InvName buildInvName = InvName.valueOf(skillTree.getBuild(openedClass, openedAspect).toString());
		InventoryView inv = p.openInventory(DeserialiseInventory.get(buildInvName));
		
		HashMap<Aptitude, Boolean> aptitudes = skillTree.getEquippedAptitudes(openedClass);
		for(Aptitude aptitude : aptitudes.keySet()) {
			if(aptitudes.get(aptitude)) {
				inv.getItem(aptitudeToSlot.get(aptitude)).setType(Material.ENCHANTED_BOOK);
			}
		}
		
		Jump jump = skillTree.getEquippedJump(openedClass);
		if(!jump.equals(Jump.NONE)) {
			inv.getItem(jumpToSlot.get(jump)).setType(Material.ENCHANTED_BOOK);
		}
		
		HashMap<Proficiency, Boolean> proficiencies = skillTree.getEquippedProficiencies(skillTree.getBuild(openedClass, openedAspect));
		for(Proficiency proficiency : proficiencies.keySet()) {
			if(proficiencies.get(proficiency)) {
				inv.getItem(proficiencyToSlot.get(proficiency)).setType(Material.ENCHANTED_BOOK);
			}
		}
	}
	
	//Responds to a player clicking an item in the 'Build' inventory
	public static void respond(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		Player p = (Player) e.getWhoClicked();
		UUID playerUUID = p.getUniqueId();
		int clickedSlot = e.getRawSlot();
		
		SkillTree skillTree = PlayerData.getSkillTree(playerUUID);
		PlayerClass openedClass = PlayerData.getClassInventoryOpen(playerUUID);
		Aspect openedAspect = PlayerData.getAspectInventoryOpen(playerUUID);
		
		Consumer<Boolean> putAbilityInInv = (aptitude) -> {
			Build openedBuild = skillTree.getBuild(openedClass, openedAspect);
			
			int slotOffset = (int) (Math.floor(clickedSlot / (aptitude ? 29 : 35))) + (aptitude ? 1 : 3);
			int customModelData = -1;
			Build builds[] = SkillTree.Build.values();
			for(int i = 1; i < builds.length; i++) {
				if(builds[i].equals(openedBuild)) {
					customModelData = aptitude ? ((2 * (((int) (i - 1) / 4) + 1)) - (clickedSlot == 28 ? 1 : 0)) : (8 + (2 * i) - (clickedSlot == 34 ? 1 : 0));
					break;
				}
			}
			
			ItemStack ability = new ItemStack(Material.IRON_HOE);
			ItemMeta abilityItemMeta = ability.getItemMeta();
			abilityItemMeta.setDisplayName("Placeholder");
			abilityItemMeta.setCustomModelData(customModelData);
			ability.setItemMeta(abilityItemMeta);
			
			Function<ItemStack[], Integer> calcFirstBarrierIndex = (inventory) -> {
				for(int i = 0; i < 9; i++) {
					if(inventory[i] != null && inventory[i].getType().equals(Material.BARRIER) && inventory[i].getItemMeta().getCustomModelData() == 1) {
						return i;
					}
				}
				return null;
			};
			
			Function<ItemStack[], ItemStack[]> editInv = (invToEdit) -> {
				ItemStack temp[] = invToEdit;
				int hotbarIndex = calcFirstBarrierIndex.apply(invToEdit) + slotOffset;
				if(hotbarIndex > 8) hotbarIndex -= 9;
				temp[hotbarIndex] = ability;
				return invToEdit;
			};
			
			Supplier<ArrayList<Build>> getBuildsForOpenedClass = () -> {
				ArrayList<Build> classBuilds = new ArrayList<Build>();
				for(Build build : SkillTree.Build.values()) {
					if(build.toString().contains(openedClass.toString())) {
						classBuilds.add(build);
					}
				}
				return classBuilds;
			};
			
			if(aptitude) {
				if(skillTree.getPlayerClass().equals(openedClass)) {
					if(skillTree.getAspect().equals(Aspect.NONE)) {
						ArrayList<Build> buildsToEdit = getBuildsForOpenedClass.get();
						for(Build build : buildsToEdit) {
							skillTree.setHotbar(build, editInv.apply(skillTree.getHotbar(build)));
						}
					} else {
						p.getInventory().setContents(editInv.apply(p.getInventory().getContents()));
						
						ArrayList<Build> buildsToEdit = getBuildsForOpenedClass.get();
						buildsToEdit.remove(skillTree.getBuild());
						for(Build build : buildsToEdit) {
							skillTree.setHotbar(build, editInv.apply(skillTree.getHotbar(build)));
						}
					}
				} else {
					skillTree.setInventory(openedClass, editInv.apply(skillTree.getInventory(openedClass)));
					
					for(Build build : getBuildsForOpenedClass.get()) {
						skillTree.setHotbar(build, editInv.apply(skillTree.getHotbar(build)));
					}
				}
			} else {
				if(!skillTree.getAspect().equals(openedAspect)) {
					skillTree.setHotbar(openedBuild, editInv.apply(skillTree.getHotbar(openedBuild)));
				} else if(!skillTree.getPlayerClass().equals(openedClass)) {
					skillTree.setInventory(openedClass, editInv.apply(skillTree.getInventory(openedClass)));
					
					for(Build build : getBuildsForOpenedClass.get()) {
						skillTree.setHotbar(build, editInv.apply(skillTree.getHotbar(build)));
					}
				} else {
					p.getInventory().setContents(editInv.apply(p.getInventory().getContents()));
				}
			}
		};
		
		switch(clickedSlot) {
		case 0:
			SelectAnAspectInv.load(p);
			click(p);
			break;
		case 28:
		case 37:
			Aptitude clickedAptitude = slotToAptitude.get(clickedSlot);
			
			if(p.hasPermission("legacycraft.op")) {
				skillTree.setUnlockedAptitude(openedClass, clickedAptitude);
			}
			
			HashMap<Aptitude, Boolean> unlockedAptitudes = skillTree.getUnlockedAptitudes(openedClass);
			HashMap<Aptitude, Boolean> equippedAptitudes = skillTree.getEquippedAptitudes(openedClass);
			
			if(unlockedAptitudes.get(clickedAptitude) && !equippedAptitudes.get(clickedAptitude)) {
				skillTree.setEquippedAptitude(openedClass, clickedAptitude);
				inv.getItem(clickedSlot).setType(Material.ENCHANTED_BOOK);

				putAbilityInInv.accept(true);
				
				click(p);
				//Fancy particle effects and stuff
			}
			
			break;
		case 31:
		case 40:
			Jump clickedJump = slotToJump.get(clickedSlot);
			
			if(clickedJump.equals(skillTree.getEquippedJump())) return;
			
			if(p.hasPermission("legacycraft.op")) {
				skillTree.setUnlockedJump(openedClass, clickedJump);
			}
			
			HashMap<Jump, Boolean> unlockedJumps = skillTree.getUnlockedJumps(openedClass);
			
			if(unlockedJumps.get(clickedJump)) {
				inv.getItem(31).setType(Material.BOOK);
				inv.getItem(40).setType(Material.BOOK);
				inv.getItem(clickedSlot).setType(Material.ENCHANTED_BOOK);
				
				skillTree.setEquippedJump(openedClass, slotToJump.get(clickedSlot));
				
				click(p);
			}
			
			break;
		case 34:
		case 43:
			Build build = skillTree.getBuild(openedClass, openedAspect);
			Proficiency clickedProficiency = slotToProficiency.get(clickedSlot);
			
			if(p.hasPermission("legacycraft.op")) {
				skillTree.setUnlockedProficiency(build, clickedProficiency);
			}
			
			HashMap<Proficiency, Boolean> unlockedProficiencies = skillTree.getUnlockedProficiencies(build);
			HashMap<Proficiency, Boolean> equippedProficiencies = skillTree.getEquippedProficiencies(build);
			
			if(unlockedProficiencies.get(clickedProficiency) && !equippedProficiencies.get(clickedProficiency)) {
				skillTree.setEquippedProficiency(build, clickedProficiency);
				inv.getItem(clickedSlot).setType(Material.ENCHANTED_BOOK);
				
				putAbilityInInv.accept(false);
				
				click(p);
				
				//Fancy particle effects and stuff
			}
			
			break;
		}
	}
}
