package me.BadBones69.CrazyRunes.API;

public enum Rune {
	
	MEDIC("Medic", 3, 1),
	TANK("Tank", 3, 3),
	PYRO("Pyro", 5, 10),
	SURVIVOR("Survivor", 1, 1),
	LEECH("Leech", 4, 15),
	REBORN("Reborn", 3, 10),
	ROCKET("Rocket", 4, 15),
	SPEED("Speed", 2, 50);
	
	private String Name;
	private Integer MaxLevel;
	private Integer Power;
	
	private Rune(String name, Integer maxLevel, Integer power){
		Name = name;
		MaxLevel = maxLevel;
		Power = power;
	}
	
	public String getName(){
		return Name;
	}
	
	public Integer getMaxLevel(){
		return MaxLevel;
	}
	
	public Integer getPower(){
		return Power;
	}
	
}