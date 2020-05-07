package com.pwnion.legacycraft.levelling;

public enum ExperienceType {
	PLAYER("Player Level"), //Total player experience
	SWORDS("Swords"),
	BOWS("Bows");
	
	private final String name;
	
	private ExperienceType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
