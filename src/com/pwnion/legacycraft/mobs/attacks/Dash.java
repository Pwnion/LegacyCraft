package com.pwnion.legacycraft.mobs.attacks;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.pwnion.legacycraft.Util;

public class Dash extends Attack {
	
	double speed;
	
	public Dash(int weight, int cooldown, double defaultSpeed) {
		super(weight, cooldown);
		this.speed = defaultSpeed;
	}

	@Override
	public void target(LivingEntity self, Location target) {
		target(self, target, speed);
	}
	
	public void target(LivingEntity self, Location target, double speed) {
		self.setVelocity(Util.vectorCalc(self, speed).add(new Vector(0, speed * 0.1, 0)));
	}

	@Override
	public Location isValid(LivingEntity self) {
		Collection<Player> players = self.getWorld().getNearbyPlayers(self.getLocation(), 10);
		return !players.isEmpty() ? self.getLocation() : null;
	}
}
