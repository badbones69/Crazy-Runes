package me.BadBones69.CrazyRunes.Controlers;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.BadBones69.CrazyRunes.Api;
import me.BadBones69.CrazyRunes.API.PlayerRunes;
import me.BadBones69.CrazyRunes.API.Rune;

public class RuneControl implements Listener{
	
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CrazyRunes");
	public RuneControl(Plugin plugin){
		this.plugin = plugin;
	}
	
	ArrayList<Player> fall = new ArrayList<Player>();
	
	public static void setSpeed(Player player, Integer level){
		if(level >= 1){
			player.setWalkSpeed(.2F + (.1F * level));
		}else{
			player.setWalkSpeed(.2F);
		}
	}
	
	public static void setTank(Player player, Integer level){
		int health = level * 6;
		player.setMaxHealth(20 + health);
	}
	
	public static void setSurviver(Player player, Boolean toggle){
		if(toggle){
			player.setSaturation(1000000);
			player.setFoodLevel(20);
			player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 99999999*20, 10000));
		}else{
			player.setSaturation(20);
			player.setFoodLevel(20);
			player.removePotionEffect(PotionEffectType.SATURATION);
		}
	}
	
	public static void setMedic(Player player, Integer level){
		if(level > 0){
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 99999999*20, level - 1));
		}else{
			player.removePotionEffect(PotionEffectType.REGENERATION);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDamage(EntityDamageByEntityEvent e){
		if(Api.allowsPVP(e.getDamager().getLocation())){
			if(e.getEntity() instanceof Player && e.getDamager() instanceof LivingEntity){
				final LivingEntity damager = (LivingEntity) e.getDamager();
				final Player player = (Player) e.getEntity();
				if(!Api.isFriendly(damager, player)){
					if(PlayerRunes.getRuneActivation(player, Rune.REBORN)){
						int chance = PlayerRunes.getRuneLevel(player, Rune.REBORN) * 10;
						if(player.getHealth() <= 5){
							if(Api.chance(chance, 100)){
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
									public void run(){
										player.setHealth((player.getMaxHealth() / 2));
									}
								}, 0);
							}
						}
					}
					if(PlayerRunes.getRuneActivation(player, Rune.ROCKET)){
						int chance = PlayerRunes.getRuneLevel(player, Rune.ROCKET) * 15;
						if(player.getHealth() <= 5){
							if(Api.chance(chance, 100)){
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
									public void run(){
										Vector v = player.getLocation().toVector().subtract(damager.getLocation().toVector()).normalize().setY(1);
										player.setVelocity(v);
									}
								}, 1);
								player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 1);
								fall.add(player);
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
									public void run(){
										fall.remove(player);
									}
								}, 8*20);
							}
						}
					}
				}
			}
			if(e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity){
				Player damager = (Player) e.getDamager();
				LivingEntity entity = (LivingEntity) e.getEntity();
				if(!Api.isFriendly(damager, entity)){
					if(PlayerRunes.getRuneActivation(damager, Rune.PYRO)){
						int chance = PlayerRunes.getRuneLevel(damager, Rune.PYRO) * 10;
						if(Api.chance(chance, 100)){
							entity.setFireTicks(6*20);
						}
					}
					if(PlayerRunes.getRuneActivation(damager, Rune.LEECH)){
						int chance = PlayerRunes.getRuneLevel(damager, Rune.LEECH) * 15;
						if(Api.chance(chance, 100)){
							double damage = e.getDamage() / 10;
							if(damager.getHealth() + damage >= damager.getMaxHealth()){
								damager.setHealth(damager.getMaxHealth());
							}else{
								damager.setHealth(damage + damager.getHealth());
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerFallDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			if(e.getCause() == DamageCause.FALL){
				if(fall.contains((Player)e.getEntity())){
					e.setCancelled(true);
				}
			}
		}
	}
	
}