package com.pwnion.legacycraft.abilities.proficiencies;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.abilities.HotbarAbility;

public class TerraShamanProficiency1 extends HotbarAbility {
	//Template
	
	/*
	 * Class: Shaman
	 * Aspect: Terra
	 * Ability Name: Earthquake 
	 * Ability Type: Ability
	 * Description: The earth shakes causing players within the area of effect to slow, not be able to sprint and may stumble.
	 * 
	 * TODO: Show earthy particles on the ground surrounding player
	 * TODO: Slow down players and mobs in the area including those that enter the area after activation.
	 * TODO: Stop player sprint in the area.
	 * 
	 * TODO: Test if swim animation can be given to players when not in water
	 * TODO: If possible see if it works as a viable stumbling animation that is applied at random intervals
	 */
	
	/**
	 * Activates the Terra Shaman Proficiency 1 for player
	 * 
	 * @param p Player to activate for
	 * @return Message shown to player
	 */

	@Override
	public String activate(Player p) {
		//pass
		cooldown(p, HotbarAbility.Type.PROFICIENCY1, 40);
		return ChatColor.DARK_GREEN + "Casted Earthquake!";
	}
}
