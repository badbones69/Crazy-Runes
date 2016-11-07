package me.BadBones69.CrazyRunes;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.BadBones69.CrazyRunes.API.CrazyRunes;
import me.BadBones69.CrazyRunes.API.PlayerRunes;
import me.BadBones69.CrazyRunes.API.Rune;
import me.BadBones69.CrazyRunes.Controlers.GUIControl;
import me.BadBones69.CrazyRunes.Controlers.JoinAndLeaveEvent;
import me.BadBones69.CrazyRunes.Controlers.RuneControl;

public class Main extends JavaPlugin{
	
	public static SettingsManager settings = SettingsManager.getInstance();
	public static CrazyRunes runes = CrazyRunes.getInstance();
	
	@Override
	public void onEnable(){
		settings.setup(this);
		runes.load();
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new GUIControl(), this);
		pm.registerEvents(new RuneControl(this), this);
		pm.registerEvents(new JoinAndLeaveEvent(), this);
		for(Player player : Bukkit.getOnlinePlayers()){
			PlayerRunes.loadPlayer(player);
		}
	}
	
	@Override
	public void onDisable(){
		for(Player player : Bukkit.getOnlinePlayers()){
			PlayerRunes.unLoadPlayer(player);
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args){
		if(commandLable.equalsIgnoreCase("Runes") || commandLable.equalsIgnoreCase("CrazyRunes")){
			if(args.length == 0){
				if(sender instanceof Player){
					GUIControl.openMain((Player)sender);
					return true;
				}else{
					sender.sendMessage(Api.color("&cYou must be a player to use this command."));
					return true;
				}
			}
			if(args.length >= 1){
				if(args[0].equalsIgnoreCase("Help")){
					if(!Api.hasPermission(sender, "Access"))return true;
					sender.sendMessage(Api.color("&b/Runes &7- Allows you to pick your runes you wish to use."));
					sender.sendMessage(Api.color("&b/Runes Check [Player] &7- Check a players runes."));
					sender.sendMessage(Api.color("&b/Runes Add <Amount> [Player] &7- Give a player more Credits."));
					sender.sendMessage(Api.color("&b/Runes Remove <Amount> [Player] &7- Take away a players Credits."));
					sender.sendMessage(Api.color("&b/Runes Reload &7- Reloads the runes files."));
					sender.sendMessage(Api.color("&b/Runes Help &7- Shows the runes help menu."));
					return true;
				}
				if(args[0].equalsIgnoreCase("Reload")){
					if(!Api.hasPermission(sender, "Admin"))return true;
					settings.reloadConfig();
					settings.reloadData();
					settings.reloadMsg();
					settings.reloadRunes();
					settings.setup(this);
					runes.load();
					for(Player player : Bukkit.getOnlinePlayers()){
						PlayerRunes.loadPlayer(player);
					}
					sender.sendMessage(Api.color(Api.getPrefix() + "&7You have just reloaded all the files."));
					return true;
				}
				if(args[0].equalsIgnoreCase("Add")){// /Runes Add <Amount> [Player]
					if(!Api.hasPermission(sender, "Admin"))return true;
					if(args.length >= 2){
						int amount = 0;
						Player player = null;
						if(!Api.isInt(args[1])){
							sender.sendMessage(Api.getPrefix()+Api.color("&cThat is not a number."));
							return true;
						}else{
							amount = Integer.parseInt(args[1]);
						}
						if(args.length >= 3){
							if(!Api.isOnline(args[2], sender))return true;
							player = Api.getPlayer(args[2]);
						}else{
							if(sender instanceof Player){
								player = (Player) sender;
							}else{
								sender.sendMessage(Api.getPrefix()+Api.color("&cYou must be a player to use this command."));
								return true;
							}
						}
						PlayerRunes.addCredits(player, amount);
						sender.sendMessage(Api.getPrefix()+Api.color("&7You have just given &6" + player.getName() + " " + amount + " &7more credits."));
						return true;
					}
					sender.sendMessage(Api.getPrefix()+Api.color("&c/Runes Add <Amount> [Player]"));
					return true;
				}
				if(args[0].equalsIgnoreCase("Remove")){// /Runes Remove <Amount> [Player]
					if(!Api.hasPermission(sender, "Admin"))return true;
					if(args.length >= 2){
						int amount = 0;
						Player player = null;
						if(!Api.isInt(args[1])){
							sender.sendMessage(Api.getPrefix()+Api.color("&cThat is not a number."));
							return true;
						}else{
							amount = Integer.parseInt(args[1]);
						}
						if(args.length >= 3){
							if(!Api.isOnline(args[2], sender))return true;
							player = Api.getPlayer(args[2]);
						}else{
							if(sender instanceof Player){
								player = (Player) sender;
							}else{
								sender.sendMessage(Api.getPrefix()+Api.color("&cYou must be a player to use this command."));
								return true;
							}
						}
						PlayerRunes.removeCredits(player, amount);
						sender.sendMessage(Api.getPrefix()+Api.color("&7You have just removed &6" + amount + " &7credits from &6" + player.getName() + "&7."));
						return true;
					}
					sender.sendMessage(Api.getPrefix()+Api.color("&c/Runes Remove <Amount> [Player]"));
					return true;
				}
				if(args[0].equalsIgnoreCase("Check")){// /Runes Check [Player]
					if(!Api.hasPermission(sender, "Access"))return true;
					Player player = null;
					if(args.length >= 2){
						if(!Api.isOnline(args[1], sender))return true;
						player = Api.getPlayer(args[1]);
					}else{
						if(sender instanceof Player){
							player = (Player) sender;
						}else{
							sender.sendMessage(Api.color("&cYou must be a player to use this command."));
							return true;
						}
					}
					String msg = "";
					for(Rune rune : runes.getRunes()){
						if(PlayerRunes.getRuneActivation(player, rune)){
							msg += "&a&l";
						}else{
							msg += "&c&l";
						}
						msg += rune.getName() + "&7: &3" + PlayerRunes.getRuneLevel(player, rune);
						msg += "\n";
					}
					sender.sendMessage(Api.color("&7A list of all &b" + player.getName() + "'s &7runes."));
					sender.sendMessage(Api.color("&7Max Credits: &3" + PlayerRunes.getMaxCredits(player)));
					sender.sendMessage(Api.color("&7Available Credits: &3" + PlayerRunes.getAvailableCredits(player)));
					sender.sendMessage(Api.color(msg));
					return true;
				}
			}
			sender.sendMessage(Api.color("&cPlease use /Runes Help for more information."));
			return true;
		}
		return false;
	}
	
}