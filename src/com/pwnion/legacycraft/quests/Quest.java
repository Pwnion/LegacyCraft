package com.pwnion.legacycraft.quests;

import java.util.ArrayList;

public class Quest {

	public Quest(String id, String name, String desc, ArrayList<Trigger> triggers, String nextQuestId) {
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.nextQuest = nextQuestId;
		this.triggers = triggers;
	}
	
	//TEMP
	public Quest(String id, String name, String desc, Trigger trigger) {
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.nextQuest = null;
		ArrayList<Trigger> triggers = new ArrayList<Trigger>();
		triggers.add(trigger);
		this.triggers = triggers;
	}

	private final String id;
	private final String name;
	private final String desc;
	private final String nextQuest;
	private final ArrayList<Trigger> triggers;
	
	//Possible reward system
	//private ArrayList<ItemStack> rewards = new ArrayList<ItemStack>();
	
	public String getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public String getNextQuest() {
		return nextQuest;
	}

	/**
	 * Gets a list of all the triggers attached to this quest.
	 * 
	 * @return
	 */
	public ArrayList<Trigger> getTriggers() {
		return triggers;
	}
	
	/**
	 * Gets the amount of triggers attached to this quest
	 * 
	 * @return
	 */
	public int getTriggerCount() {
		return triggers.size();
	}

	/**
	 * Gets a list of all the triggers of the given type attached to this quest
	 * 
	 * @param type
	 * @return
	 */
	public ArrayList<Trigger> getTriggers(TriggerType type) {
		ArrayList<Trigger> output = new ArrayList<Trigger>();
		for(Trigger trigger : triggers) {
			if(trigger.getType() == type) {
				output.add(trigger);
			}
		}
		return output;
	}

	/**
	 * gets the index of a trigger or -1;
	 * 
	 * @param trigger
	 * @return
	 */
	public int getIndex(Trigger trigger) {
		for(int i = 0; i < triggers.size(); i++) {
			if(trigger.equals(triggers.get(i))) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the finish condition of the trigger at the given index
	 * 
	 * @param index
	 * @return
	 */
	public int getCondition(int index) {
		return triggers.get(index).getFinishCondition();
	}
	
	/**
	 * Gets the progress required to complete all triggers.
	 * The sum of all trigger's finish condition.
	 * 
	 * @return
	 */
	public int getConditionOverall() {
		int out = 0;
		for(Trigger trigger : triggers) {
			out += trigger.getFinishCondition();
		}
		return out;
	}

	/**
	 * Checks if a Trigger of the given type is attached to this quest.
	 * 
	 * @param type
	 * @return
	 */
	public boolean hasTrigger(TriggerType type) {
		for(Trigger trigger : triggers) {
			if(trigger.getType() == type) {
				return true;
			}
		}
		return false;
	}

}
