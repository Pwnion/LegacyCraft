package com.pwnion.legacycraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.pwnion.legacycraft.listeners.PlayerInteract;
import com.pwnion.legacycraft.listeners.PlayerJoin;
import com.pwnion.legacycraft.listeners.PlayerMove;
import com.pwnion.legacycraft.listeners.PlayerToggleFlight;
 
public class LegacyCraft extends JavaPlugin {

	//Declare lots of variables that can be accessed by this classes getter and setter methods
	//These variables facilitate the storing of values used to track players actions
	private static Plugin plugin;
	private static Map<UUID, Integer> playerJumpCounters = new HashMap<>();
	private static Map<UUID, Integer> playerJumpSlot = new HashMap<>();
	private static Map<UUID, String> playerClass = new HashMap<>();
	private static Map<UUID, Float> playerFallDistance = new HashMap<>();
	private static Map<UUID, ArrayList<String>> playerInventorySave = new HashMap<>();
	private static Map<UUID, Boolean> playerAdventureMode = new HashMap<>();

	//Called when the plugin is enabled
	public void onEnable() {
		plugin = this;
		
		saveDefaultConfig();
		new ConfigAccessor("inventory-gui.yml").saveDefaultConfig();
		new ConfigAccessor("player-data.yml").saveDefaultConfig();
		
		registerEvents(this,
			new PlayerJoin(),
			new PlayerMove(),
			new PlayerToggleFlight(),
			new EntityDamage(),
			new InventoryClick(),
			new PlayerInteract(),
			new PlayerGameModeChange()
		);
		
		this.getCommand("class").setExecutor((CommandExecutor) new OnCommand());
		this.getCommand("pillar").setExecutor((CommandExecutor) new OnCommand());
		this.getCommand("test").setExecutor((CommandExecutor) new OnCommand());
		
		//Creates new instance of a BukkitRunnable, within which all code runs every game tick
		new BukkitRunnable() {
            @Override
            public void run() {
            	for(Player p : Bukkit.getOnlinePlayers()) {
            		UUID playerUUID = p.getUniqueId();
            		
            		if(p.isOnGround()) {
            			//Handles instances where the player hadn't used all of their ability jumps,
                		//but still must take fall damage
                		float fallDistanceLastTick = getFallDistance(playerUUID);
                		float fallDistance = p.getFallDistance();
                		
                		if(fallDistanceLastTick > 6 && fallDistance == 0 && p.getAllowFlight()) {
                			p.damage((fallDistanceLastTick / 4f - 1.5f) * 2f);
                		}
            		}
            		setFallDistance(playerUUID, p.getFallDistance());
            	}
            }
		}.runTaskTimer(this, 0L, 0L);
	}

	//Called when the plugin is disabled
	public void onDisable() {
		//Set plugin to null to prevent memory leaks
		plugin = null;
	}
	
	//Makes registering events in onEnable() simpler and cleaner
	public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
		for (Listener listener : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
		}
	}
	
	/*
	 * GETTERS AND SETTERS
	 */
	
	public static Plugin getPlugin() {
		return plugin;
	}
	
	public static int getJumpCounter(UUID playerUUID) {
		return playerJumpCounters.get(playerUUID);
	}
	
	public static void setJumpCounter(UUID playerUUID, int jumpCounter) {
		playerJumpCounters.put(playerUUID, jumpCounter);
	}
	
	public static int getJumpSlot(UUID playerUUID) {
		return playerJumpSlot.get(playerUUID);
	}
	
	public static void setJumpSlot(UUID playerUUID, int jumpSlot) {
		playerJumpSlot.put(playerUUID, jumpSlot);
	}
	
	public static String getClass(UUID playerUUID) {
		return playerClass.get(playerUUID);
	}
	
	public static void setClass(UUID playerUUID, String className) {
		playerClass.put(playerUUID, className);
	}
	
	public static float getFallDistance(UUID playerUUID) {
		return playerFallDistance.get(playerUUID);
	}
	
	public static void setFallDistance(UUID playerUUID, float fallDamage) {
		playerFallDistance.put(playerUUID, fallDamage);
	}
	
	public static List<String> getPlayerInventorySave(UUID playerUUID) {
		return playerInventorySave.get(playerUUID);
	}
	
	public static void setPlayerInventorySave(UUID playerUUID, int index, String value) {
		playerInventorySave.get(playerUUID).set(index, value);
	}
	
	public static void setPlayerInventorySave(UUID playerUUID, ArrayList<String> list) {
		playerInventorySave.put(playerUUID, list);
	}
	
	public static boolean getPlayerAdventureMode(UUID playerUUID) {
		return playerAdventureMode.get(playerUUID);
	}
	
	public static void setPlayerAdventureMode(UUID playerUUID, boolean isInAdventureMode) {
		playerAdventureMode.put(playerUUID, isInAdventureMode);
	}
}