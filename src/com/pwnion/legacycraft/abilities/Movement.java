package com.pwnion.legacycraft.abilities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class Movement {
	public static final void launch(Entity e, Location loc, float horizontalMultiplier, float verticalMultiplier) {
		Vector launch = loc.toVector().subtract(e.getLocation().toVector());
		launch.multiply(-((1 / launch.length()) * horizontalMultiplier));
		launch.setY(verticalMultiplier);
		
		e.setVelocity(e.getVelocity().add(launch));
	}
}
