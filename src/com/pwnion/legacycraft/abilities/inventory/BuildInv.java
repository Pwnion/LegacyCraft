package com.pwnion.legacycraft.abilities.inventory;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

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
		
		SkillTree skillTree = (SkillTree) LegacyCraft.getPlayerData(p.getUniqueId(), PlayerData.SKILL_TREE);
		PlayerClass openedClass = (PlayerClass) LegacyCraft.getPlayerData(playerUUID, PlayerData.CLASS_INVENTORY_OPEN);
		Aspect openedAspect = (Aspect) LegacyCraft.getPlayerData(playerUUID, PlayerData.ASPECT_INVENTORY_OPEN);
		
		InvName buildInvName = InvName.valueOf(skillTree.getBuild(openedClass, openedAspect).toString());
		InventoryView inv = p.openInventory(DeserialiseInventory.get(buildInvName));
		
		HashMap<Aptitude, Boolean> aptitudes = skillTree.getEquippedAptitudes(openedClass);
		for(Aptitude aptitude : aptitudes.keySet()) {
			if(aptitudes.get(aptitude)) {
				inv.getItem(aptitudeToSlot.get(aptitude)).setType(Material.ENCHANTED_BOOK);
			}
		}
		
		Jump jump = skillTree.getEquippedJump(openedClass);
		inv.getItem(jumpToSlot.get(jump)).setType(Material.ENCHANTED_BOOK);
		
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
		
		SkillTree skillTree = (SkillTree) LegacyCraft.getPlayerData(p.getUniqueId(), PlayerData.SKILL_TREE);
		PlayerClass openedClass = (PlayerClass) LegacyCraft.getPlayerData(playerUUID, PlayerData.CLASS_INVENTORY_OPEN);
		Aspect openedAspect = (Aspect) LegacyCraft.getPlayerData(playerUUID, PlayerData.ASPECT_INVENTORY_OPEN);
		
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
				
				click(p);
				
				//Fancy particle effects and stuff
			}
			
			break;
		}
	}
}
