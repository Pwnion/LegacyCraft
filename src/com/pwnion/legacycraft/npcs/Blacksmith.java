package com.pwnion.legacycraft.npcs;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import com.pwnion.legacycraft.LegacyCraft;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.persistence.DelegatePersistence;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.persistence.PersistenceLoader;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;

public class Blacksmith extends Trait {

	public Blacksmith() {
		super("blacksmith");
		legacycraft = JavaPlugin.getPlugin(LegacyCraft.class);
	}

	LegacyCraft legacycraft = null;

	boolean SomeSetting = false;
	
	@Persist 
    @DelegatePersistence(LocationPersister.class) // explicit delegation
    Location homeLocation;
	Location workLocation;

	/**
    @Persist Location implicitComplexType; // implicit delegation
    static {
        PersistenceLoader.registerPersistDelegate(ImplicitComplexTypePersister.class);
    } //**/ 
        
    // see the 'Persistence API' section
    //@Persist("mysettingname") boolean automaticallyPersistedSetting = false;

	// Here you should load up any values you have previously saved (optional). 
    // This does NOT get called when applying the trait for the first time, only loading onto an existing npc at server start.
    // This is called AFTER onAttach so you can load defaults in onAttach and they will be overridden here.
    // This is called BEFORE onSpawn, npc.getBukkitEntity() will return null.
	public void load(DataKey key) {
		
	}

	// Save settings for this NPC (optional). These values will be persisted to the Citizens saves file
	public void save(DataKey key) {
		key.setBoolean("SomeSetting",SomeSetting);
	}

    // An example event handler. All traits will be registered automatically as Bukkit Listeners.
	@EventHandler
	public void click(NPCRightClickEvent event){
		//Handle a click on a NPC. The event has a getNPC() method. 
		//Be sure to check event.getNPC() == this.getNPC() so you only handle clicks on this NPC!
		
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
		legacycraft.getServer().getLogger().info(npc.getName() + "has been assigned MyTrait!");
	}

    // Run code when the NPC is despawned. This is called before the entity actually despawns so npc.getBukkitEntity() is still valid.
	@Override
	public void onDespawn() {
        
	}

	//Run code when the NPC is spawned. Note that npc.getBukkitEntity() will be null until this method is called.
        //This is called AFTER onAttach and AFTER Load when the server is started.
	@Override
	public void onSpawn() {

	}

        //run code when the NPC is removed. Use this to tear down any repeating tasks.
	@Override
	public void onRemove() {
        
	}

}
