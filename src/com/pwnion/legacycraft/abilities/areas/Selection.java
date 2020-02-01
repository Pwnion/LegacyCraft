package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.ConfigAccessor;

import net.md_5.bungee.api.ChatColor;

public class Selection {
    public static final ConfigAccessor structuresConfig = new ConfigAccessor("structures.yml");
    public static final ConfigurationSection structuresCS = structuresConfig.getRoot();
    
    @SuppressWarnings("unchecked")
    public static final ArrayList<String> load(String name) {
        return (ArrayList<String>) structuresCS.getList("structures." + name);
    }
    
    private Player p;
    private Block pos1;
    private Block pos2;
    
    public Selection(Player p) {
        this.p = p;
    }
    
    public final String setPos1() {
        pos1 = p.getTargetBlock(null, 100);
        return ChatColor.GOLD + "Set position 1!";
    }
    
    public final String setPos2() {
        pos2 = p.getTargetBlock(null, 100);
        return ChatColor.GOLD + "Set position 2!";
    }
    
    public final String export(String name) {
        ArrayList<String> data = new ArrayList<String>();
        if(pos1 == null || pos2 == null) {
            return ChatColor.DARK_RED + "You forgot to set both positions!";
        } else {
            //add to data (i.e. data.put(key, value))
            for(Block block : RectangularPrism.get(pos1, pos2)) {
                if(!block.isEmpty()) {
                	Location RelBlockLoc = block.getLocation().subtract(p.getLocation());
                    data.add(DataToString(RelBlockLoc, block.getType()));
                }
            }
        }
        structuresCS.set("structures." + name, data);
        structuresConfig.saveCustomConfig();
        return ChatColor.DARK_GREEN + "Saved to file!";
    }
    
    private String DataToString(Location loc, Material material) {
    	return loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "," + material.name();
    }
}