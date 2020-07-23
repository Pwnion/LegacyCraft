package com.pwnion.legacycraft.items.enhancements.effects.melee;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.items.ItemType;
import com.pwnion.legacycraft.items.enhancements.Enhancement;

public class Relentless implements Enhancement {

	@Override
	public String getName() {
		return "Relentless";
	}
	
	@Override
	public ItemType getRestriction() {
		return ItemType.MELEE;
	}
	
	private int lastHit = Bukkit.getCurrentTick();
	private int consecHits = 0; //Consecutive hits
	
	private static final double DMG_PERCENT_ADD = 0.1; //adds 10% damage per consecutive hit
	private static final int MAX_DELAY = 40; //max delay allowed between damages

	@Override
	public void onHit(LivingEntity wielder, LivingEntity target, double damage) {
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
