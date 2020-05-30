package com.pwnion.legacycraft.abilities.proficiencies;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class IgnisStrikerProficiency1 {
	
	
	/*
	 * Class: Striker
	 * Aspect: Ignis
	 * Ability Name: Magma Wave
	 * Description: Sends out a wave of lava in direction your facing
	 * 
	 * TODO: Show fire like particles in a wave form
	 * TODO: Create low level lava in a wave shape that moves forward
	 * Lava can be created at lower heights just like snow and water
	 * TODO: Add knockback to any Entities in the way
	 * TODO: Add the ability to customise the damage taken by the lava wave (Through a constant)
	 * 
	 * Make sure to only replace Air/Long grass like blocks and not solid blocks.
	 * 
	 * Bonus points if you can make the wave 'splash' on any wall by raising it's height. (Only if that actually looks cool)
	 */
	
	/**
	 * Activates the Ignis Striker Proficiency 1 for player
	 * 
	 * @param p Player to activate for
	 * @return Message shown to player
	 */
	public static String activate(Player p) {
		
		//Code
		
		
		return ChatColor.DARK_GREEN + "Casted Magma Wave!";
	}
}
