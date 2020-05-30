package com.pwnion.legacycraft.abilities.proficiencies;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TerraShamanProficiency1 {
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
	public static String activate(Player p) {
		
		//Code
		
		return ChatColor.DARK_GREEN + "Casted Earthquake!";
	}
}
