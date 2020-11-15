package com.pwnion.legacycraft.npcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.ConfigAccessor;
import com.pwnion.legacycraft.Util;

import net.citizensnpcs.api.npc.NPC;

public class Speech {
	
	private static HashMap<String, ArrayList<String>> lines = new HashMap<>();
	
	private static final Random rnd = new Random();
	
	public static void loadFiles() {
		final ConfigAccessor npcDataConfig = new ConfigAccessor("npc-data.yml");
		final ConfigurationSection npcDataCS = npcDataConfig.getRoot();
		String nodePrefix = "lines.";
		
		npcDataCS.getConfigurationSection("lines").getKeys(false).forEach((traitName) -> {
			ArrayList<String> traitLines = new ArrayList<>();
			
			npcDataCS.getList(nodePrefix + traitName).forEach((line) -> {
				traitLines.add((String) line);
			});
			
			lines.put(traitName, traitLines);
		});
		
		Util.br(lines.keySet().toString());
		Util.br(lines.values().toString());
		Bukkit.getLogger().info(lines.toString());
	}
	
	public static String getRnd(NPC npc, String traitName, Player p) {
		ArrayList<String> list = lines.get(traitName.toLowerCase());
		String line = list.get(rnd.nextInt(list.size()));
		line = line.replace("[PLAYER]", p.getName());
		line = line.replace("[NPC]", npc.getName());
		return line;
	}
}
