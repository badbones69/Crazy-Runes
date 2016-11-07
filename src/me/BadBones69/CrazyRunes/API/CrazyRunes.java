package me.BadBones69.CrazyRunes.API;

import java.util.HashMap;
import java.util.List;

import me.BadBones69.CrazyRunes.Main;

public class CrazyRunes {
	
	public static CrazyRunes instance = new CrazyRunes();
	
	private HashMap<Rune, String> names = new HashMap<Rune, String>();
	private HashMap<Rune, Integer> slot = new HashMap<Rune, Integer>();
	private HashMap<Rune, Boolean> active = new HashMap<Rune, Boolean>();
	private HashMap<Rune, String> material = new HashMap<Rune, String>();
	private HashMap<Rune, String> customName = new HashMap<Rune, String>();
	private HashMap<Rune, List<String>> description = new HashMap<Rune, List<String>>();
	private HashMap<Rune, List<String>> levelDescription = new HashMap<Rune, List<String>>();
	private HashMap<Rune, HashMap<Integer, Integer>> levelCost = new HashMap<Rune, HashMap<Integer, Integer>>();
	
	public static CrazyRunes getInstance(){
		return instance;
	}
	
	public void load(){
		slot.clear();
		names.clear();
		active.clear();
		material.clear();
		levelCost.clear();
		customName.clear();
		description.clear();
		for(Rune rune : getRunes()){
			if(Main.settings.getRunes().contains("Runes." + rune.getName())){
				String name = rune.getName();
				names.put(rune, name);
				HashMap<Integer, Integer> cost = new HashMap<Integer, Integer>();
				for(int i = 1; i <= rune.getMaxLevel(); i++){
					cost.put(i, Main.settings.getRunes().getInt("Runes." + name + ".Level-Costs." + i));
				}
				levelCost.put(rune, cost);
				slot.put(rune, Main.settings.getRunes().getInt("Runes." + name + ".Slot") - 1);
				active.put(rune, Main.settings.getRunes().getBoolean("Runes." + name + ".Active"));
				material.put(rune, Main.settings.getRunes().getString("Runes." + name + ".Item"));
				customName.put(rune, Main.settings.getRunes().getString("Runes." + name + ".Name"));
				description.put(rune, Main.settings.getRunes().getStringList("Runes." + name + ".Description"));
				levelDescription.put(rune, Main.settings.getRunes().getStringList("Runes." + name + ".Level-Description"));
			}
		}
	}
	
	public Rune[] getRunes(){
		return Rune.values();
	}
	
	public String getCustomName(Rune rune){
		return customName.get(rune);
	}
	
	public List<String> getDescription(Rune rune){
		return description.get(rune);
	}
	
	public List<String> getLevelDescription(Rune rune){
		return levelDescription.get(rune);
	}
	
	public Boolean isActive(Rune rune){
		return active.get(rune);
	}
	
	public Integer getSlot(Rune rune){
		return slot.get(rune);
	}
	
	public String getMaterial(Rune rune){
		return material.get(rune);
	}
	
	public Integer getLevelCost(Rune rune, Integer level){
		return levelCost.get(rune).get(level);
	}
	
	public Boolean isRune(String name){
		for(Rune rune : getRunes()){
			if(rune.getName().equalsIgnoreCase(name)){
				return true;
			}
			if(getCustomName(rune).equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
	}
	
	public Rune getFromName(String name){
		for(Rune rune : getRunes()){
			if(rune.getName().equalsIgnoreCase(name)){
				return rune;
			}
			if(getCustomName(rune).equalsIgnoreCase(name)){
				return rune;
			}
		}
		return null;
	}
	
}