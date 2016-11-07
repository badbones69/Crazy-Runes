package me.BadBones69.CrazyRunes.API;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.BadBones69.CrazyRunes.Main;
import me.BadBones69.CrazyRunes.Controlers.RuneControl;

public class PlayerRunes {
	
	private static HashMap<Player, HashMap<Rune, Integer>> playerStats = new HashMap<Player, HashMap<Rune, Integer>>();
	private static HashMap<Player, HashMap<Rune, Boolean>> playerActivations = new HashMap<Player, HashMap<Rune, Boolean>>();
	private static HashMap<Player, Integer> maxCredits = new HashMap<Player, Integer>();
	
	public static void loadPlayer(Player player){
		String uuid = player.getUniqueId().toString();
		HashMap<Rune, Integer> stats = new HashMap<Rune, Integer>();
		HashMap<Rune, Boolean> activations = new HashMap<Rune, Boolean>();
		if(Main.settings.getData().contains("Players." + uuid)){
			for(Rune rune : Main.runes.getRunes()){
				stats.put(rune, Main.settings.getData().getInt("Players." + uuid + "." + rune.getName() + ".Level"));
				if(stats.get(rune) > 0){
					activations.put(rune, true);
				}else{
					activations.put(rune, false);
				}
			}
			maxCredits.put(player, Main.settings.getData().getInt("Players." + uuid + ".Max-Credits"));
		}else{
			for(Rune rune : Main.runes.getRunes()){
				stats.put(rune, 0);
				activations.put(rune, false);
			}
			maxCredits.put(player, Main.settings.getConfig().getInt("Settings.Default-Credits-Amount"));
		}
		playerStats.put(player, stats);
		playerActivations.put(player, activations);
		activateRunes(player);
	}
	
	public static void unLoadPlayer(Player player){
		String uuid = player.getUniqueId().toString();
		Main.settings.getData().set("Players." + uuid + ".Name", player.getName());
		Main.settings.getData().set("Players." + uuid + ".Max-Credits", maxCredits.get(player));
		for(Rune rune : Main.runes.getRunes()){
			Main.settings.getData().set("Players." + uuid + "." + rune.getName() + ".Level", playerStats.get(player).get(rune));
			if(playerStats.get(player).get(rune) > 0){
				Main.settings.getData().set("Players." + uuid + "." + rune.getName() + ".Active", true);
			}else{
				Main.settings.getData().set("Players." + uuid + "." + rune.getName() + ".Active", false);
			}
		}
		Main.settings.saveData();
		deActivateRunes(player);
		maxCredits.remove(player);
		playerStats.remove(player);
		playerActivations.remove(player);
	}
	
	public static void activateRunes(Player player){
		RuneControl.setMedic(player, playerStats.get(player).get(Rune.MEDIC));
		RuneControl.setSpeed(player, playerStats.get(player).get(Rune.SPEED));
		if(playerStats.get(player).get(Rune.SURVIVOR) > 0){
			RuneControl.setSurvivor(player, true);
		}else{
			RuneControl.setSurvivor(player, false);
		}
		RuneControl.setTank(player, playerStats.get(player).get(Rune.TANK));
	}
	
	public static void deActivateRunes(Player player){
		RuneControl.setMedic(player, 0);
		RuneControl.setSpeed(player, 0);
		RuneControl.setSurvivor(player, false);
		RuneControl.setTank(player, 0);
	}
	
	public static Integer getRuneLevel(Player player, Rune rune){
		return playerStats.get(player).get(rune);
	}
	
	public static Boolean getRuneActivation(Player player, Rune rune){
		return playerActivations.get(player).get(rune);
	}
	
	public static void setRuneLevel(Player player, Rune rune, Integer level){
		playerStats.get(player).put(rune, level);
	}
	
	public static void setRuneActivation(Player player, Rune rune, Boolean toggle){
		playerActivations.get(player).put(rune, toggle);
	}
	
	public static Integer getMaxCredits(Player player){
		return maxCredits.get(player);
	}
	
	public static void addCredits(Player player, int amount){
		maxCredits.put(player, maxCredits.get(player)+amount);
	}
	
	public static void removeCredits(Player player, int amount){
		amount = maxCredits.get(player)-amount;
		if(amount < 0){
			amount = 0;
		}
		maxCredits.put(player, amount);
	}
	
	public static Integer getAvailableCredits(Player player){
		int available = 0;
		int used = 0;
		int max = getMaxCredits(player);
		for(Rune rune : Main.runes.getRunes()){
			for(int i = 1; i <= getRuneLevel(player, rune); i++){
				used += Main.runes.getLevelCost(rune, i);
			}
		}
		available = (max - used);
		if(available < 0){
			available = 0;
		}
		return available;
	}
	
}