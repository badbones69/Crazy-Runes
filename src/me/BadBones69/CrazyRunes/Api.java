package me.BadBones69.CrazyRunes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import me.BadBones69.CrazyRunes.MultiSupport.FactionsSupport;
import me.BadBones69.CrazyRunes.MultiSupport.FactionsUUID;
import me.BadBones69.CrazyRunes.MultiSupport.FeudalSupport;
import me.BadBones69.CrazyRunes.MultiSupport.WorldGuard;

public class Api {
	public static String getPrefix(){
		return color(Main.settings.getConfig().getString("Settings.Prefix"));
	}
	public static String color(String msg){
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		return msg;
	}
	public static String removeColor(String msg){
		msg = ChatColor.stripColor(msg);
		return msg;
	}
	public static ItemStack makeItem(String type){
		int ty = 0;
		if(type.contains(":")){
			String[] b = type.split(":");
			type = b[0];
			ty = Integer.parseInt(b[1]);
		}
		Material m = Material.matchMaterial(type);
		ItemStack item = new ItemStack(m, 1, (short) ty);
		return item;
	}
	public static ItemStack makeItem(String type, int amount, String name){
		int ty = 0;
		if(type.contains(":")){
			String[] b = type.split(":");
			type = b[0];
			ty = Integer.parseInt(b[1]);
		}
		Material m = Material.matchMaterial(type);
		ItemStack item = new ItemStack(m, amount, (short) ty);
		ItemMeta me = item.getItemMeta();
		me.setDisplayName(color(name));
		item.setItemMeta(me);
		return item;
	}
	public static ItemStack makeItem(String type, int amount, String name, List<String> lore){
		ArrayList<String> l = new ArrayList<String>();
		int ty = 0;
		if(type.contains(":")){
			String[] b = type.split(":");
			type = b[0];
			ty = Integer.parseInt(b[1]);
		}
		Material m = Material.matchMaterial(type);
		ItemStack item = new ItemStack(m, amount, (short) ty);
		ItemMeta me = item.getItemMeta();
		me.setDisplayName(color(name));
		for(String L:lore)l.add(color(L));
		me.setLore(l);
		item.setItemMeta(me);
		return item;
	}
	public static ItemStack makeItem(Material material, int amount, int type, String name){
		ItemStack item = new ItemStack(material, amount, (short) type);
		ItemMeta m = item.getItemMeta();
		m.setDisplayName(color(name));
		item.setItemMeta(m);
		return item;
	}
	public static ItemStack makeItem(Material material, int amount, int type, String name, List<String> lore){
		ArrayList<String> l = new ArrayList<String>();
		ItemStack item = new ItemStack(material, amount, (short) type);
		ItemMeta m = item.getItemMeta();
		m.setDisplayName(color(name));
		for(String L:lore)l.add(color(L));
		m.setLore(l);
		item.setItemMeta(m);
		return item;
	}
	public static ItemStack makeItem(Material material, int amount, int type, String name, List<String> lore, Map<Enchantment, Integer> enchants){
		ItemStack item = new ItemStack(material, amount, (short) type);
		ItemMeta m = item.getItemMeta();
		m.setDisplayName(name);
		m.setLore(lore);
		item.setItemMeta(m);
		item.addUnsafeEnchantments(enchants);
		return item;
	}
	public static ItemStack addLore(ItemStack item, String i){
		ArrayList<String> lore = new ArrayList<String>();
		ItemMeta m = item.getItemMeta();
		if(item.getItemMeta().hasLore()){
			lore.addAll(item.getItemMeta().getLore());
		}
		lore.add(i);
		m.setLore(lore);
		item.setItemMeta(m);
		return item;
	}
	public static boolean isInt(String s) {
	    try {
	        Integer.parseInt(s);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	public static Player getPlayer(String name){
		return Bukkit.getServer().getPlayer(name);
	}
	public static Location getLoc(Player player){
		return player.getLocation();
	}
	public static void runCMD(Player player, String CMD){
		player.performCommand(CMD);
	}
	public static boolean isOnline(String name, CommandSender p){
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(player.getName().equalsIgnoreCase(name)){
				return true;
			}
		}
		p.sendMessage(color("&cThat player is not online at this time."));
		return false;
	}
	public static boolean hasPermission(Player player, String perm){
		if(!player.hasPermission("CrazyRunes." + perm)){
			player.sendMessage(color("&cYou need permission to use this command."));
			return false;
		}
		return true;
	}
	public static boolean hasPermission(CommandSender sender, String perm){
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(!player.hasPermission("CrazyRunes." + perm)){
				player.sendMessage(color("&cYou need permission to use this command."));
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	}
	public static boolean allowsPVP(Location loc){
		if(Bukkit.getServer().getPluginManager().getPlugin("WorldEdit")!=null&&Bukkit.getServer().getPluginManager().getPlugin("WorldGuard")!=null){
			if(WorldGuard.allowsPVP(loc))return true;
			if(!WorldGuard.allowsPVP(loc))return false;
		}
		return true;
	}
	public static boolean hasFactions(){
		if(Bukkit.getServer().getPluginManager().getPlugin("Factions")!=null){
			return true;
		}
		return false;
	}
	public static boolean hasFeudal(){
		if(Bukkit.getServer().getPluginManager().getPlugin("Feudal")!=null){
			return true;
		}
		return false;
	}
	public static boolean inTerritory(Player player){
		Plugin factions = Bukkit.getServer().getPluginManager().getPlugin("Factions");
		if(factions!=null){
			if(factions.getDescription().getAuthors().contains("drtshock")){
				if(FactionsUUID.inTerritory(player))return true;
				if(!FactionsUUID.inTerritory(player))return false;
			}
			if(factions.getDescription().getWebsite()!=null){
				if(factions.getDescription().getWebsite().equalsIgnoreCase("https://www.massivecraft.com/factions")){
					if(FactionsSupport.inTerritory(player))return true;
					if(!FactionsSupport.inTerritory(player))return false;
				}
			}
		}
		if(Bukkit.getServer().getPluginManager().getPlugin("Feudal")!=null){
			if(FeudalSupport.inTerritory(player)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	public static boolean isFriendly(Entity P, Entity O){
		if(P instanceof Player&&O instanceof Player){
			Player player = (Player) P;
			Player other = (Player) O;
			Plugin factions = Bukkit.getServer().getPluginManager().getPlugin("Factions");
			if(factions!=null){
				if(factions.getDescription().getAuthors().contains("drtshock")){
					if(FactionsUUID.isFriendly(player, other))return true;
					if(!FactionsUUID.isFriendly(player, other))return false;
				}
				if(factions.getDescription().getWebsite()!=null){
					if(factions.getDescription().getWebsite().equalsIgnoreCase("https://www.massivecraft.com/factions")){
						if(FactionsSupport.isFriendly(player, other))return true;
						if(!FactionsSupport.isFriendly(player, other))return false;
					}
				}
			}
			if(Bukkit.getServer().getPluginManager().getPlugin("Feudal")!=null){
				if(FeudalSupport.isFrendly(player, other)){
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}
	public static boolean chance(int min, int max){
		if(max==min)return true;
		Random number = new Random();
		int chance = 1 + number.nextInt(max);
		if(chance>=1&&chance<=min){
			return true;
		}
		return false;
	}
}