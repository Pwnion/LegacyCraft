package com.pwnion.legacycraft.listeners;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.items.ItemData;
import com.pwnion.legacycraft.items.ItemManager;
import com.pwnion.legacycraft.items.Stats;
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
		Entity attacker = e.getDamager();
		//LivingEntity attacked = (LivingEntity) e.getEntity();
		
		if(attacker instanceof Player && e.getEntity() instanceof LivingEntity) {
			Player p = (Player) attacker;
			LivingEntity attacked = (LivingEntity) e.getEntity();
			ItemStack item = p.getInventory().getItemInMainHand();
			
			/**
			 * Calculate attack cooldown (no weapon switching) 
			 * {@link https://minecraft.gamepedia.com/Damage#Attack_cooldown}
			 */
			double t = Bukkit.getCurrentTick() - (int)LegacyCraft.getPlayerData(p.getUniqueId(), PlayerData.LAST_ATTACK);
			double cooldown = 1 / ItemManager.getItemData(item).calculateSpeed() * 20;
			double dmgMul = 0.2 + Math.pow((t + 0.5) / cooldown, 2) * 0.8;
			if(dmgMul > 1) dmgMul = 1;
			Util.br("Cooldown Multiplier: " + dmgMul);
			
			e.setDamage(ItemManager.getStats(item).get(Stats.ATTACK) * dmgMul);
			
			if(attacked.isInvulnerable() || e.isCancelled()) {
				e.setDamage(0);
			}
			
			double damage = e.getFinalDamage();
			
			Util.br("Damage: " + damage);
			
			if(damage > 0) {
				if(SWORDS.contains(item.getType())) { //If sword add sword experience equal to damage
					Experience playerExperience = (Experience) LegacyCraft.getPlayerData(p.getUniqueId(), PlayerData.EXPERIENCE);
					playerExperience.addExperience((int) damage, ExperienceType.SWORDS);
				}
				Enhancement.apply(p, attacked, item, damage, EnhancementType.WEAPON_HIT);
				LegacyCraft.setPlayerData(p.getUniqueId(), PlayerData.LAST_ATTACK, Bukkit.getCurrentTick());
			} else {
				Enhancement.apply(p, null, item, 0, EnhancementType.WEAPON_SWING);
			}
		}
	}
}
