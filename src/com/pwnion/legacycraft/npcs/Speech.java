package com.pwnion.legacycraft.npcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.entity.Player;

import com.pwnion.legacycraft.Util;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;

public class Speech {
	
	private static HashMap<Trait, ArrayList<String>> lines = new HashMap<Trait, ArrayList<String>>();
	
	public static void loadFiles() {
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("Hello [PLAYER], I am [NPC]");
		lines.put(CitizensAPI.getTraitFactory().getTrait("blacksmith"), temp);
		
		Util.br(lines.keySet().toString());
		Util.br(lines.values().toString());
	}
	
	public static void save() {
		
	}
	
	public static String getRnd(NPC npc, Trait trait, Player p) {
		Random rand = new Random(); 
		ArrayList<String> list = lines.get(trait);
		String line = list.get(rand.nextInt(list.size()));
		line.replaceAll("[PLAYER]", p.getName());
		line.replaceAll("[NPC]", npc.getName());
		return line;
	}
}
