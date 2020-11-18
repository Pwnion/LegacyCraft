package com.pwnion.legacycraft.abilities.proficiencies;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.abilities.HotbarAbility;

public class AerRogueProficiency1 extends HotbarAbility {
	//Template
	
	/*
	 * Class: Rogue
	 * Aspect: Aer
	 * Ability Name: Smoke Screen
	 * Ability Type: Ability
	 * Description: Surroundings are filled with smoke
	 * 
	 * Fill 4-5? block area with smoke particles (make this easily modifiable)
	 * Have some kind of tag given to players so that some abilities and mobs do not target players
	 * Making vanilla mobs passive may not be required so just having a HashMap or something similar will do for now.
	 * 
	 * TODO: Show smoke particles (Campfire?)
	 * TODO: Make mobs passive to players in smoke.
	 */
	
	/**
	 * Activates the Aer Rogue Proficiency 1 for player
	 * 
	 * @param p Player to activate for
	 * @return Message shown to player
	 */
	
	@Override
	public String activate(Player p) {
		//pass
		cooldown(p, HotbarAbility.Type.PROFICIENCY1, 40);
		return ChatColor.DARK_GREEN + "Casted Smoke Screen!";
	}
}
