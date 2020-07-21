package com.pwnion.legacycraft.items;

import com.pwnion.legacycraft.Util;

public enum ItemStat {
	ATTACK(0, 1, 1),
	SPEED(0.2, 1, 1),
	RANGE(1, 1, 1);
	
	private final double absMin;
	private final int min;
	private final int defaultt;
	
	ItemStat(double absMin, int min, int defaultt) {
		this.absMin = absMin;
		this.min = min;
		this.defaultt = defaultt;
	}
	
	public double getAbsMin() {
		return absMin;
	}
	
	public int getMin() {
		return min;
	}
	
	public int getDefault() {
		return defaultt;
	}
	
	@Override
	public String toString() {
		return Util.toTitleCase(this.name().replace("_", " "));
	}
}
