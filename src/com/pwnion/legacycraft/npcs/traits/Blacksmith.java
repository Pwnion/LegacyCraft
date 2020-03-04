package com.pwnion.legacycraft.npcs.traits;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import com.pwnion.legacycraft.OnCommand;
import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.npcs.GoPlaces;
import com.pwnion.legacycraft.npcs.HomeWorkData;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.event.NPCTraitCommandAttachEvent;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;
import net.md_5.bungee.api.ChatColor;

public class Blacksmith extends Trait {

	public Blacksmith() {
		super("blacksmith");
	}

	@Persist Location homeLocation = null;
	@Persist Location workLocation = null;
	
	  
    // see the 'Persistence API' section
    //@Persist("mysettingname") boolean automaticallyPersistedSetting = false;

	private void setupGoals() {
		HashMap<Integer, Location> places = new HashMap<Integer, Location>();
		
		places.clear();
		
		//at 5PM go home (11000 ticks)
		places.put(((5 + 12) - 6) * 1000, homeLocation);
		//at 7AM go to work (1000 ticks)
		places.put(((7) - 6) * 1000, workLocation);
		
		GoPlaces.run(npc, places);
	}
	
	
	// Here you should load up any values you have previously saved (optional). 
    // This does NOT get called when applying the trait for the first time, only loading onto an existing npc at server start.
    // This is called AFTER onAttach so you can load defaults in onAttach and they will be overridden here.
    // This is called BEFORE onSpawn, npc.getBukkitEntity() will return null.
	public void load(DataKey key) {
		Bukkit.getLogger().log(Level.INFO, "NPC '" + npc.getName() + "' is loading for trait " + this.getName());
	}

	// Save settings for this NPC (optional). These values will be persisted to the Citizens saves file
	public void save(DataKey key) {
		Bukkit.getLogger().log(Level.INFO, "NPC '" + npc.getName() + "' is saving for trait " + this.getName());
	}

    // An example event handler. All traits will be registered automatically as Bukkit Listeners.
	@EventHandler
	public void click(NPCRightClickEvent event){
		//Handle a click on a NPC. The event has a getNPC() method. 
		//Be sure to check event.getNPC() == this.getNPC() so you only handle clicks on this NPC!
		if(event.getNPC() == this.getNPC()) {
			Player p = event.getClicker();
			//If close to work do work related stuff
			//Else do other stuff
			Bukkit.getLogger().log(Level.INFO, "NPC '" + npc.getName() + "' has been clicked by " + p.getName());
		}
	}
	
	@EventHandler
	public void onNPCTraitCommandAttachEvent(NPCTraitCommandAttachEvent e) {
		if(!(e.getCommandSender() instanceof Player)) {
			return;
		}
		
		if(e.getNPC() == this.getNPC()) {
			Player p = (Player) e.getCommandSender();
			Bukkit.getLogger().info("NPC '" + npc.getName() + "' has been assigned trait " + this.getName().toUpperCase() + " by " + p.getName());
			
			HomeWorkData data = OnCommand.playerToNPCdata.get(p.getUniqueId());
			
			if(data == null || !data.hasLocations()) {
				p.sendMessage(ChatColor.RED + "No Home/Work found, please add a Home and Work before adding this trait.");
				npc.removeTrait(this.getClass());
				return;
			}
			
			homeLocation = data.getHome();
			workLocation = data.getWork();
			p.sendMessage(ChatColor.GOLD + "Transfered Home/Work locations successfully!");
		
			setupGoals();
		}
	}
      
    // Called every tick
    @Override
    public void run() {
    	
    }

	//Run code when your trait is attached to a NPC. 
    //This is called BEFORE onSpawn, so npc.getBukkitEntity() will return null
    //This would be a good place to load configurable defaults for new NPCs.
	@Override
	public void onAttach() {
		Bukkit.getLogger().log(Level.INFO, "NPC '" + npc.getName() + "' has called onAttach event for trait " + this.getName());
	}

    // Run code when the NPC is despawned. This is called before the entity actually despawns so npc.getBukkitEntity() is still valid.
	@Override
	public void onDespawn() {
		Bukkit.getLogger().log(Level.INFO, "NPC '" + npc.getName() + "' has called onDespawn event for trait " + this.getName());
		GoPlaces.remove(npc);
	}

	//Run code when the NPC is spawned. Note that npc.getBukkitEntity() will be null until this method is called.
    //This is called AFTER onAttach and AFTER Load when the server is started.
	@Override
	public void onSpawn() {
		Bukkit.getLogger().log(Level.INFO, "NPC '" + npc.getName() + "' has called onSpawn event for trait " + this.getName());
		if(homeLocation == null || workLocation == null) {
			Bukkit.getLogger().log(Level.WARNING, "INVALID SETUP: NPC '" + npc.getName() + "' has no places. Removing '" + this.getName() + "' trait");
			npc.removeTrait(this.getClass());
			return;
		}
		setupGoals();
	}

    //run code when the NPC is removed. Use this to tear down any repeating tasks.
	@Override
	public void onRemove() {
		Bukkit.getLogger().log(Level.INFO, "NPC '" + npc.getName() + "' has called onRemove event for trait " + this.getName());
		GoPlaces.remove(npc);
	}

}
