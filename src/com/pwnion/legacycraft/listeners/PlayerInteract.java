package com.pwnion.legacycraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.items.ItemManager;
import com.pwnion.legacycraft.items.ItemStat;
import com.pwnion.legacycraft.items.enhancements.Enhancement;

public class PlayerInteract implements Listener {
	
	private static final double RANGE_INCREMENT = 0.5;

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack item = p.getInventory().getItemInMainHand();
		
		//Handle item range
		if(e.getAction() == Action.LEFT_CLICK_AIR) {
			int range = ItemManager.getStats(item).get(ItemStat.RANGE);
			if(range > 1) {
				Entity target = p.getTargetEntity((int) Math.ceil(range * RANGE_INCREMENT + 3));
				if(target instanceof LivingEntity) {
					LivingEntity le = (LivingEntity) target;
					double dis = p.getLocation().distanceSquared(target.getLocation());
					double maxDis = Math.pow(range * RANGE_INCREMENT + 3, 2);
					if(dis < maxDis && dis > Math.pow(3, 2)) {
						le.damage(1, p); //triggers damage event where dmg calc is done
					}
				}
			}
		}
		
		//Handle swing enhancements 
		if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
			Enhancement.applySwing(p, item);
			
			//Possible Horizontal Enhancement
			/*
			Vector vec = p.getLocation().getDirection().add(new Vector(0, 0.2, 0));
			Location initial = p.getLocation().add(0, 0.5, 0);

			for(int i = 1; i < 7; i++) {
				final int mul = i;
				Bukkit.getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
					public void run() {
						final int mul2 = mul;
						p.getWorld().spawnParticle(Particle.SWEEP_ATTACK, initial.clone().add(vec.clone().multiply(mul2)), 1, 0, 0, 0, 0.5);
					}
				}, i * 3);
			} //*/
		}
		
	}
}
