package com.pwnion.legacycraft.npcs;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.citizensnpcs.api.ai.Goal;
import net.citizensnpcs.api.ai.GoalSelector;
import net.citizensnpcs.api.ai.goals.MoveToGoal;
import net.citizensnpcs.api.npc.NPC;

public class GoPlaces implements Goal {

	//Hashmap of time and place
	NPC npc;
	private HashMap<Long, Location> placesToGo;
	
	public GoPlaces(NPC npc, HashMap<Integer, Location> placesToGo) {
		for(int time : placesToGo.keySet()) {
			this.placesToGo.put((long) time, placesToGo.get(time));
		}
		this.npc = npc;
	}

	@Override
	public void run(GoalSelector sel) {
		if(placesToGo.containsKey(npc.getEntity().getWorld().getTime())) {
			long time = npc.getEntity().getWorld().getTime();
			Bukkit.getLogger().log(Level.INFO, "Moving NPC '" + npc.getName() + "' to " + placesToGo.get(time));
			npc.getDefaultGoalController().addGoal(new MoveToGoal(npc, placesToGo.get(time)), 1);
		}
	}

	@Override
	public boolean shouldExecute(GoalSelector arg0) {
		if(!placesToGo.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public void reset() {
		//placesToGo.clear();
	}
	


}
