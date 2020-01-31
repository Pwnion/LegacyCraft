package com.pwnion.legacycraft.abilities.inventory;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.pwnion.legacycraft.abilities.SkillTree.Aptitude;
import com.pwnion.legacycraft.abilities.SkillTree.Aspect;
import com.pwnion.legacycraft.abilities.SkillTree.PlayerClass;
import com.pwnion.legacycraft.abilities.SkillTree.Jump;
import com.pwnion.legacycraft.abilities.SkillTree.Proficiency;

//Super class for inventory classes
//Also acts as a tagging class for safety in the InventoryClick class
public abstract class Inv {
	static final String FILE = "inventory-gui.yml";
	static final List<Integer> selectedSlots = Arrays.asList(2, 4, 6, 8, 20, 22, 24, 26);
	static final List<Integer> aptitudeSlots = Arrays.asList(28, 37);
	static final List<Integer> jumpSlots = Arrays.asList(31, 40);
	static final List<Integer> proficiencySlots = Arrays.asList(34, 43);
	
	static final ImmutableMap<PlayerClass, Integer> classToSlot = ImmutableMap.of(
		PlayerClass.STRIKER, 11,
		PlayerClass.VANGUARD, 13,
		PlayerClass.ROGUE, 15,
		PlayerClass.SHAMAN, 17
	);
	
	static final ImmutableMap<Aspect, Integer> aspectToSlot = ImmutableMap.of(
		Aspect.IGNIS, 11,
		Aspect.TERRA, 13,
		Aspect.VACUOUS, 15,
		Aspect.ARCTIC, 17
	);
	
	static final ImmutableMap<Aptitude, Integer> aptitudeToSlot = ImmutableMap.of(
		Aptitude.ONE, 28,
		Aptitude.TWO, 37
	);
	
	static final ImmutableMap<Integer, Aptitude> slotToAptitude = ImmutableMap.of(
		28, Aptitude.ONE,
		37, Aptitude.TWO
	);
	
	static final ImmutableMap<Jump, Integer> jumpToSlot = ImmutableMap.of(
		Jump.ONE, 31,
		Jump.TWO, 40
	);
	
	static final ImmutableMap<Integer, Jump> slotToJump = ImmutableMap.of(
		31, Jump.ONE,
		40, Jump.TWO
	);
	
	static final ImmutableMap<Proficiency, Integer> proficiencyToSlot = ImmutableMap.of(
		Proficiency.ONE, 34,
		Proficiency.TWO, 43
	);
	
	static final ImmutableMap<Integer, Proficiency> slotToProficiency = ImmutableMap.of(
		34, Proficiency.ONE,
		43, Proficiency.TWO
	);
}
