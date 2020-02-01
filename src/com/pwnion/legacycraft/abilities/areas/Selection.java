package com.pwnion.legacycraft.abilities.areas;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.ConfigAccessor;

public class Selection {
    private static final ConfigAccessor structuresConfig = new ConfigAccessor("structures.yml");
    private static final ConfigurationSection structuresCS = structuresConfig.getRoot();
    
    @SuppressWarnings("unchecked")
    public static final HashMap<Location, Material> load(String name) {
        return (HashMap<Location, Material>) structuresCS.get("structures." + name);
    }
    
    private Player p;
    private Block pos1;
    private Block pos2;
    
    Selection(Player p) {
        this.p = p;
    }
    
    public final void setPos1() {
        pos1 = p.getTargetBlock(null, 100);
    }
    
    public final void setPos2() {
        pos2 = p.getTargetBlock(null, 100);
    }
    
    public final void export(String name) {
        HashMap<Location, Material> data = new HashMap<Location, Material>();
        if(pos1 == null || pos2 == null) {
            return;
        } else {
            //add to data (i.e. data.put(key, value))
        	for(Block block : RectangularPrism.get(pos1, pos2)) {
        		if(block.isEmpty()) {
        			data.put(block.getLocation(), block.getType());
        		}
        	}
        }
        structuresCS.set("structures." + name, data);
        structuresConfig.saveCustomConfig();
    }
}