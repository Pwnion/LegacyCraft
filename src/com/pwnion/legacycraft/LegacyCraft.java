package com.pwnion.legacycraft;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.pwnion.legacycraft.commands.OnCommand;
import com.pwnion.legacycraft.listeners.EntityDamage;
import com.pwnion.legacycraft.listeners.InventoryClick;
import com.pwnion.legacycraft.listeners.PlayerGameModeChange;
import com.pwnion.legacycraft.listeners.PlayerJoin;
import com.pwnion.legacycraft.listeners.PlayerMove;
import com.pwnion.legacycraft.listeners.PlayerQuit;
import com.pwnion.legacycraft.listeners.PlayerToggleFlight;
 
public class LegacyCraft extends JavaPlugin {

	//Declare lots of variables that can be accessed by this classes getter and setter methods
	//These variables facilitate the storing of values used to track players actions
	private static final HashMap<UUID, HashMap<PlayerData, Object>> playerData = new HashMap<UUID, HashMap<PlayerData, Object>>();
	private static Plugin plugin;
	
	//Makes registering events in onEnable() simpler and cleaner
	private void registerEvents(Listener... listeners) {
		for (Listener listener : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(listener, this);
		}
	}
	
	//Makes registering commands in onEnable() simpler and cleaner
	private void registerCommands(String... commands) {
		for (String command : commands) {
			this.getCommand(command).setExecutor((CommandExecutor) new OnCommand());
		}
	}
	
	//Called when the plugin is enabled
	public void onEnable() {
		plugin = this;
		
		//Create and populate config files if needed
		saveDefaultConfig();
		new ConfigAccessor("inventory-gui.yml").saveDefaultConfig();
		new ConfigAccessor("player-data.yml").saveDefaultConfig();
		new ConfigAccessor("player-data-template.yml").saveDefaultConfig();
		
		//Register listeners
		registerEvents(
			new PlayerJoin(),
			new PlayerMove(),
			new PlayerToggleFlight(),
			new EntityDamage(),
			new InventoryClick(),
			new PlayerGameModeChange(),
			new PlayerQuit()
		);
		
		//Register commands
		registerCommands(
		    "legacycraft",
		    "test"
		);
		
		//Runs the run() method once per game tick
		new BukkitRunnable() {
            @Override
            public void run() {
            	for(Player p : Bukkit.getOnlinePlayers()) {
            		UUID playerUUID = p.getUniqueId();
            		
            		//Handles instances where the player hadn't used all of their ability jumps,
            		//but still must take fall damage
            		if(p.isOnGround()) {
            			float fallDistanceLastTick = (float) getPlayerData(playerUUID, PlayerData.FALL_DISTANCE);
                		float fallDistance = p.getFallDistance();
                		
                		if(fallDistanceLastTick > 6 && fallDistance == 0 && p.getAllowFlight()) {
                			p.damage((fallDistanceLastTick / 4f - 1.5f) * 2f);
                		}
            		}
            		setPlayerData(playerUUID, PlayerData.FALL_DISTANCE, p.getFallDistance());
            	}
            }
		}.runTaskTimer(this, 0L, 0L);
	}

	//Called when the plugin is disabled
	public void onDisable() {
		//Set plugin to null to prevent memory leaks
		plugin = null;
	}
	
	/*
	 * GETTERS AND SETTERS
	 */
	
	public static final Plugin getPlugin() {
		return plugin;
	}
	
	public static final HashMap<PlayerData, Object> getPlayerData(UUID playerUUID) {
		return playerData.get(playerUUID);
	}
	
	public static final Object getPlayerData(UUID playerUUID, PlayerData data) {
		return playerData.get(playerUUID).get(data);
	}
	
	public static final void setPlayerData(UUID playerUUID, HashMap<PlayerData, Object> data) {
		playerData.put(playerUUID, data);
	}
	
	public static final void setPlayerData(UUID playerUUID, PlayerData data, Object obj) {
		playerData.get(playerUUID).put(data, obj);
	}
	
	public static final void removePlayerData(UUID playerUUID) {
		playerData.remove(playerUUID);
	}
}