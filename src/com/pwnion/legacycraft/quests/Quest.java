package com.pwnion.legacycraft.quests;

import java.util.ArrayList;

public class Quest {

	public Quest(String name, String desc, ArrayList<Trigger> triggers) {
		this.name = name;
		this.desc = desc;
		this.nextQuest = nextQuest;
		this.triggers = triggers;
	}

	private String name;
	private String desc;
	private String nextQuest;
	
	public String getName() {
		return name;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public String getNextQuest() {
		return nextQuest;
	}

	ArrayList<Trigger> triggers = new ArrayList<Trigger>();

	public ArrayList<Trigger> getTriggers() {
		return triggers;
	}

	public ArrayList<Trigger> getTriggers(TriggerType name) {
		ArrayList<Trigger> output = new ArrayList<Trigger>();
		for(Trigger trigger : triggers) {
			if(trigger.type == name) {
				output.add(trigger);
			}
		}
		return output;
	}

	public int getIndex(Trigger trigger) {
		for(int i = 0; i < triggers.size(); i++) { //GET CHECKED
			if(trigger.equals(triggers.get(i))) {
				return i;
			}
		}
		return -1;
	}

	public int getCondition(int index) {
		return triggers.get(index).getFinishCondition();
	}

	public boolean hasTrigger(TriggerType item) {
		for(Trigger trigger : triggers) {
			if(trigger.getName() == item) {
				return true;
			}
		}

		return false;
	}

}
