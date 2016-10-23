package me.BadBones69.CrazyRunes.API;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import me.BadBones69.CrazyRunes.Api;
import me.BadBones69.CrazyRunes.Main;
import me.BadBones69.CrazyRunes.Controlers.RuneControl;

public class PlayerRunes {
	
	private static HashMap<Player, HashMap<Rune, Integer>> playerStats = new HashMap<Player, HashMap<Rune, Integer>>();
	private static HashMap<Player, HashMap<Rune, Boolean>> playerActivations = new HashMap<Player, HashMap<Rune, Boolean>>();
	
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
		}else{
			for(Rune rune : Main.runes.getRunes()){
				stats.put(rune, 0);
				activations.put(rune, false);
			}
		}
		playerStats.put(player, stats);
		playerActivations.put(player, activations);
		activateRunes(player);
	}
	
	public static void unLoadPlayer(Player player){
		String uuid = player.getUniqueId().toString();
		Main.settings.getData().set("Players." + uuid + ".Name", player.getName());
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
		playerStats.remove(player);
		playerActivations.remove(player);
	}
	
	public static void activateRunes(Player player){
		RuneControl.setMedic(player, playerStats.get(player).get(Rune.MEDIC));
		RuneControl.setSpeed(player, playerStats.get(player).get(Rune.SPEED));
		if(playerStats.get(player).get(Rune.SURVIVER) > 0){
			RuneControl.setSurviver(player, true);
		}else{
			RuneControl.setSurviver(player, false);
		}
		RuneControl.setTank(player, playerStats.get(player).get(Rune.TANK));
	}
	
	public static void deActivateRunes(Player player){
		RuneControl.setMedic(player, 0);
		RuneControl.setSpeed(player, 0);
		RuneControl.setSurviver(player, false);
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
		int credits = 0;
		for(PermissionAttachmentInfo Permission : player.getEffectivePermissions()){
			String perm = Permission.getPermission();
			if(perm.startsWith("crazyrunes.credits.")){
				perm=perm.replace("crazyrunes.credits.", "");
				if(Api.isInt(perm)){
					if(Integer.parseInt(perm) > credits){
						credits = Integer.parseInt(perm);
					}
				}
			}
		}
		return credits;
	}
	
	public static Integer getAvailableCredits(Player player){
		int available = 0;
		int used = 0;
		int max = getMaxCredits(player);
		for(Rune rune : Main.runes.getRunes()){
			used += getRuneLevel(player, rune);
		}
		available = (max - used);
		if(available < 0){
			available = 0;
		}
		return available;
	}
	
}