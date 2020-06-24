package com.pwnion.legacycraft.abilities.enhancements.effects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.abilities.enhancements.Enhancement;
import com.pwnion.legacycraft.abilities.enhancements.EnhancementType;

public class Relentless implements Enhancement {

	@Override
	public String getName() {
		return "Relentless";
	}

	@Override
	public EnhancementType getType() {
		return EnhancementType.WeaponHit;
	}
	
	private int lastHit = Bukkit.getCurrentTick();
	private int consecHits = 0; //Consecutive hits
	
	private static final double DMG_PERCENT_ADD = 0.1; //adds 10% damage per consecutive hit
	private static final int MAX_DELAY = 20; //max delay allowed between damages

	@Override
	public void apply(Entity wielder, Damageable target, double damage) {
		int curTime = Bukkit.getCurrentTick();
		if(curTime - lastHit <= MAX_DELAY) {
			consecHits++;
			target.damage(damage * DMG_PERCENT_ADD * consecHits);
			wielder.sendMessage("COMBO x" + consecHits);
		} else {
			consecHits = 0;
		}
		lastHit = Bukkit.getCurrentTick();
	}

}
