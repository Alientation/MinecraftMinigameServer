package me.Alientation.AlienPlugin.Listener;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.Alientation.AlienPlugin.Main;
import me.Alientation.AlienPlugin.Util.GameFileHandler;
import me.Alientation.AlienPlugin.Util.PlayerInterface;
import me.Alientation.AlienPlugin.Util.PlayerRewardsHandler;
import me.Alientation.AlienPlugin.Util.Util;

public class MainListener implements Listener{
	
	@EventHandler
	public void onMobSpawn(EntitySpawnEvent event) {
		if (Main.currentGame == null || Main.currentGame.hasStarted == false) {
			if (event.getEntity() instanceof Monster) {
				event.setCancelled(true);
			} else {
				switch(event.getEntity().getType()) {
				case BAT:
				case BEE:
				case CAT:
				case COD:
				case FOX:
				case OCELOT:
				case PARROT:
				case PANDA:
				case POLAR_BEAR:
				case SALMON:
				case SQUID:
				case TROPICAL_FISH:
					event.setCancelled(true);
				default:
					break;
				}
			}
		}
	}
	
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (Main.currentGame == null || Main.currentGame.hasStarted == false || !Util.containsUUID(Main.currentGame.activePlayers, event.getPlayer().getUniqueId().toString())) {
			if (event.getPlayer().getWorld().getName().equals("lobby")) {
				if (event.getPlayer().getLocation().distance(Bukkit.getWorld("lobby").getSpawnLocation()) < 100 && Integer.parseInt(GameFileHandler.load(event.getPlayer().getUniqueId().toString() + "_rank", 0).toString()) <= 5 || event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You cannot break blocks within 100 blocks of spawn!");
				}
			} else {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You cannot break blocks as a spectator!");
			}
		}
	}
	
	@EventHandler
	public void onBlockPlaced(BlockPlaceEvent event) {
		if (Main.currentGame == null || Main.currentGame.hasStarted == false || !Util.containsUUID(Main.currentGame.activePlayers, event.getPlayer().getUniqueId().toString())) {
			if (event.getPlayer().getWorld().getName().equals("lobby")) {
				if (event.getPlayer().getLocation().distance(Bukkit.getWorld("lobby").getSpawnLocation()) < 100 && Integer.parseInt(GameFileHandler.load(event.getPlayer().getUniqueId().toString() + "_rank", 0).toString()) <= 5 || event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You cannot place blocks within 100 blocks of spawn!");
				}
			} else {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You cannot place blocks as a spectator!");
			}
		}
	}
	
	
	@EventHandler
	public void normalLogin(PlayerLoginEvent event) {
		Util.updatePlayerName(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if (Main.currentGame == null || Main.currentGame.hasStarted == false) {
			event.setRespawnLocation(Bukkit.getWorld("lobby").getSpawnLocation());
			 Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plug, new Runnable() {
		           @Override
		           public void run() {
		        	   event.getPlayer().setAllowFlight(true);
		        	   Util.updateAllPlayers();
		           }
		     });
		} else {
			if (Util.containsUUID(Main.currentGame.activePlayers, event.getPlayer().getUniqueId().toString()) && event.getRespawnLocation().getWorld().getName().equals("lobby")) {
				event.setRespawnLocation(Bukkit.getWorld("world").getSpawnLocation());
				event.getPlayer().setAllowFlight(true);
			}
		}
	}
	
	
	@EventHandler
	public void onItemSpawn(ItemSpawnEvent event) {
		if (event.getEntity().getItemStack().getItemMeta() != null && event.getEntity().getItemStack().getItemMeta().getDisplayName() != null  && event.getEntity().getItemStack().getItemMeta().getDisplayName().equals(ChatColor.AQUA + "Open Game Menu!")) {
			event.getEntity().remove();
		}
	}
	
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
		if (Main.currentGame != null && Util.containsUUID(Main.currentGame.players, event.getPlayer().getUniqueId().toString())) {
			event.getPlayer().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Welcome back " + ChatColor.GOLD + event.getPlayer().getName() + ChatColor.RESET + ChatColor.WHITE + "!");
		} else {
			event.getPlayer().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Teleporting to lobby!");
			Util.giveGameCompass(event.getPlayer());
			event.getPlayer().setAllowFlight(true);
			event.getPlayer().teleport(Bukkit.getWorld("lobby").getSpawnLocation());
		}
		int type = Main.mainTab.clear();
		Util.loadTabManager(Main.mainTab);
		Main.mainTab.setRunType(type);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plug, new Runnable() {
			@Override
			public void run() {
				if (!PlayerRewardsHandler.playerInterfaceMappings.containsKey(event.getPlayer().getUniqueId().toString())) {
					PlayerRewardsHandler.playerInterfaceMappings.put(event.getPlayer().getUniqueId().toString(), new PlayerInterface(event.getPlayer()));
				}
				
				Util.createBoard(event.getPlayer());
			}
		}, 20L);
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		
		if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.COMPASS && event.getPlayer().getInventory().getHeldItemSlot() == 8) {
			if (Main.currentGame == null) {
				event.getPlayer().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "No active games yet!");
			} else if (Main.currentGame.hasStarted == false){
				Main.currentGame.updateGameMenu();
				Util.removeAllUUID(Main.currentGame.players, event.getPlayer().getUniqueId().toString());
				Main.currentGame.players.add(event.getPlayer().getUniqueId().toString());
				Main.currentGame.showMenu(event.getPlayer());
			}
		}
		if (Main.currentGame == null || Main.currentGame.hasStarted == false) {
			if (event.getPlayer().getWorld().getName().equals("lobby")) {
				if (event.getPlayer().getLocation().distance(Bukkit.getWorld("lobby").getSpawnLocation()) < 100 && Integer.parseInt(GameFileHandler.load(event.getPlayer().getUniqueId().toString() + "_rank", 0).toString()) <= 5) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onItemMove(InventoryMoveItemEvent event) {
		if ((event.getItem() != null && event.getItem().getType() == Material.COMPASS && event.getItem().getItemMeta().getDisplayName().equals("Game Menu"))) {
			event.setCancelled(true);
		}
	}
	
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		
		if ((event.getCurrentItem() != null || (event.getHotbarButton() >= 0 && ((Player) event.getWhoClicked()).getInventory().getItem(event.getHotbarButton()) != null)) && event.getCurrentItem().getType() == Material.COMPASS && event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.AQUA + "Open Game Menu!")) {
			
			if (Main.currentGame == null) {
				if (event.getAction() == InventoryAction.DROP_ALL_CURSOR) {
					return;
				}
				((Player) event.getWhoClicked()).closeInventory();
				event.getWhoClicked().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "No active games yet!");
			} else {
				((Player) event.getWhoClicked()).closeInventory();
				Main.currentGame.updateGameMenu();
				Util.removeAllUUID(Main.currentGame.players, event.getWhoClicked().getUniqueId().toString());
				Main.currentGame.players.add(event.getWhoClicked().getUniqueId().toString());
				Main.currentGame.showMenu((Player) event.getWhoClicked());
			}
			event.setCancelled(true);
			
		}
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		if (event.getItemDrop() != null && event.getItemDrop().getItemStack().getType() == Material.COMPASS && event.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals(ChatColor.AQUA + "Open Game Menu!")) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemFrameItemBreak(EntityDamageByEntityEvent event) {
		if (Main.currentGame == null || Main.currentGame.hasStarted == false) {
			if (event.getEntity() instanceof ItemFrame && event.getEntity().getLocation().getWorld().getName().equals("lobby")) {
				if (!(event.getDamager() instanceof Player) || Integer.parseInt("" + GameFileHandler.load(event.getDamager().getUniqueId().toString() + "_rank", 0)) <= 5) {
					event.setCancelled(true);
				}
			}
		}
	}
}