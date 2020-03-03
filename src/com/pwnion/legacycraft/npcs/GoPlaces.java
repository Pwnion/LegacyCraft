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
	private HashMap<Integer, Location> placesToGo;
	
	public GoPlaces(NPC npc, HashMap<Integer, Location> placesToGo) {
		this.placesToGo = placesToGo;
		this.npc = npc;
	}

	@Override
	public void run(GoalSelector sel) {
		for(int time : placesToGo.keySet()) {
			if(npc.getEntity().getWorld().getTime() == time) {
				Bukkit.getLogger().log(Level.INFO, "Moving NPC '" + npc.getName() + "' to " + placesToGo.get(time));
				npc.getDefaultGoalController().addGoal(new MoveToGoal(npc, placesToGo.get(time)), 1);
			}
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
