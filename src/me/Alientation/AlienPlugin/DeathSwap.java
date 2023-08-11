package me.Alientation.AlienPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import me.Alientation.AlienPlugin.Modifiers.Modifier;
import me.Alientation.AlienPlugin.Util.PlayerChatManager;
import me.Alientation.AlienPlugin.Util.PlayerInterface;
import me.Alientation.AlienPlugin.Util.PlayerRewardsHandler;
import me.Alientation.AlienPlugin.Util.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;


public class DeathSwap extends Game{
	
	public HashMap<String,Float> playerBets;
	public String gameMaster;
	public String winner;
	public Plugin p;	
	public Inventory gameMenuAdmin = Bukkit.createInventory(null, 9, ChatColor.GOLD + "" + ChatColor.BOLD + "Death Swap Settings");
	public Inventory gameMenuPlayers = Bukkit.createInventory(null, 9, ChatColor.GOLD + "" + ChatColor.BOLD + "Death Swap Game");
	
	
	public int swapSeconds = 300;
	public int spawnRadius = 500;
	public int spawnCenterX = 0;
	public int spawnCenterZ = 0;
	
	@SuppressWarnings("deprecation")
	public DeathSwap(Player player, Plugin p) {
		super();
		playerBets = new HashMap<String,Float>();
		
		this.p = p;
		name = "deathswap";
		initialWorld = Bukkit.getWorld("world");
		updateMenu();
		gameMaster = player.getUniqueId().toString();
		showMenu(player);
		
		TextComponent message = new TextComponent(player.getName() + " has started a Death Swap Game! Click here to join the match!");
		players.add(player.getUniqueId().toString());
		message.setColor(ChatColor.GOLD);
		message.setBold(true);
		message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/game join"));
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder("Click here to join Death Swap!").color(ChatColor.GRAY).italic(true).create()));
		Main.broadcastGlobalMessage(message);
	}
	
	public void pause() {
		
	}
	
	public void unpause() {
		
	}
	
	public void openModifierMenu(Player player) {
		
	}
	
	public void updateMenu() {
		updateGameMenu();
		gameMenuAdmin.clear();
		ItemStack item = new ItemStack(Material.CLOCK);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		
		/*
		 * start game
		 */
		item.setType(Material.GREEN_WOOL);
		meta.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Begin Game!");
		lore.add(ChatColor.GRAY + "Player list!");
		lore.add(ChatColor.GRAY + "--------------------------");
		for (String id : players) {
			Player p = Bukkit.getPlayer(Util.getUUID(id));
			if (p == null) {
				lore.add("  ♦ " + ChatColor.GRAY + " OFFLINE PLAYER - " + id);
			} else {
				lore.add("  ♦ " + ChatColor.GRAY + p.getName());
			}
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuAdmin.setItem(0, item);
		
		lore.clear();
		/*
		 * set swap time in seconds
		 */
		item.setType(Material.CLOCK);
		meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Change Swap Time (Current " + swapSeconds + ")");
		lore.add(ChatColor.GRAY + "Left Click to increase swap time!");
		lore.add(ChatColor.GRAY + "Right  Click to decrease swap time!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuAdmin.setItem(1, item);
		
		lore.clear();
		
		/*
		 * set spawn radius
		 */
		item.setType(Material.OAK_SIGN);
		meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Change Spawn Radius (Current " + spawnRadius + ")");
		lore.add(ChatColor.GRAY + "Left Click to increase spawn radius!");
		lore.add(ChatColor.GRAY + "Right  Click to decrease spawn radius!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuAdmin.setItem(2, item);
		
		lore.clear();
		
		/*
		 * set spawn center
		 */
		item.setType(Material.OAK_SIGN);
		meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Change Spawn Center (Current " + spawnCenterX +", " + spawnCenterZ + ")");
		lore.add(ChatColor.GRAY + "Click to set random spawn center!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuAdmin.setItem(3, item);
		
		lore.clear();
		
		/*
		 * add modifiers
		 */
		item.setType(Material.COMMAND_BLOCK);
		meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Click to add Modifiers");
		lore.add(ChatColor.GRAY + "Current Game Modifiers");
		lore.add(ChatColor.GRAY + "-------------------------------");
		for (Modifier m : modifiers) {
			lore.add("  ♦ " + ChatColor.GRAY + "" + m.name);
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuAdmin.setItem(4, item);
		
		lore.clear();
		
		/*
		 * Player Bets
		 */
		
		item.setType(Material.GOLD_NUGGET);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Bet Coins!");
		lore.add(ChatColor.GRAY + "Bet List");
		lore.add(ChatColor.GRAY + "==============");
		for (String id : players) {
			if (playerBets.get(id) == null) {
				continue;
			}
			if (Bukkit.getPlayer(Util.getUUID(id)) == null) {
				lore.add("  ♦ " + ChatColor.GRAY + "" + ChatColor.ITALIC + playerBets.get(id) + " - OFFLINE PLAYER - " + id);
				continue;
			}
			lore.add("  ♦ " + ChatColor.GRAY + "" + ChatColor.ITALIC + playerBets.get(id) + " - " + Bukkit.getPlayer(Util.getUUID(id)).getName());
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuAdmin.setItem(5, item);
		
		lore.clear();
		
		
		/*
		 * Remove players from game
		 */
		
		
		item.setType(Material.WRITABLE_BOOK);
		meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Remove Players");
		lore.add(ChatColor.GRAY + "Click to open player list!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuAdmin.setItem(7, item);
		
		lore.clear();
		
		
		
		/*
		 * cancel game create
		 */
		item.setType(Material.BARRIER);
		meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Cancel");
		lore.add(ChatColor.GRAY + "Click to cancel game create!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuAdmin.setItem(8, item);
		
		lore.clear();
		
	}
	
	@SuppressWarnings("deprecation")
	public void openPlayerList(Player player) {
		Inventory playerList = Bukkit.createInventory(null, (int)Math.ceil((1+players.size())/9.0) * 9, ChatColor.GOLD + "" + ChatColor.BOLD + "Player List");
		for (String id : players) {
			Player p = Bukkit.getPlayer(Util.getUUID(id));
			ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta meta = (SkullMeta) skull.getItemMeta();
			if (p != null) {
				meta.setOwner(p.getName());
			}
			List<String> lore = new ArrayList<String>();
			meta.setDisplayName(ChatColor.WHITE + "Click to remove " + ChatColor.AQUA + "" + ChatColor.BOLD + p.getName() + ChatColor.WHITE + " From the game!");
			lore.add(id);
			meta.setLore(lore);
			skull.setItemMeta(meta);
			playerList.addItem(skull);
		}
		player.openInventory(playerList);
	}
	
	public void updateGameMenu() {
		gameMenuPlayers.clear();
		ItemStack item = new ItemStack(Material.GREEN_WOOL);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		
		
		meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Join Game!");
		lore.add(ChatColor.GRAY + "Click to join game!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuPlayers.setItem(0, item);
		
		lore.clear();
		
		item.setType(Material.OAK_SIGN);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Player List!");
		lore.add(ChatColor.GRAY + "Queued Players");
		lore.add(ChatColor.GRAY + "==============");
		for (String id : players) {
			if (Bukkit.getPlayer(Util.getUUID(id)) == null) {
				lore.add("  ♦ " + ChatColor.GRAY + "" + ChatColor.ITALIC + "OFFLINE PLAYER - " + id);
				continue;
			}
			lore.add("  ♦ " + ChatColor.GRAY + "" + ChatColor.ITALIC + Bukkit.getPlayer(Util.getUUID(id)).getName());
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuPlayers.setItem(1, item);
		
		lore.clear();
		
		
		/*
		 * Player Bets
		 */
		
		item.setType(Material.GOLD_NUGGET);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Bet Coins!");
		lore.add(ChatColor.GRAY + "Bet List");
		lore.add(ChatColor.GRAY + "==============");
		for (String id : players) {
			if (playerBets.get(id) == null) {
				continue;
			}
			if (Bukkit.getPlayer(Util.getUUID(id)) == null) {
				lore.add("  ♦ " + ChatColor.GRAY + "" + ChatColor.ITALIC + playerBets.get(id) + " - OFFLINE PLAYER - " + id);
				continue;
			}
			lore.add("  ♦ " + ChatColor.GRAY + "" + ChatColor.ITALIC + playerBets.get(id) + " - " + Bukkit.getPlayer(Util.getUUID(id)).getName());
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuPlayers.setItem(3, item);
		
		lore.clear();
		
		
		/*
		 * current modifiers
		 */
		item.setType(Material.OAK_SIGN);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Modifiers");
		lore.add(ChatColor.GRAY + "Current Game Modifiers");
		lore.add(ChatColor.GRAY + "-------------------------------");
		for (Modifier m : modifiers) {
			lore.add("  ♦ " + ChatColor.GRAY + "" + m.name);
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuAdmin.setItem(4, item);
		
		lore.clear();
		
		
		item.setType(Material.BARRIER);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Leave Game!");
		lore.add(ChatColor.GRAY + "Click to leave game!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuPlayers.setItem(8, item);
		
		lore.clear();
		
	}
	
	public void showMenu(Player player) {
		if (!player.getUniqueId().toString().equals(gameMaster.toString())) {
			player.openInventory(gameMenuPlayers);
		} else {
			player.openInventory(gameMenuAdmin);
		}
	}
	
	@SuppressWarnings("deprecation")
	public boolean start() {
		if (players.size() < 2) {
			Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Must have at least 2 players to play!");
			return false;
		}
		
		if (!Util.allUUIDOnline(players)) {
			Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Not all participants are online! Remove them or wait for them to log on to start!");
			return false;
		}
		
		
		super.start();
		Random random = new Random();
		World world = Bukkit.getWorld("world");
		world.setDifficulty(Difficulty.NORMAL);
		world.setTime(0);
		for (Entity e : world.getEntities()) {
			if (e instanceof Player) {
				
			} else {
				if (e instanceof Monster) {
					e.remove();
				}
			}
		}
		
		hasStarted = true;
		PlayerChatManager.createTeamChat("DEATHSWAP");
		PlayerChatManager.createTeamChat("SPECTATOR");
		
		Main.mainTab.setRunType(1);
		if (Bukkit.getPlayer(gameMaster) != null) {
			Bukkit.getPlayer(gameMaster).closeInventory();
		}
		for (String id : players) {
			Player p = Bukkit.getPlayer(Util.getUUID(id));
			if (p == null) {
				Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.DARK_RED + "" + ChatColor.BOLD + "A participant disconnected, Stopping game!");
				return false;
			}
			Util.createBoard(p);
			for (PotionEffect effect : p.getActivePotionEffects())
		        p.removePotionEffect(effect.getType());
			p.setAllowFlight(false);
			p.setFlying(false);
			p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Teleporting to world!");
			p.teleport(world.getSpawnLocation());
			p.setGameMode(GameMode.SURVIVAL);
			p.getInventory().clear();
			p.setHealth(p.getMaxHealth());
			p.setFoodLevel(20);
			p.setSaturation(20);
			Iterator<Advancement> iterator = Bukkit.getServer().advancementIterator();
	        while (iterator.hasNext())
	        {
	            AdvancementProgress progress = p.getAdvancementProgress(iterator.next());
	            for (String criteria : progress.getAwardedCriteria())
	                progress.revokeCriteria(criteria);
	        }
	        
	        activePlayers.add(p.getUniqueId().toString());
			PlayerChatManager.addPlayerToTeam(p.getName(), "DEATHSWAP");
	        
			p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.DARK_RED + "Every " + swapSeconds + " seconds, you will switch with a random player. Last man standing wins!");
			
			int x = random.nextInt(spawnRadius * 2) - spawnRadius + spawnCenterX;
			int z = random.nextInt(spawnRadius * 2) - spawnRadius + spawnCenterZ;
			Location loc = new Location(p.getWorld(), x, 10.0D, z);
			int y = p.getWorld().getHighestBlockYAt(loc);
			loc = new Location(p.getWorld(), x, y, z);
			p.teleport(loc);
		} 
		startRewards();
		startTimer();
		return true;
	}
	
	public void startRewards() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plug, new Runnable() {
			@Override
			public void run() {
				if (gameOver == false) {
					startRewards();
				}
			}
		}, 1200);
		
		for (String id : activePlayers) {
			PlayerInterface pi = PlayerRewardsHandler.playerInterfaceMappings.get(id);
			if (pi == null) {
				PlayerRewardsHandler.playerInterfaceMappings.put(id, new PlayerInterface(id));
				pi = PlayerRewardsHandler.playerInterfaceMappings.get(id);
			}
			pi.addCoins(PlayerRewardsHandler.COINS_PER_MINUTE_MANHUNT, ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.YELLOW + "You gained ", true);
			pi.addXP(PlayerRewardsHandler.XP_PER_MINUTE_MANHUNT, ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.YELLOW + "You gained ", true);
		}
	}
	
	public boolean end() {
		PlayerChatManager.deleteTeamChat("DEATHSWAP");
		PlayerChatManager.deleteTeamChat("SPECTATOR");
		Main.mainTab.setRunType(0);
		gameOver = true;
		if (winner == null) {
			TextComponent message = new TextComponent(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap]" + ChatColor.RESET + " Cancelled Game! :(");
			Main.broadcastGlobalMessage(message);
		}
		for (String id: players) {
			Player p = Bukkit.getPlayer(Util.getUUID(id));
			if (p == null)
				continue;
			Util.createBoard(p);
			if (!p.isDead()) {
				p.setGameMode(GameMode.CREATIVE);
				p.getInventory().clear();
				p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Teleporting to lobby!");
	        	p.teleport(Bukkit.getWorld("lobby").getSpawnLocation());
			}
		}
		players.clear();
		Main.currentGame = null;
		Util.updateAllPlayers();
		return true;
	}
	
	public void startTimer() {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(p, new Runnable() {
			public void run() {
				if (!gameOver) {
					Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Switching in 10 seconds!");
					startWait(9);
				} 
			}
		},(swapSeconds * 20L));
	}
	
	public void startWait(final int count) {
		if (count != -1) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(p, new Runnable() {
				public void run() {
					if (!gameOver) {
						if (count != 0)
							Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Switching in " + count + " seconds!");
						startWait(count - 1);
					} 
				}
			},20L);
		} else {
			teleportLoop(activePlayers.size()%2 == 0 ? 0:1);
			Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Switched!");
			if (!gameOver)
				startTimer(); 
		}
	}
	
	public void removeGlass(final Location loc) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plug, new Runnable() {
			@Override
			public void run() {
				loc.getWorld().getBlockAt(loc).setType(Material.AIR);
			}
		}, 100L);
	}
	
	public void teleportLoop(int i) {
		if (i >= activePlayers.size()) {
			return;
		}
		final int j = i+1 >= activePlayers.size() ? 0:i+1;
		if (Bukkit.getPlayer(Util.getUUID(activePlayers.get(i))) == null || Bukkit.getPlayer(Util.getUUID(activePlayers.get(j))) == null) {
			Bukkit.broadcastMessage("Could not teleport 2 players because someone was offline");
		} else {
			Player p1 = Bukkit.getPlayer(Util.getUUID(activePlayers.get(i)));
			Player p2 = Bukkit.getPlayer(Util.getUUID(activePlayers.get(j)));
			Location finalTeleport1 = new Location(Bukkit.getWorld(p1.getWorld().getName()), p1.getLocation().getX(), p1.getLocation().getY(), p1.getLocation().getZ());
			Location finalTeleport2 = new Location(Bukkit.getWorld(p2.getWorld().getName()), p2.getLocation().getX(), p2.getLocation().getY(), p2.getLocation().getZ());
			
			finalizeTeleport(p1, p2, finalTeleport1, finalTeleport2);
			
			p1.setFallDistance(0);
			p2.setFallDistance(0);
			
			Location l1 = p1.getLocation().clone();
			l1.setY(250);
			p1.getWorld().getBlockAt(l1).setType(Material.GLASS);
			removeGlass(l1.clone());
			l1.setY(l1.getY() + 2);
			
			Location l2 = p2.getLocation().clone();
			l2.setY(250);
			p2.getWorld().getBlockAt(l2).setType(Material.GLASS);
			removeGlass(l2.clone());
			l2.setY(l2.getY() + 2);
			
			p1.teleport(l2);
			p2.teleport(l1);
			
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plug, new Runnable() {
			@Override
			public void run() {
				teleportLoop(j+1);
			}
		},4L);
	}
	
	public void finalizeTeleport(final Player p1, final Player p2, final Location l1, final Location l2) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plug, new Runnable() {
			@Override
			public void run() {
				p1.teleport(l2);
				p2.teleport(l1);
				p1.setFallDistance(0);
				p2.setFallDistance(0);
			}
		},80L);
	}
	
	public void playerDeath(Player player) {
		PlayerChatManager.addPlayerToTeam(player.getName(), "SPECTATOR");
		Bukkit.broadcastMessage(ChatColor.GREEN + "" + (activePlayers.size() - 1) + " players remaining!");
		if (activePlayers.size() == 2) {
			gameOver = true;
			Util.removeAllUUID(activePlayers, player.getUniqueId().toString());
			player.setGameMode(GameMode.CREATIVE);
			winner = activePlayers.get(0);
			float totalBets = 0;
			for (String id : players) {
				if (playerBets.get(id) == null || id.equals(winner)) {
					continue;
				}
				PlayerRewardsHandler.playerInterfaceMappings.get(id).removeCoins(playerBets.get(id), ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap]" + ChatColor.RESET + " You lost your bet of " + playerBets.get(id) + " coins! :(");
				totalBets += playerBets.get(id);
			}
			
			PlayerRewardsHandler.playerInterfaceMappings.get(winner).addCoins(totalBets, ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap]" + ChatColor.RESET + " You earned " + totalBets + " coins from the prize pool! :)", true);
			
			Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Congrats to " + Bukkit.getPlayer(Util.getUUID(winner)).getName().toString() + "for winning DeathSwap and earning " + totalBets + " coins from the prize pool!");
			
			Bukkit.getPlayer(Util.getUUID(winner)).sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "You Won", ChatColor.GOLD + "" + ChatColor.DARK_RED + "DeathSwap!", 10, 70, 20);
			
			for (String id : players) {
				Player p = Bukkit.getPlayer(Util.getUUID(id));
				if (!p.getUniqueId().toString().equals(winner)) {
					p.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "Congrats to " + Bukkit.getPlayer(Util.getUUID(winner)).getName(), ChatColor.DARK_RED + "" + ChatColor.BOLD + "for winning DeathSwap!", 10, 70, 20);
				}
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plug, new Runnable() {
				@Override
				public void run() {
					end();
				}
			}, 10L);
		} else {
			Util.removeAllUUID(activePlayers, player.getUniqueId().toString());
	    	player.setGameMode(GameMode.CREATIVE);
	    	Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "oof! RIP " + player.getName().toString());
	    } 
	}
	
	public boolean initModifiers() {
		
		
		return true;
	}
}
