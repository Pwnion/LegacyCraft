package com.pwnion.legacycraft.npcs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import net.citizensnpcs.api.persistence.Persister;
import net.citizensnpcs.api.util.DataKey;

public class LocationPersister implements Persister<Location> {
	
	public Location create(DataKey root) {
		World world = Bukkit.getWorld(root.getString("world"));
        return new Location(world, root.getDouble("x"), root.getDouble("y"), root.getDouble("z"));
    }
	
    public void save(Location real, DataKey root) {
        root.setString("world", real.getWorld().getName());
        root.setDouble("x", real.getX());
        root.setDouble("y", real.getY());
        root.setDouble("z", real.getZ());
        root.setDouble("yaw", real.getYaw());
        root.setDouble("pitch", real.getPitch());
    }
}
