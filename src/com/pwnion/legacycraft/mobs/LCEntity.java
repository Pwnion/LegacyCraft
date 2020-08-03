package com.pwnion.legacycraft.mobs;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/**
 * Name in consideration
 *  - EnemyEntity?
 *  - Mob?
 *  - LCEntity?
 *  - 
 * 
 */
public class LCEntity {
	
	public enum LCEntityType {
		ZOMBIE(EntityType.ZOMBIE, 20);
		
		final EntityType model;
		final int health;
		
		LCEntityType(EntityType model, int health) {
			this.model = model;
			this.health = health;
		}
	}
	
	private static final HashMap<Integer, LCEntity> activeEntities = new HashMap<Integer, LCEntity>(); 
	
	LivingEntity entity;
	
	public LCEntity(LivingEntity entity) {
		this.entity = entity;
		activeEntities.put(entity.getEntityId(), this);
	}
	
	public LCEntity(Location loc, LCEntityType type) {
		this.entity = (LivingEntity) loc.getWorld().spawnEntity(loc, type.model);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(type.health);
		activeEntities.put(entity.getEntityId(), this);
	}
	
	public static LCEntity get(Entity entity) {
		return activeEntities.get(entity.getEntityId());
	}
}
