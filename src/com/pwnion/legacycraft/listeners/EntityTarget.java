package com.pwnion.legacycraft.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.mobs.LCEntity;

public class EntityTarget implements Listener {

	@EventHandler
	public void onEntityTarget(EntityTargetEvent e) {
		Entity entity = e.getEntity();
		Entity target = e.getTarget();
		
		Util.br("Entity Target");
		
		switch (e.getReason()) {
		case FORGOT_TARGET:
		case TARGET_DIED:
			LCEntity.removeAttacking(entity);
			break;
		case CLOSEST_PLAYER:
		case COLLISION:
		case CUSTOM:
		case OWNER_ATTACKED_TARGET:
		case RANDOM_TARGET:
		case REINFORCEMENT_TARGET:
		case TARGET_ATTACKED_ENTITY:
		case TARGET_ATTACKED_NEARBY_ENTITY:
		case TARGET_ATTACKED_OWNER:
			LCEntity.addAttacking(entity, target);
			break;
		default:
			break;
		}
	}

}
