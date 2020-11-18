package com.pwnion.legacycraft.abilities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.pwnion.legacycraft.LegacyCraft;

public abstract class HotbarAbility {
	public enum Type {
		APTITUDE1(Material.WOODEN_HOE),
		APTITUDE2(Material.STONE_HOE),
		PROFICIENCY1(Material.IRON_HOE),
		PROFICIENCY2(Material.DIAMOND_HOE);
		
		private final Material material;
		
		private Type(Material material) {
			this.material = material;
		}
		
		public final Material getMaterial() {
			return material;
		}
		
		public static final ArrayList<Material> getMaterials() {
			ArrayList<Material> validMaterials = new ArrayList<Material>(4);
			for(int i = 0; i < 4; i++) {
				validMaterials.add(HotbarAbility.Type.values()[i].getMaterial());
			}
			
			return validMaterials;
		}
	}
	
	public abstract String activate(Player p);
	
	protected void cooldown(Player p, Type type, int cooldownTimer) {
		p.setCooldown(type.getMaterial(), cooldownTimer);
		
		Bukkit.getServer().getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
			public void run() {
				p.setCooldown(type.getMaterial(), 0);
			}
		}, cooldownTimer);
	}
}
