package com.pwnion.legacycraft.items.enhancements.effects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import com.pwnion.legacycraft.LegacyCraft;
import com.pwnion.legacycraft.items.enhancements.Enhancement;
import com.pwnion.legacycraft.items.enhancements.EnhancementType;

public class Puncture implements Enhancement {

	@Override
	public String getName() {
		return "Puncture";
	}

	@Override
	public EnhancementType getType() {
		return EnhancementType.WEAPON_HIT;
	}
	
	private static final double PROC_RATE = 0.4; //Chance to trigger
	private static final double CONT_RATE = 0.8; //Chance to continue
	private static final int DAMAGE = 1; //Damage dealt
	private static final int DELAY = 20; //Delay between damages

	@Override
	public void apply(Entity wielder, Damageable target, double damage) {
		if (Math.random() < PROC_RATE) {
			wielder.sendMessage("Opponent is bleeding"); // TODO: Choose colour and text
			int count = 1;
			while (Math.random() < CONT_RATE) count++;

			LegacyCraft.addTaskToBeCancelled(Bukkit.getServer().getScheduler().runTaskTimer(LegacyCraft.getPlugin(), new Runnable() {
				public void run() {
					target.damage(DAMAGE);
				}
			}, DELAY, DELAY), DELAY * count + 1);
		}
		
	}

}
