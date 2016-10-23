package me.BadBones69.CrazyRunes.API;

import java.util.HashMap;
import java.util.List;

import me.BadBones69.CrazyRunes.Main;

public class CrazyRunes {
	
	static CrazyRunes instance = new CrazyRunes();
	
	HashMap<Rune, String> names = new HashMap<Rune, String>();
	HashMap<Rune, String> customName = new HashMap<Rune, String>();
	HashMap<Rune, List<String>> description = new HashMap<Rune, List<String>>();
	HashMap<Rune, List<String>> levelDescription = new HashMap<Rune, List<String>>();
	HashMap<Rune, Boolean> active = new HashMap<Rune, Boolean>();
	HashMap<Rune, Integer> slot = new HashMap<Rune, Integer>();
	HashMap<Rune, String> material = new HashMap<Rune, String>();
	
	public static CrazyRunes getInstance(){
		return instance;
	}
	
	public void load(){
		names.clear();
		customName.clear();
		description.clear();
		active.clear();
		slot.clear();
		material.clear();
		for(Rune rune : getRunes()){
			if(Main.settings.getRunes().contains("Runes." + rune.getName())){
				String name = rune.getName();
				names.put(rune, name);
				customName.put(rune, Main.settings.getRunes().getString("Runes." + name + ".Name"));
				description.put(rune, Main.settings.getRunes().getStringList("Runes." + name + ".Description"));
				levelDescription.put(rune, Main.settings.getRunes().getStringList("Runes." + name + ".Level-Description"));
				active.put(rune, Main.settings.getRunes().getBoolean("Runes." + name + ".Active"));
				slot.put(rune, Main.settings.getRunes().getInt("Runes." + name + ".Slot") - 1);
				material.put(rune, Main.settings.getRunes().getString("Runes." + name + ".Item"));
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