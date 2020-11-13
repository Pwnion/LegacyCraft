package com.pwnion.legacycraft.mobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.mobs.attacks.*;

/**
 * Name in consideration - EnemyEntity? - Mob? - LCEntity? -
 * 
 */
public class LCEntity {
	
	static final Random rnd = new Random();

	public enum LCEntityType {
		FIRE_ELEMENTAL(EntityType.ZOMBIE, 20, 20 * 2, new Dash(1, 20 * 5, 1));

		final EntityType model;
		final int health;
		final int attackSpeed;
		final Attack[] moveSet;

		LCEntityType(EntityType model, int health, int attackSpeed, Attack... moveSet) {
			this.model = model;
			this.health = health;
			this.attackSpeed = attackSpeed;
			this.moveSet = moveSet;
		}
	}

	private static final HashMap<Integer, LCEntity> activeEntities = new HashMap<>();
	private static final HashSet<Integer> attackingEntities = new HashSet<>();

	final LivingEntity entity;
	final LCEntityType type;
	int lastAttack = Bukkit.getCurrentTick();
	final HashMap<Attack, Integer> moves = new HashMap<>();

	public LCEntity(Location loc, LCEntityType type) {
		this.type = type;
		
		this.entity = (LivingEntity) loc.getWorld().spawnEntity(loc, type.model);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(type.health);
		entity.setHealth(type.health);
		
		for(Attack move : type.moveSet) {
			moves.put(move, Bukkit.getCurrentTick());
		}
		
		activeEntities.put(entity.getEntityId(), this);
	}
	
	public static void calculateAttacks() {
		for (Integer entityId : attackingEntities) {
			LCEntity lce = activeEntities.get(entityId);
			attack(lce);
		}
	}

	public static LCEntity get(Entity entity) {
		return activeEntities.get(entity.getEntityId());
	}
	
	public static void remove(Entity entity) {
		removeAttack(entity);
		activeEntities.remove(entity.getEntityId());
	}
	
	public static void addAttack(Entity entity) {
		LCEntity lce = get(entity);
		if (lce != null) {
			lce.lastAttack = Bukkit.getCurrentTick();
			attackingEntities.add(entity.getEntityId());
		}
	}
	
	public static void removeAttack(Entity entity) {
		attackingEntities.remove(entity.getEntityId());
	}
	
	public static void attack(LCEntity lcEntity) {
		if (lcEntity == null) {
			return;
		}
		if (Bukkit.getCurrentTick() - lcEntity.lastAttack < lcEntity.type.attackSpeed) {
			return;
		}
		HashMap<Attack, Location> validAttacks = new HashMap<>();
		for (Attack att : lcEntity.moves.keySet()) {
			if (Bukkit.getCurrentTick() - lcEntity.moves.get(att) < att.cooldown) {
				Util.br("Attack on cooldown, '" + (Bukkit.getCurrentTick() - lcEntity.moves.get(att) - att.cooldown) + "' remaining ticks");
				return;
			}
			Location valid = att.isValid(lcEntity.entity);
			if (valid != null) {
				validAttacks.put(att, valid);
			}
		}
		Attack att = getMove(validAttacks.keySet());
		if(att != null) {
			lcEntity.lastAttack = Bukkit.getCurrentTick();
			lcEntity.moves.put(att, Bukkit.getCurrentTick());
			att.target(lcEntity.entity, validAttacks.get(att));
		}
	}
	
	static Attack getMove(Collection<Attack> possibleMoves) {
		if(possibleMoves.isEmpty()) {
			return null;
		}
		Iterator<Attack> posbMovesItr = possibleMoves.iterator();
		int totalWeight = 0;
		for (Attack a : possibleMoves) {
			totalWeight += a.weight;
		}
		int i = 0;
		Attack att = null;
		for (int random = rnd.nextInt(totalWeight) + 1; random > 0; i++) {
			att = posbMovesItr.next();
			random -= att.weight;
		}
		return att;
	}
}
