package com.pwnion.legacycraft.quests;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class LocationData {
	
	ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
	
	LocationData(World world, Integer x, Integer y, Integer z, int radius) {
		data.add(newArray(world, x, y, z, radius));
	}
	
	LocationData(String data) {
		this.data = deserialise(data);
	}
	
	public static ArrayList<Object> newArray(World world, Integer x, Integer y, Integer z, int radius) {
		ArrayList<Object> array = new ArrayList<Object>(4);
		array.add(world);
		array.add(x);
		array.add(y);
		array.add(z);
		array.add(radius);
		return array;
	}
	
	public boolean isPlayerHere(Player p) {
		for(ArrayList<Object> location : data) {
			World world = (World) location.get(0);
			Integer x = (Integer) location.get(1);
			Integer y = (Integer) location.get(2);
			Integer z = (Integer) location.get(3);
			int radius = (int) location.get(4);
			
			
			Location pLoc = p.getLocation();
			
			boolean isWorld = pLoc.getWorld() == world;
			boolean isX = inRadius(pLoc.getBlockX(), x, radius);
			boolean isY = inRadius(pLoc.getBlockY(), y, radius);
			boolean isZ = inRadius(pLoc.getBlockZ(), z, radius);
			
			if(isWorld && isX && isY && isZ) {
				return true;
			}
		}
		return false;
		
	}
	
	private boolean inRadius(int pDim, Integer dim, int radius) {
		return dim == null || ((pDim > dim - radius) && (pDim < dim + radius));
	}
	
	//Not used
	public String serialise() {
		String output = "";
		for(ArrayList<Object> location : data) {
			World world = (World) location.get(0);
			Integer x = (Integer) location.get(1);
			Integer y = (Integer) location.get(2);
			Integer z = (Integer) location.get(3);
			int radius = (int) location.get(4);
			
			output = output + world.getName() + "|" + x + "|" + y + "|" + z + "|" + radius + ",";
		}
		return output.substring(0, output.length() - 2);
	}
	
	public static ArrayList<ArrayList<Object>> deserialise(String data) {
		String dataSplit[] = data.split(",");
		ArrayList<ArrayList<Object>> output = new ArrayList<ArrayList<Object>>(dataSplit.length - 1);
		for(String locationStr : dataSplit) {
			String locSplit[] = locationStr.split("|");
			ArrayList<Object> location = new ArrayList<Object>();
			location.add(Bukkit.getWorld(locSplit[0]));
			location.add(Integer.parseInt(locSplit[1]));
			location.add(Integer.parseInt(locSplit[2]));
			location.add(Integer.parseInt(locSplit[3]));
			location.add(Integer.parseInt(locSplit[4]));
			output.add(location);
		}
		return output;
	}
}
