package com.pwnion.legacycraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.items.ItemData;
import com.pwnion.legacycraft.items.ItemManager;
import com.pwnion.legacycraft.items.ItemStat;
import com.pwnion.legacycraft.items.enhancements.Enhancement;
import com.pwnion.legacycraft.levelling.Experience;
import com.pwnion.legacycraft.levelling.ExperienceType;

public class EntityDamageByEntity implements Listener {
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		Entity attacker = e.getDamager();
		//LivingEntity attacked = (LivingEntity) e.getEntity();
		
		if(attacker instanceof Player && e.getEntity() instanceof LivingEntity) {
			Player p = (Player) attacker;
			LivingEntity attacked = (LivingEntity) e.getEntity();
			ItemStack item = p.getInventory().getItemInMainHand();
			ItemData itemData = ItemManager.getItemData(item);
			
			/**
			 * Calculate attack cooldown (no weapon switching) 
			 * {@link https://minecraft.gamepedia.com/Damage#Attack_cooldown}
			 */
			double t = Bukkit.getCurrentTick() - (int)LegacyCraft.getPlayerData(p.getUniqueId(), PlayerData.LAST_ATTACK);
			double cooldown = 1 / itemData.calculateSpeed() * 20;
			double dmgMul = 0.2 + Math.pow((t + 0.5) / cooldown, 2) * 0.8;
			if(dmgMul > 1) dmgMul = 1;
			Util.br("Cooldown Multiplier: " + dmgMul);
			
			e.setDamage(itemData.getStats().get(ItemStat.ATTACK) * dmgMul);
			
			if(attacked.isInvulnerable() || e.isCancelled()) {
				e.setDamage(0);
			}
			
			double damage = e.getFinalDamage();
			
			Util.br("Damage: " + damage);
			
			if(damage > 0) {
				
				//Apply Experience
				Experience playerExperience = (Experience) LegacyCraft.getPlayerData(p.getUniqueId(), PlayerData.EXPERIENCE);
				switch(itemData.getType()) {
				case SHORTSWORD:
				case LONGSWORD:
					playerExperience.addExperience((int) damage, ExperienceType.SWORDS);
					break;
				case BOW:
					playerExperience.addExperience((int) damage, ExperienceType.BOWS);
					break;
				default:
					break;
				}
				
				//Apply Enhancements
				Enhancement.applyHit(p, attacked, item, damage);
				LegacyCraft.setPlayerData(p.getUniqueId(), PlayerData.LAST_ATTACK, Bukkit.getCurrentTick());
			} else {
				Enhancement.applySwing(p, item);
				e.setCancelled(true);
			}
		}
	}
}
