package com.pwnion.legacycraft.abilities;

import org.bukkit.entity.Player;

import com.pwnion.legacycraft.abilities.proficiencies.*;

public enum Abilities {
	EARTH_PILLAR(HotbarAbility.Type.PROFICIENCY1, 6, new TerraVanguardProficiency1()),
	ICEBLOCK(HotbarAbility.Type.PROFICIENCY1, 8, new AquaVanguardProficiency1());
	
	private final HotbarAbility.Type type;
	private final int customModelData;
	private final HotbarAbility ability;
	
	private Abilities(HotbarAbility.Type type, int customModelData, HotbarAbility ability) {
		this.type = type;
		this.customModelData = customModelData;
		this.ability = ability;
	}
	
	public final String activate(Player p) {
		return ability.activate(p);
	}
	
	public final HotbarAbility.Type getType() {
		return type;
	}
	
	public final int getCustomModelData() {
		return customModelData;
	}
}
