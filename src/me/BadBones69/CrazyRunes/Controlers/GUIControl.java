package me.BadBones69.CrazyRunes.Controlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.BadBones69.CrazyRunes.Api;
import me.BadBones69.CrazyRunes.Main;
import me.BadBones69.CrazyRunes.API.PlayerRunes;
import me.BadBones69.CrazyRunes.API.Rune;

public class GUIControl implements Listener{
	
	HashMap<Player, Rune> pickedRune = new HashMap<Player, Rune>();
	
	public static void openMain(Player player){
		Inventory inv = Bukkit.createInventory(null, 45, Api.color(Main.settings.getConfig().getString("Settings.Inventory-Name")));
		for(Rune rune : Main.runes.getRunes()){
			if(Main.runes.isActive(rune)){
				List<String> Lore = new ArrayList<String>();
				for(String lore : Main.runes.getDescription(rune)){
					Lore.add(lore.replaceAll("%Level%", PlayerRunes.getRuneLevel(player, rune) + "").replaceAll("%level%", PlayerRunes.getRuneLevel(player, rune) + ""));
				}
				ItemStack item = Api.makeItem(Main.runes.getMaterial(rune), 1, "&6&l" + Main.runes.getCustomName(rune), Lore);
				inv.setItem(Main.runes.getSlot(rune), item);
			}
		}
		ItemStack runes = Api.makeItem("388", 1, "&2&lCredits", Arrays.asList("&6Available: &7" + PlayerRunes.getAvailableCredits(player)));
		inv.setItem(22, runes);
		player.openInventory(inv);
	}
	
	public static void openLeveler(Player player, Rune rune){
		Inventory inv = Bukkit.createInventory(null, 9, Api.color(Main.settings.getConfig().getString("Settings.Choose-Level")));
		Integer max = rune.getMaxLevel();
		ItemStack toggler = null;
		if(PlayerRunes.getRuneActivation(player, rune)){
			toggler = Api.makeItem("165", 1, "&6&lActivated", Arrays.asList("&7Click to deactivate."));
		}else{
			toggler = Api.makeItem("152", 1, "&4&lDeactivated", Arrays.asList("&7Click a level to activate."));
		}
		inv.setItem(0, toggler);
		int total = 0;
		for(int i = 1; i <= max; i++){
			ItemStack block = null;
			List<String> Lore = new ArrayList<String>();
			total += Main.runes.getLevelCost(rune, i);
			for(String lore : Main.runes.getLevelDescription(rune)){
				Lore.add(lore.replaceAll("%Power%", rune.getPower() * i + "").replaceAll("%power%", rune.getPower() * i + "")
						.replaceAll("%Level%", i+ "").replaceAll("%level%", i+ "")
						.replaceAll("%Cost%", Main.runes.getLevelCost(rune, i)+ "").replaceAll("%cost%", Main.runes.getLevelCost(rune, i)+ "")
						.replaceAll("%TotalCost%", total+ "").replaceAll("%totalcost%", total+ ""));
			}
			if(i <= PlayerRunes.getRuneLevel(player, rune)){
				block = Api.makeItem("35:5", 1, "&a&lUnLocked", Lore);
			}else{
				block = Api.makeItem("35:14", 1, "&c&lLocked", Lore);
			}
			inv.addItem(block);
		}
		ItemStack back = Api.makeItem("410", 1, "&7&l<< &3Back");
		inv.setItem(8, back);
		ItemStack runes = Api.makeItem("388", 1, "&2&lCredits", Arrays.asList("&6Available: &7" + PlayerRunes.getAvailableCredits(player)));
		inv.setItem(7, runes);
		player.openInventory(inv);
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e){
		Inventory inv = e.getInventory();
		Player player = (Player) e.getWhoClicked();
		if(inv != null){
			if(inv.getName().equalsIgnoreCase(Api.color(Main.settings.getConfig().getString("Settings.Inventory-Name")))){
				e.setCancelled(true);
				ItemStack item = e.getCurrentItem();
				if(item != null){
					if(item.hasItemMeta()){
						if(item.getItemMeta().hasDisplayName()){
							if(Main.runes.isRune(Api.removeColor(item.getItemMeta().getDisplayName()))){
								Rune rune = Main.runes.getFromName(Api.removeColor(item.getItemMeta().getDisplayName()));
								openLeveler(player, rune);
								pickedRune.put(player, rune);
								return;
							}
						}
					}
				}
			}
			if(inv.getName().equalsIgnoreCase(Api.color(Main.settings.getConfig().getString("Settings.Choose-Level")))){
				e.setCancelled(true);
				ItemStack item = e.getCurrentItem();
				Rune rune = pickedRune.get(player);
				if(item != null){
					if(item.hasItemMeta()){
						if(item.getItemMeta().hasDisplayName()){
							String name = item.getItemMeta().getDisplayName();
							if(name.equalsIgnoreCase(Api.color("&7&l<< &3Back"))){
								openMain(player);
								return;
							}
							if(name.equalsIgnoreCase(Api.color("&6&lActivated"))){
								PlayerRunes.setRuneLevel(player, rune, 0);
								PlayerRunes.setRuneActivation(player, rune, false);
								openLeveler(player, rune);
								PlayerRunes.activateRunes(player);
								return;
							}
							int slot = e.getRawSlot();
							if(slot < inv.getSize()){
								int credits = PlayerRunes.getAvailableCredits(player);
								int cost = 0;
								for(int i = 1; i <= slot; i++){
									cost += Main.runes.getLevelCost(rune, i);
								}
								if(PlayerRunes.getRuneLevel(player, rune) == slot){
									return;
								}
								if((credits - cost) < 0){
									return;
								}
								PlayerRunes.setRuneLevel(player, rune, slot);
								if(slot <= 0){
									PlayerRunes.setRuneActivation(player, rune, false);
								}else{
									PlayerRunes.setRuneActivation(player, rune, true);
								}
								openLeveler(player, rune);
								PlayerRunes.activateRunes(player);
								return;
							}
						}
					}
				}
			}
		}
	}
	
}