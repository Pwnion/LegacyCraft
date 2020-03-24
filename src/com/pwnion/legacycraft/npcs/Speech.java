package com.pwnion.legacycraft.npcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.Util;

import net.citizensnpcs.api.npc.NPC;

public class Speech {
	
	private static HashMap<String, ArrayList<String>> lines = new HashMap<String, ArrayList<String>>();
	
	public static void loadFiles() {
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("Hello [PLAYER], I am [NPC]");
		lines.put("blacksmith", temp);
		
		Util.br(lines.keySet().toString());
		Util.br(lines.values().toString());
		Bukkit.getLogger().info(lines.toString());
	}
	
	public static void save() {
		
	}
	
	public static String getRnd(NPC npc, String traitName, Player p) {
		Random rand = new Random(); 
		ArrayList<String> list = lines.get(traitName.toLowerCase());
		String line = list.get(rand.nextInt(list.size()));
		line = line.replace("[PLAYER]", p.getName());
		line = line.replace("[NPC]", npc.getName());
		return line;
	}
}
