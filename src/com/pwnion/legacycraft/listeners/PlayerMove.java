package com.pwnion.legacycraft.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.abilities.SkillTree;
import com.pwnion.legacycraft.abilities.SkillTree.PlayerClass;
import com.pwnion.legacycraft.quests.triggers.NearLocation;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerMove implements Listener {
	
	@SuppressWarnings("unused")
	private static final int distanceToGround(Player p) {
		Location loc = p.getLocation();
		
		int counter = 0;
		while(true) {
			loc.subtract(0, counter, 0);
			if(loc.getBlock().getType().isSolid() || counter > 255) {
				return counter;
			}
			counter++;
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		
		try {
			NearLocation.onPlayerMove(p);
		} catch (NoClassDefFoundError e1) {
			e1.printStackTrace();
		}
		
		SkillTree skillTree = PlayerData.getSkillTree(playerUUID);
		
		//Resets jump ability usage every time a player touches the ground
		if(p.isOnGround()) {
			if(PlayerData.getJumpCount(playerUUID) != 0) {
				PlayerData.setJumpCount(playerUUID, 0);
				if(skillTree.getPlayerClass().equals(PlayerClass.SHAMAN)) {
					p.removePotionEffect(PotionEffectType.SLOW_FALLING);
				}
				PlayerData.setShamanFalling(playerUUID, false);
			}
			if(p.getGameMode().equals(GameMode.ADVENTURE) && !p.getAllowFlight() && !skillTree.getPlayerClass().equals(PlayerClass.NONE)) p.setAllowFlight(true);
		}
		
		if(PlayerData.getShamanFalling(playerUUID)) {
			if(p.isSneaking()) {
				p.removePotionEffect(PotionEffectType.SLOW_FALLING);
			} else {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 120, 3, false, false, false), true);
			}
		}
	}
}