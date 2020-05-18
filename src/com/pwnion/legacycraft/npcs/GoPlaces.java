package com.pwnion.legacycraft.npcs;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.Util;

import net.citizensnpcs.api.npc.NPC;

public class GoPlaces {
	
	private static BukkitTask allGoPlaces = null;
	private static HashMap<NPC, HashMap<Integer, Location>> data = new HashMap<NPC, HashMap<Integer, Location>>();
	
	private final static int roundingConstant = 1;

	private static HashMap<Integer, Location> format(HashMap<Integer, Location> placesToGo) {
		HashMap<Integer, Location> formatted = new HashMap<Integer, Location>();
		for(int time : placesToGo.keySet()) {
			formatted.put(Math.round(time / roundingConstant), placesToGo.get(time));
		}
		return formatted;
	}

	public final static void start() {
		allGoPlaces = Bukkit.getServer().getScheduler().runTaskTimer(LegacyCraft.getPlugin(), new Runnable() {
			public void run() {
				for(NPC npc : data.keySet()) {
					HashMap<Integer, Location> placesToGo = data.get(npc);
					if(placesToGo.containsKey(Math.round(npc.getEntity().getWorld().getTime() / roundingConstant))) {
						int time = Math.round(npc.getEntity().getWorld().getTime() / roundingConstant);
						Util.br("Moving NPC '" + npc.getName() + "' to " + placesToGo.get(time) + " time is " + npc.getEntity().getWorld().getTime());
						
						//npc.getDefaultGoalController().addGoal(new MoveToGoal(npc, placesToGo.get(time)), 2);
						
						/* Astar?
						CitizensNavigator nav = new CitizensNavigator(npc);
						PathStrategy pathStrat = new AStarNavigationStrategy(npc, placesToGo.get(time), nav.getDefaultParameters());
						Path path = new Path((Collection<Vector>) pathStrat.getPath());
						path.run(npc); //*/
					}
				}
				
			}
		}, 0, 1);
	}
	
	public static void run(NPC npc, HashMap<Integer, Location> placesToGo) {
		data.put(npc, format(placesToGo));
		if(allGoPlaces == null) {
			start();
		}
	}
	
	public static void remove(NPC npc) {
		data.remove(npc);
		if(data.isEmpty()) {
			cancel();
		}
	}
	
	public static void cancel() {
		if(allGoPlaces == null) {
			return;
		}
		allGoPlaces.cancel();
		allGoPlaces = null;
	}

}
