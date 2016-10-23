package me.BadBones69.CrazyRunes.Controlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.BadBones69.CrazyRunes.API.PlayerRunes;

public class JoinAndLeaveEvent implements Listener{
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		Player player = e.getPlayer();
		PlayerRunes.loadPlayer(player);
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e){
		Player player = e.getPlayer();
		PlayerRunes.unLoadPlayer(player);
	}
	
}