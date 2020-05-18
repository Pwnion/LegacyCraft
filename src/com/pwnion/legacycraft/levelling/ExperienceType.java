package com.pwnion.legacycraft.levelling;

public enum ExperienceType {
	PLAYER("Player Level", "player"), //Total player experience
	SWORDS("Swords", "sword"),
	BOWS("Bows", "bow");
	
	private final String name;
	private final String identifier;
	
	private ExperienceType(String name, String identifier) {
		this.name = name;
		this.identifier = identifier;
	}
	
	public String getName() {
		return name;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public static ExperienceType fromIdentifier(String identifier) {
		for(ExperienceType experienceType : ExperienceType.values()) {
			if(experienceType.getIdentifier() == identifier) {
				return experienceType;
			}
		}
		return null;
	}
}
