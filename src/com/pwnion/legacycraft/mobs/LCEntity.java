package com.pwnion.legacycraft.mobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import com.destroystokyo.paper.ParticleBuilder;
import com.pwnion.legacycraft.Util;
import com.pwnion.legacycraft.mobs.attacks.*;

/**
 * Name in consideration - EnemyEntity? - Mob? - LCEntity? -
 * 
 */
public class LCEntity {
	
	static final Random rnd = new Random();
	
	static final ParticleBuilder[] prebuilt;
	static {
		
		ParticleBuilder fireElemental = new ParticleBuilder(Particle.FLAME);
		fireElemental.count(2);
		fireElemental.force(true);
		fireElemental.extra(0);
		fireElemental.offset(0, 0, 0);
		
		prebuilt = new ParticleBuilder[]{fireElemental};
	}

	public enum LCEntityType {
		FIRE_ELEMENTAL(EntityType.ZOMBIE, 20, 20 * 2, new Dash(1, 20 * 5, 1), 
				new Projectile(3, 20, prebuilt[0], 1, 5, 10, -0.5, null, 0.5, 5, 5), 
				new Wave(3, 20, prebuilt[0], 1, 5, 10, 0.5, null, 0.5, 5, 5, 360, 10));

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

	/**
	 * HashMap of Entity ID, to LCEntity object
	 */
	private static final HashMap<Integer, LCEntity> activeEntities = new HashMap<>();
	
	
	/**
	 * HashMap of Entity ID of LCEntity, and Entity that we are attacking.
	 */
	private static final HashMap<Integer, Entity> attackingEntities = new HashMap<>();

	public final LivingEntity entity; // self
	public final LCEntityType type;
	public int lastAttack = Bukkit.getCurrentTick();
	public final HashMap<Attack, Integer> moves = new HashMap<>();

	// create and summon an LCEntity at this location
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
	
	// calculate all attacks for this tick
	public static void calculateAttacks() {
		for (Integer entityId : attackingEntities.keySet()) {
			LCEntity lce = activeEntities.get(entityId);
			calcAttack(lce);
		}
	}

	// get the LCEntity for this entity or null
	public static LCEntity get(Entity entity) {
		return activeEntities.get(entity.getEntityId());
	}
	
	// this entity no longer exists
	public static void remove(Entity entity) {
		removeAttacking(entity);
		activeEntities.remove(entity.getEntityId());
	}
	
	// this entity just started attacking this target
	public static void addAttacking(Entity entity, Entity target) {
		LCEntity lce = get(entity);
		if (lce != null) {
			lce.lastAttack = Bukkit.getCurrentTick();
			attackingEntities.put(entity.getEntityId(), target);
		}
	}
	
	// this entity is no longer attacking
	public static void removeAttacking(Entity entity) {
		attackingEntities.remove(entity.getEntityId());
	}
	
	// get the entity that this LCEntity is attacking
	public static Entity getAttacking(Entity self) {
		return attackingEntities.get(self.getEntityId());
	}
	
	// calculates and executes an attack
	public static void calcAttack(LCEntity lcEntity) {
		if (lcEntity == null) {
			return;
		}
		if (lcEntity.lastAttack + lcEntity.type.attackSpeed > Bukkit.getCurrentTick()) {
			return;
		}
		HashMap<Attack, Location> validAttacks = new HashMap<>();
		for (Attack att : lcEntity.moves.keySet()) {
			if (lcEntity.moves.get(att) + att.cooldown > Bukkit.getCurrentTick()) { // last attack + cooldown > now
				Util.br("Attack on cooldown, '" + (lcEntity.moves.get(att) + att.cooldown - Bukkit.getCurrentTick()) + "' remaining ticks");
				continue;
			}
			Location valid = att.getValidTarget(lcEntity.entity);
			if (valid != null) {
				validAttacks.put(att, valid);
			}
		}
		Attack att = getMove(validAttacks.keySet());
		if(att != null) {
			lcEntity.lastAttack = Bukkit.getCurrentTick(); // entity last attack for attack speed
			lcEntity.moves.put(att, Bukkit.getCurrentTick()); // move specific last attack for cooldown
			att.makeAttack(lcEntity.entity, validAttacks.get(att));
		}
	}
	
	// gets a random move (weighted)
	static Attack getMove(Collection<Attack> possibleMoves) {
		if(possibleMoves.isEmpty()) {
			return null;
		}
		Iterator<Attack> posbMovesItr = possibleMoves.iterator();
		int totalWeight = 0;
		for (Attack a : possibleMoves) {
			totalWeight += a.weight;
		}
		Attack att = null;
		for (int random = rnd.nextInt(totalWeight) + 1; random > 0; random -= att.weight) {
			att = posbMovesItr.next();
		}
		return att;
	}
}
