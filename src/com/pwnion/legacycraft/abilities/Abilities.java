package com.pwnion.legacycraft.abilities;

import org.bukkit.entity.Player;

import com.pwnion.legacycraft.abilities.proficiencies.*;

public enum Abilities {
	EARTH_PILLAR(19, new TerraVanguardProficiency1()),
	ICEBLOCK(23, new AquaVanguardProficiency1());
	
	private final int customModelData;
	private final IHotbarActivatedAbility ability;
	
	private Abilities(int customModelData, IHotbarActivatedAbility ability) {
		this.customModelData = customModelData;
		this.ability = ability;
	}
	
	public final String activate(Player p) {
		return ability.activate(p);
	}
	
	public final int getCustomModelData() {
		return customModelData;
	}
}
