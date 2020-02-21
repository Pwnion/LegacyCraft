package com.pwnion.legacycraft.npcs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.pwnion.legacycraft.ConfigAccessor;

public class HomesAndJobs {

	private static final ConfigAccessor locationsConfig = new ConfigAccessor("Citzen-Locations.yml");
	private static final ConfigurationSection locationsCS = locationsConfig.getRoot();

	protected static HashSet<Location> homes = loadHomes();
	protected static HashMap<Location, String> jobs = loadJobs();
	
	protected static final HashSet<Location> getHomes() {
		return homes;
	}
	
	protected static final HashMap<Location, String> getJobs() {
		return jobs;
	}
	
	public static final void addHome(Location location) {
		homes.add(location);
		save();
	}
	
	public static final void addJob(Location location, String type) {
		jobs.put(location, type);
		save();
	}
	
	public static final boolean removeHome(Location location) {
		if(homes.contains(location)) {
			return false;
		}
		homes.remove(location);
		return true;
	}
	
	public static final boolean removeJob(Location location) {
		if(jobs.containsKey(location)) {
			return false;
		}
		jobs.remove(location);
		return true;
	}
	
	public static final boolean changeJob(Location location, String toType) {
		if(jobs.containsKey(location)) {
			return false;
		}
		jobs.put(location, toType);
		return true;
	}

    @SuppressWarnings("unchecked")
	private static final HashSet<Location> loadHomes() {
    	ArrayList<String> homeData = (ArrayList<String>) locationsCS.getList("locations.homes");
    	HashSet<Location> locations = new HashSet<Location>();
		for(String string : homeData) {
			locations.add(StringToLocation(string));
		}
    	return locations;
    }
    
    @SuppressWarnings("unchecked")
	private static final HashMap<Location, String> loadJobs() {
    	ArrayList<String> jobData = (ArrayList<String>) locationsCS.getList("locations.jobs.");
    	HashMap<Location, String> jobMap = new HashMap<Location, String>();
		for(String string : jobData) {
			String data[] = string.split("|");
			jobMap.put(StringToLocation(data[0]), data[1]);
		}
    	return jobMap;
    }
    
    private static final void save() {
    	homesCS.set("homes", LocationToString(homes));
        homesConfig.saveCustomConfig();
        
        ArrayList<String> jobData = new ArrayList<String>(jobs.size() - 1);
        
        for(Location jobLoc : jobs.keySet()) {
        	jobData.add(LocationToString(jobLoc) + jobs.get(jobLoc));
        }
        
        jobsCS.set("jobs", jobData);
        jobsConfig.saveCustomConfig();
    }
    
	private static final String LocationToString(Location l) {
		return l.getWorld().getName() + "," + l.getX() + "," + l.getY() + "," + l.getZ() + "," + l.getYaw() + "," + l.getPitch();
	}
	
	private static final Location StringToLocation(String str) {
		String data[] = str.split(",");
		return new Location(Bukkit.getWorld(data[0]), Double.valueOf(data[1]), Double.valueOf(data[2]), Double.valueOf(data[3]), Float.valueOf(data[4]), Float.valueOf(data[5]));
	}
	
	private static final ArrayList<String> LocationToString(Collection<Location> locations) {
		ArrayList<String> strings = new ArrayList<String>(locations.size() - 1);
		for(Location location : locations) {
			strings.add(LocationToString(location));
		}
		return strings;
	}
}
