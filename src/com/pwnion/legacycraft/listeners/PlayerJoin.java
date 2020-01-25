package com.pwnion.legacycraft.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.pwnion.legacycraft.ConfigAccessor;
import com.pwnion.legacycraft.LegacyCraft;

public class PlayerJoin implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();

		//Populates variables that help track player actions
		LegacyCraft.setJumpCounter(playerUUID, 0);
		LegacyCraft.setFallDistance(playerUUID, 0);
		LegacyCraft.setPlayerInventorySave(playerUUID, new ArrayList<String>(Arrays.asList("", "")));
		
		ConfigAccessor playerData = new ConfigAccessor("player-data.yml");
		FileConfiguration playerDataFile = playerData.getCustomConfig();
		ConfigurationSection playerDataCS = playerDataFile.getRoot();
		Set<String> playerUUIDs = playerDataCS.getKeys(true);
		
		Set<String> keys = new ConfigAccessor("inventory-gui.yml").getCustomConfig().getKeys(false);
		boolean start = false;
		
		if(!playerUUIDs.contains("players." + playerUUID.toString())) {
			playerDataCS.set("players." + playerUUID.toString() + ".selected.class", "");
			playerDataCS.set("players." + playerUUID.toString() + ".selected.striker", "");
			playerDataCS.set("players." + playerUUID.toString() + ".selected.vanguard", "");
			playerDataCS.set("players." + playerUUID.toString() + ".selected.rogue", "");
			playerDataCS.set("players." + playerUUID.toString() + ".selected.shaman", "");
			
			for(String key : keys) {
				if(key.equals("ignis-striker")) start = true;
				if(start) {
					playerDataCS.set("players." + playerUUID.toString() + "." + key + ".aptitude", 0);
					playerDataCS.set("players." + playerUUID.toString() + "." + key + ".jump", 0);
					playerDataCS.set("players." + playerUUID.toString() + "." + key + ".aspect-ability", 0);
				}
			}
			playerData.saveCustomConfig();
		}
		
		LegacyCraft.setClass(playerUUID, playerDataCS.getString("players." + playerUUID.toString() + ".selected.class"));
		
		String className = playerDataCS.getString("players." + playerUUID.toString() + ".selected.class");
		String aspect = playerDataCS.getString("players." + playerUUID.toString() + ".selected." + playerDataCS.getString("players." + playerUUID.toString() + ".selected.class"));
		
		if(!className.equals("") && !aspect.equals("")) {
			LegacyCraft.setJumpSlot(playerUUID, playerDataCS.getInt("players." + playerUUID.toString() + "." + aspect + "-" + className + ".jump"));
		} else {
			LegacyCraft.setJumpSlot(playerUUID, 0);
		}
	}
}