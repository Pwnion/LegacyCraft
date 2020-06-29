package com.pwnion.legacycraft.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.items.ItemManager;
import com.pwnion.legacycraft.items.ItemStat;
import com.pwnion.legacycraft.items.enhancements.Enhancement;
import com.pwnion.legacycraft.items.enhancements.EnhancementType;

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
			Enhancement.apply(p, null, item, 0, EnhancementType.WEAPON_SWING);
		}
		
	}
}
