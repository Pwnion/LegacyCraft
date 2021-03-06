package com.pwnion.legacycraft.items.enhancements.effects;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.items.enhancements.MeleeEnhancement;

public class Puncture implements MeleeEnhancement {

	private static final double PROC_RATE = 0.4; //Chance to trigger
	private static final double CONT_RATE = 0.8; //Chance to continue
	private static final int DAMAGE = 1; //Damage dealt
	private static final int DELAY = 20; //Delay between damages

	@Override
	public void onHit(ItemStack item, LivingEntity wielder, LivingEntity target, double damage) {
		if (Math.random() < PROC_RATE) {
			wielder.sendMessage("Opponent is bleeding"); // TODO: Choose colour and text
			int count = 1;
			while (Math.random() < CONT_RATE) count++;

			LegacyCraft.addTaskToBeCancelled(Bukkit.getServer().getScheduler().runTaskTimer(LegacyCraft.getPlugin(), new Runnable() {
				public void run() {
					//Puncture does not effect NoDamageTicks Invincibility but is still affected by it.
					int prev = target.getNoDamageTicks();
					target.damage(DAMAGE);
					target.setNoDamageTicks(prev);
				}
			}, DELAY, DELAY), DELAY * count + 1);
		}
		
	}

	@Override
	public void onEquip(ItemStack item, boolean initial) {
		// TODO Auto-generated method stub
		
	}

}
