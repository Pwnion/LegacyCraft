package com.pwnion.legacycraft.npcs.traits;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.npcs.GoPlaces;
import com.pwnion.legacycraft.npcs.NPCHomeWork;
import com.pwnion.legacycraft.npcs.Speech;
import com.pwnion.legacycraft.quests.triggers.SpeakToNPC;
import com.pwnion.legacycraft.abilities.inventory.BlacksmithInv;

import net.citizensnpcs.api.event.NPCRightClickEvent;
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
	boolean first = true;
	Trait trait;
	
	  
    // see the 'Persistence API' section
    //@Persist("mysettingname") boolean automaticallyPersistedSetting = false;

	private void setupGoals() {
		HashMap<Integer, Location> places = new HashMap<Integer, Location>();
		
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
		Util.br("NPC '" + npc.getName() + "' is loading for trait " + this.getName());
		first = false;
	}

	// Save settings for this NPC (optional). These values will be persisted to the Citizens saves file
	public void save(DataKey key) {
		Util.br("NPC '" + npc.getName() + "' is saving for trait " + this.getName());
	}

    // An example event handler. All traits will be registered automatically as Bukkit Listeners.
	@EventHandler
	public void click(NPCRightClickEvent event){
		//Handle a click on a NPC. The event has a getNPC() method. 
		//Be sure to check event.getNPC() == this.getNPC() so you only handle clicks on this NPC!
		if(event.getNPC() == this.getNPC()) {
			Player p = event.getClicker();
			SpeakToNPC.onSpeakToNPC(p, npc.getName());
			
			//If close to work do work related stuff
			//Else do other stuff
			p.sendMessage(ChatColor.GRAY + "REPAIR ITEMS?");
			
			p.sendMessage(Speech.getRnd(npc, this.getName(), p));
			
			PlayerInventory inv = p.getInventory();
			inv.setItemInMainHand(repairItem(inv.getItemInMainHand()));
			Util.br("NPC '" + npc.getName() + "' has been clicked by " + p.getName());
			
			BlacksmithInv.load(p);
		}
	}
	
	public ItemStack repairItem(ItemStack item) {
		if(item.getItemMeta() instanceof Damageable) {
			Damageable dmg = (Damageable) item.getItemMeta();
			dmg.setDamage(0);
			item.setItemMeta((ItemMeta) dmg);
		}
		return item;
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
		Util.br("NPC '" + npc.getName() + "' has called onAttach event for trait " + this.getName());
	}

    // Run code when the NPC is despawned. This is called before the entity actually despawns so npc.getBukkitEntity() is still valid.
	@Override
	public void onDespawn() {
		Util.br("NPC '" + npc.getName() + "' has called onDespawn event for trait " + this.getName());
		GoPlaces.remove(npc);
	}

	//Run code when the NPC is spawned. Note that npc.getBukkitEntity() will be null until this method is called.
    //This is called AFTER onAttach and AFTER Load when the server is started.
	@Override
	public void onSpawn() {
		Util.br("NPC '" + npc.getName() + "' has called onSpawn event for trait " + this.getName());
		
		if(first) {
			Player p = NPCHomeWork.editPlayer;
			
			if(!NPCHomeWork.hasLocations()) {
				p.sendMessage(ChatColor.RED + "No Home/Work found, please add a Home and Work before adding this trait.");
				npc.removeTrait(this.getClass());
				return;
			}
			
			homeLocation = NPCHomeWork.getHome();
			workLocation = NPCHomeWork.getWork();
			p.sendMessage(ChatColor.GOLD + "Transfered Home/Work locations successfully!");
		
			Util.br("NPC '" + npc.getName() + "' has been assigned trait " + this.getName().toUpperCase() + " by " + p.getName());
		}
		
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
		Util.br("NPC '" + npc.getName() + "' has called onRemove event for trait " + this.getName());
		GoPlaces.remove(npc);
	}

}
