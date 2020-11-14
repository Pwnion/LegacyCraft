package com.pwnion.legacycraft.mobs.attacks;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.mobs.LCEntity;

public class Dash extends Attack {
	
	double speed;
	
	public Dash(int weight, int cooldown, double defaultSpeed) {
		super(weight, cooldown);
		this.speed = defaultSpeed;
	}

	@Override
	public void makeAttack(LivingEntity self, Location target) {
		target(self, target, speed);
	}
	
	public void target(LivingEntity self, Location target, double speed) {
		self.setVelocity(Util.vectorCalc(self, speed).add(new Vector(0, speed * 0.2, 0)));
	}

	@Override
	public Location getValidTarget(LCEntity self, Entity attacking) {
		return attacking == null ? null : self.entity.getLocation();
	}
}
