package com.pwnion.legacycraft.listeners;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.items.enhancements.Enhancement;
import com.pwnion.legacycraft.items.enhancements.EnhancementType;
import com.pwnion.legacycraft.levelling.Experience;
import com.pwnion.legacycraft.levelling.ExperienceType;


public class EntityDamageByEntity implements Listener {
	
	
	private static final HashSet<Material> SWORDS = new HashSet<Material>(Arrays.asList(
			Material.DIAMOND_SWORD,
			Material.IRON_SWORD,
			Material.GOLDEN_SWORD,
			Material.STONE_SWORD,
			Material.WOODEN_SWORD));

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		Entity attacked = e.getEntity();
		Entity attacker = e.getDamager();
		
		if(attacker instanceof Player) {
			Player p = (Player) attacker;
			ItemStack item = p.getInventory().getItemInMainHand();
			
			double damage = e.getFinalDamage();
			
			if(attacked.isInvulnerable() || e.isCancelled()) {
				damage = 0;
			}
			
			if(damage > 0) {
				
				if(SWORDS.contains(item.getType())) { //If sword add sword experience equal to damage
					Experience playerExperience = (Experience) LegacyCraft.getPlayerData(p.getUniqueId(), PlayerData.EXPERIENCE);
					playerExperience.addExperience((int) damage, ExperienceType.SWORDS);
				}
				
				Enhancement.apply(p, (Damageable) attacked, item, damage, EnhancementType.WEAPON_HIT);
			} else {
				Enhancement.apply(p, null, item, 0, EnhancementType.WEAPON_SWING);
			}
		}
	}
}
