package me.Alientation.AlienPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;

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

public class Manhunt extends Game{
	
	public HashMap<String,Float> speedrunnerBets;
	public HashMap<String,Float> hunterBets;
	
	public Plugin p;	
	public String winningTeam = null;
	public boolean huntersCanMove = false;
	
	public String gameMaster = null;
	public int hunterWaitTime = 1;
	
	public ArrayList<String> speedrunners;
	public HashMap<String,Location> playersDimensionLocation;
	public ArrayList<String> hunters;
	public HashMap<String,String> trackingWho;
	
	
	public Inventory gameMenuAdmin = Bukkit.createInventory(null, 9, ChatColor.GOLD + "" + ChatColor.BOLD + "Manhunt Settings");
	public Inventory gameMenuPlayers = Bukkit.createInventory(null, 9, ChatColor.GOLD + "" + ChatColor.BOLD + "Manhunt Game");
	
	@SuppressWarnings("deprecation")
	public Manhunt(Player player, Plugin p) {
		super();
		speedrunnerBets = new HashMap<String,Float>();
		hunterBets = new HashMap<String,Float>();
		
		initialWorld = Bukkit.getWorld("world");
		name = "manhunt";
		gameMaster = player.getUniqueId().toString();
		speedrunners = new ArrayList<String>();
		hunters = new ArrayList<String>();
		players.add(player.getUniqueId().toString());
		playersDimensionLocation = new HashMap<String,Location>();
		trackingWho = new HashMap<String,String>();
		updateMenu();
		showMenu(player);
		this.p = p;
		TextComponent message = new TextComponent(player.getName() + " has started a Manhunt Game! Click here to join the match!");
		message.setColor(ChatColor.GOLD);
		message.setBold(true);
		message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/game join"));
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder("Click here to join Manhunt!").color(ChatColor.GRAY).italic(true).create()));
		Main.broadcastGlobalMessage(message);
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
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Begin Game!");
		lore.add(ChatColor.GRAY + "Click to start the game!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuAdmin.setItem(0, item);
		
		lore.clear();
		
		/*
		 * set hunter wait time in seconds
		 */
		item.setType(Material.CLOCK);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Change Hunter Wait Time (Current " + hunterWaitTime + ")");
		lore.add(ChatColor.GRAY + "Left Click to increase hunter wait time!");
		lore.add(ChatColor.GRAY + "Right  Click to decrease hunter wait time!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuAdmin.setItem(1, item);
		
		lore.clear();
		
		/*
		 * Player Bets
		 */
		
		item.setType(Material.GOLD_NUGGET);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Bet Coins!");
		lore.add(ChatColor.GRAY + "Bet List");
		lore.add(ChatColor.GRAY + "==============");
		lore.add(ChatColor.GRAY + "  - Speedrunners");
		for (String id : speedrunners) {
			if (speedrunnerBets.get(id) == null) {
				continue;
			}
			if (Bukkit.getPlayer(Util.getUUID(id)) == null) {
				lore.add("     ♦ " + ChatColor.GRAY + "" + ChatColor.ITALIC + speedrunnerBets.get(id) + " - OFFLINE PLAYER - " + id);
				continue;
			}
			lore.add("     ♦ " + ChatColor.GRAY + "" + ChatColor.ITALIC + speedrunnerBets.get(id) + " - " + Bukkit.getPlayer(Util.getUUID(id)).getName());
		}
		
		lore.add(ChatColor.GRAY + "  - Hunters");
		for (String id : hunters) {
			if (hunterBets.get(id) == null) {
				continue;
			}
			if (Bukkit.getPlayer(Util.getUUID(id)) == null) {
				lore.add("     ♦ " + ChatColor.GRAY + "" + ChatColor.ITALIC + hunterBets.get(id) + " - OFFLINE PLAYER - " + id);
				continue;
			}
			lore.add("     ♦ " + ChatColor.GRAY + "" + ChatColor.ITALIC + hunterBets.get(id) + " - " + Bukkit.getPlayer(Util.getUUID(id)).getName());
		}
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuAdmin.setItem(3, item);
		
		lore.clear();
		
		
		/*
		 * add modifiers
		 */
		item.setType(Material.OAK_SIGN);
		meta = item.getItemMeta();
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
		 * Join Speedrunners
		 */
		item.setType(Material.BLUE_WOOL);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Click to join SPEEDRUNNERS");
		lore.add(ChatColor.GRAY + "Current Speedrunners");
		lore.add(ChatColor.GRAY + "-------------------------------");
		for (String id : speedrunners) {
			Player p = Bukkit.getPlayer(Util.getUUID(id));
			if (p == null) {
				lore.add("  ♦ " + ChatColor.GRAY + " OFFLINE PLAYER - " + id);
			} else {
				lore.add("  ♦ " + ChatColor.GRAY + p.getName());
			}
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuAdmin.setItem(5, item);
		
		lore.clear();
		
		
		/*
		 * Join Hunters
		 */
		item.setType(Material.RED_WOOL);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Click to join HUNTERS");
		lore.add(ChatColor.GRAY + "Current Hunters");
		lore.add(ChatColor.GRAY + "-------------------------------");
		for (String id : hunters) {
			Player p = Bukkit.getPlayer(Util.getUUID(id));
			if (p == null) {
				lore.add("  ♦ " + ChatColor.GRAY + " OFFLINE PLAYER - " + id);
			} else {
				lore.add("  ♦ " + ChatColor.GRAY + p.getName());
			}
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuAdmin.setItem(6, item);
		
		lore.clear();
		
		
		/*
		 * Remove Players From game
		 */
		
		item.setType(Material.WRITABLE_BOOK);
		meta = item.getItemMeta();
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
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Cancel");
		lore.add(ChatColor.GRAY + "Click to cancel game create!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuAdmin.setItem(8, item);
		
		lore.clear();
	}
	
	@SuppressWarnings("deprecation")
	public void openModifierMenu(Player player) {
		Inventory modifierMenu = Bukkit.createInventory(null, 27, ChatColor.GOLD + "" + ChatColor.BOLD + "Modifier Settings");
		
		/*
		 * [M][M][M][M][M][ ][ ][ ][ ]
		 * [ ][ ][ ][ ][ ][ ][ ][ ][ ]
		 * [L][G][G][G][D][G][G][G][R]
		 */
		
		ItemStack item = new ItemStack(Material.CLOCK);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		
		item.setType(Material.BARRIER);
		if (containsModifier("BORDER_MODIFIER") || containsModifier("BORDER_CHANGE_MODIFIER")) {
			meta.setDisplayName(ChatColor.GREEN + "Border Modifier");
		} else {
			meta.setDisplayName(ChatColor.WHITE + "Border Modifier");
		}
		lore.add(ChatColor.GRAY + "Click to access border settings");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modifierMenu.setItem(0, item);
		
		lore.clear();
		
		
		item.setType(Material.DIRT);
		if (containsModifier("BLOCK_SPAWN_MODIFIER") || containsModifier("BLOCK_RISE_MODIFIER")) {
			meta.setDisplayName(ChatColor.GREEN + "Block Spawn Modifier");
		} else {
			meta.setDisplayName(ChatColor.WHITE + "Block Spawn Modifier");
		}
		lore.add(ChatColor.GRAY + "Click to access block spawn settings");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modifierMenu.setItem(1, item);
		
		lore.clear();
		
		
		item.setType(Material.PLAYER_HEAD);
		meta = item.getItemMeta();
		if (containsModifier("LUCKY_BLOCKS_MODIFIER")) {
			meta.setDisplayName(ChatColor.GREEN + "Lucky Blocks Modifier");
		} else {
			meta.setDisplayName(ChatColor.WHITE + "Lucky Blocks Modifier");
		}
		lore.add(ChatColor.GRAY + "Click to access lucky blocks settings");
		meta.setLore(lore);
		SkullMeta skull = (SkullMeta) meta;
		skull.setOwner("ABigDwarf");
		item.setItemMeta(meta);
		modifierMenu.setItem(2, item);
		
		lore.clear();
		
		
		item.setType(Material.ZOMBIE_HEAD);
		if (containsModifier("MOB_SPAWN_MODIFIER")) {
			meta.setDisplayName(ChatColor.GREEN + "Mob Spawn Modifier");
		} else {
			meta.setDisplayName(ChatColor.WHITE + "Mob Spawn Modifier");
		}
		lore.add(ChatColor.GRAY + "Click to access mob spawn settings");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modifierMenu.setItem(3, item);
		
		lore.clear();
		
		
		item.setType(Material.POTION);
		meta = item.getItemMeta();
		if (containsModifier("POTION_EFFECT_MODIFIER")) {
			meta.setDisplayName(ChatColor.GREEN + "Potion Effect Modifier");
		} else {
			meta.setDisplayName(ChatColor.WHITE + "Potion Effect Modifier");
		}
		lore.add(ChatColor.GRAY + "Click to access potion effect settings");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modifierMenu.setItem(4, item);
		
		lore.clear();
		
		
		
		
		/*
		 * Bottom Row
		 */
		
		item.setType(Material.ARROW);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Left");
		lore.add(ChatColor.GRAY + "Click to go left");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modifierMenu.setItem(18, item);
		
		lore.clear();
		
		
		item.setType(Material.GRAY_STAINED_GLASS_PANE);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GRAY + "blank");
		lore.add(ChatColor.GRAY + "click to do nothing");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modifierMenu.setItem(19, item);
		
		lore.clear();
		
		
		item.setType(Material.GRAY_STAINED_GLASS_PANE);
		meta.setDisplayName(ChatColor.GRAY + "blank");
		lore.add(ChatColor.GRAY + "click to do nothing");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modifierMenu.setItem(20, item);
		
		lore.clear();
		
		item.setType(Material.GRAY_STAINED_GLASS_PANE);
		meta.setDisplayName(ChatColor.GRAY + "blank");
		lore.add(ChatColor.GRAY + "click to do nothing");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modifierMenu.setItem(21, item);
		
		lore.clear();
		
		
		item.setType(Material.GREEN_WOOL);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Confirm Changes");
		lore.add(ChatColor.GRAY + "Click to confirm modifier changes");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modifierMenu.setItem(22, item);
		
		lore.clear();
		
		
		item.setType(Material.GRAY_STAINED_GLASS_PANE);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GRAY + "blank");
		lore.add(ChatColor.GRAY + "click to do nothing");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modifierMenu.setItem(23, item);
		
		lore.clear();
		
		item.setType(Material.GRAY_STAINED_GLASS_PANE);
		meta.setDisplayName(ChatColor.GRAY + "blank");
		lore.add(ChatColor.GRAY + "click to do nothing");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modifierMenu.setItem(24, item);
		
		lore.clear();
		
		item.setType(Material.GRAY_STAINED_GLASS_PANE);
		meta.setDisplayName(ChatColor.GRAY + "blank");
		lore.add(ChatColor.GRAY + "click to do nothing");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modifierMenu.setItem(25, item);
		
		lore.clear();
		
		item.setType(Material.ARROW);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Right");
		lore.add(ChatColor.GRAY + "Click to go right");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modifierMenu.setItem(26, item);
		
		lore.clear();
		player.closeInventory();
		player.openInventory(modifierMenu);
		
	}
	
	public void updateGameMenu() {
		gameMenuPlayers.clear();
		
		ItemStack item = new ItemStack(Material.CLOCK);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		
		/*
		 * Join Speedrunners
		 */
		item.setType(Material.BLUE_WOOL);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Click to join SPEEDRUNNERS");
		lore.add(ChatColor.GRAY + "Current Speedrunners");
		lore.add(ChatColor.GRAY + "-------------------------------");
		for (String id : speedrunners) {
			Player p = Bukkit.getPlayer(Util.getUUID(id));
			if (p == null) {
				lore.add("  ♦ " + ChatColor.GRAY + " OFFLINE PLAYER - " + id);
			} else {
				lore.add("  ♦ " + ChatColor.GRAY + p.getName());
			}
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuPlayers.setItem(0, item);
		
		lore.clear();
		
		
		/*
		 * Join Hunters
		 */
		item.setType(Material.RED_WOOL);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Click to join HUNTERS");
		lore.add(ChatColor.GRAY + "Current Hunters");
		lore.add(ChatColor.GRAY + "-------------------------------");
		for (String id : hunters) {
			Player p = Bukkit.getPlayer(Util.getUUID(id));
			if (p == null) {
				lore.add("  ♦ " + ChatColor.GRAY + " OFFLINE PLAYER - " + id);
			} else {
				lore.add("  ♦ " + ChatColor.GRAY + p.getName());
			}
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
		lore.add(ChatColor.GRAY + "  - Speedrunners");
		for (String id : speedrunners) {
			if (speedrunnerBets.get(id) == null) {
				continue;
			}
			if (Bukkit.getPlayer(Util.getUUID(id)) == null) {
				lore.add("     ♦ " + ChatColor.GRAY + "" + ChatColor.ITALIC + speedrunnerBets.get(id) + " - OFFLINE PLAYER - " + id);
				continue;
			}
			lore.add("     ♦ " + ChatColor.GRAY + "" + ChatColor.ITALIC + speedrunnerBets.get(id) + " - " + Bukkit.getPlayer(Util.getUUID(id)).getName());
		}
		
		lore.add(ChatColor.GRAY + "  - Hunters");
		for (String id : hunters) {
			if (hunterBets.get(id) == null) {
				continue;
			}
			if (Bukkit.getPlayer(Util.getUUID(id)) == null) {
				lore.add("     ♦ " + ChatColor.GRAY + "" + ChatColor.ITALIC + hunterBets.get(id) + " - OFFLINE PLAYER - " + id);
				continue;
			}
			lore.add("     ♦ " + ChatColor.GRAY + "" + ChatColor.ITALIC + hunterBets.get(id) + " - " + Bukkit.getPlayer(Util.getUUID(id)).getName());
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
		gameMenuPlayers.setItem(4, item);
		
		lore.clear();
		
		/*
		 * Leave Game
		 */
		
		item.setType(Material.GREEN_WOOL);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Begin Game!");
		lore.add(ChatColor.GRAY + "Click to start the game!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		gameMenuPlayers.setItem(8, item);
		
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
	
	public void setCompassLocation(Player player, Location location) {
        boolean inMainHand = false;
        if(player.getInventory().getItemInMainHand().getType().equals(Material.COMPASS))
        	inMainHand = true;
        org.bukkit.inventory.ItemStack compass = new org.bukkit.inventory.ItemStack(Material.COMPASS);
        CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
        compassMeta.setLodestoneTracked(false);
        compassMeta.setLodestone(location);
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Right Click to pick located player");
		lore.add(ChatColor.GRAY + "Left click to locate nearest player");
		compassMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Manhunt Compass");
		compassMeta.setLore(lore);
        compass.setItemMeta(compassMeta);
       
        if(inMainHand)
        	player.getInventory().setItemInMainHand(compass);
	}
	
	public void setCompassLocation(Player player, Location location, int slot) {
        boolean inMainHand = false;
        ItemStack item = player.getInventory().getItem(slot);
        if(item.getType().equals(Material.COMPASS))
        	inMainHand = true;
        org.bukkit.inventory.ItemStack compass = new org.bukkit.inventory.ItemStack(Material.COMPASS);
        CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
        compassMeta.setLodestoneTracked(false);
        compassMeta.setLodestone(location);
        compass.setItemMeta(compassMeta);
        List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Right Click to pick located player");
		lore.add(ChatColor.GRAY + "Left click to locate nearest player");
		compassMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Manhunt Compass");
		compassMeta.setLore(lore);
        if(inMainHand)
        	player.getInventory().setItem(slot, compass);
	}
	
	@SuppressWarnings("deprecation")
	public void switchTracker(Player player) {
		Player pastTarget = Bukkit.getPlayer(trackingWho.get(player.getUniqueId().toString()));
		if (Util.containsUUID(hunters, player.getUniqueId().toString())) {
			Inventory hunterTracking = null;
			if (speedrunners.size() <= 54) {
				hunterTracking = Bukkit.createInventory(null, (int)Math.ceil((1 + speedrunners.size())/9.0) * 9, ChatColor.GOLD + "" + ChatColor.BOLD + "Track Speedrunner");
				for (String id : speedrunners) {
					Player p = Bukkit.getPlayer(Util.getUUID(id));
					if (p == null)
						continue;
					ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
					SkullMeta meta = (SkullMeta) skull.getItemMeta();
					meta.setOwner(p.getName());
					List<String> lore = new ArrayList<String>();
					if (pastTarget == p) {
						meta.setDisplayName(ChatColor.WHITE + "Currently Tracking " + ChatColor.AQUA + "" + ChatColor.BOLD + p.getName());
					} else {
						meta.setDisplayName(ChatColor.WHITE + "Click to Track " + ChatColor.AQUA + "" + ChatColor.BOLD + p.getName());
					}
					lore.add(p.getUniqueId().toString());
					meta.setLore(lore);
					skull.setItemMeta(meta);
					hunterTracking.addItem(skull);
				}
				player.openInventory(hunterTracking);
			} else {
				player.sendMessage("Could not open tracking menu as there are more than 54 speedrunners.");
				locateNearestPlayer(player);
				updateCompassLocation(player);
				return;
			}
			
			
			
			
			
			
		} else if (Util.containsUUID(speedrunners, player.getUniqueId().toString())) {
			
		} else {
			
		}
	}
	
	public void updateCompassLocation(Player player) {
		if (trackingWho.get(player.getUniqueId().toString()) == null) {
			return;
		}
		setCompassLocation(player, playersDimensionLocation.get(trackingWho.get(player.getUniqueId().toString()) + "_" + player.getWorld().getName()));
	}
	
	public void updateCompassLocation(Player player, int slot) {
		if (trackingWho.get(player.getUniqueId().toString()) == null) {
			return;
		}
		setCompassLocation(player, playersDimensionLocation.get(trackingWho.get(player.getUniqueId().toString()) + "_" + player.getWorld().getName()), slot);
	}
	
	public void locateNearestPlayer(Player p) {
		double closestDistance = Double.MAX_VALUE;
		Player result = null;
		if (Util.containsUUID(hunters, p.getUniqueId().toString())) {
			for (String id : speedrunners) {
				Player player = Bukkit.getPlayer(Util.getUUID(id));
				if (player == null)
					continue;
				double distance = player.getLocation().distance(p.getLocation());
				if (distance < closestDistance) {
					result = player;
					closestDistance = distance;
				}
			}
			if (result == null) {
				return;
			}
			trackingWho.put(p.getUniqueId().toString(), result.getUniqueId().toString());
			
		} else if (Util.containsUUID(speedrunners, p.getUniqueId().toString())){
			
		} else {
			
		}
	}
	
	public void playerDeath(Player player) {
		if (Util.containsUUID(speedrunners, player.getUniqueId().toString())) {
			PlayerChatManager.addPlayerToTeam(player.getName(), "SPECTATORS");
			player.setGameMode(GameMode.CREATIVE);
			Util.removeAllUUID(speedrunners, player.getUniqueId().toString());
			Util.removeAllUUID(activePlayers, player.getUniqueId().toString());
			for (String id : hunters) {
				Player p = Bukkit.getPlayer(Util.getUUID(id));
				if (p == null)
					return;
				if (trackingWho.get(p.getUniqueId().toString()).equals(player.getUniqueId().toString())) {
					locateNearestPlayer(p);
				}
			}
			if (speedrunners.size() >= 1)
				Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt] " + ChatColor.RESET + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Only " + speedrunners.size() + " speedrunners remaining!");
			else {
				winningTeam = "Hunters";
				gameOver = true;
				float totalBets = 0;
				for (String id : players) {
					if (speedrunnerBets.get(id) == null) {
						continue;
					}
					PlayerRewardsHandler.playerInterfaceMappings.get(id).removeCoins(speedrunnerBets.get(id), ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap]" + ChatColor.RESET + " You lost your bet of " + speedrunnerBets.get(id) + " coins! :(");
					totalBets += speedrunnerBets.get(id);
				}
				
				for (String id : speedrunners) {
					PlayerRewardsHandler.playerInterfaceMappings.get(id).addCoins(totalBets/speedrunners.size(), ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap]" + ChatColor.RESET + " You earned " + (totalBets / speedrunners.size()) + " coins from the prize pool! :)", true);
				
				}
				for (String id : players) {
					Player p = Bukkit.getPlayer(Util.getUUID(id));
					if (p == null)
						continue;
					p.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "Congrats to " + winningTeam, ChatColor.DARK_RED + "" + ChatColor.BOLD + "for winning Manhunt and each earning" + (totalBets/speedrunners.size()) + "coins from the prize pool!", 10, 70, 20);
				}
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plug, new Runnable() {
					@Override
					public void run() {
						end();
					}
				}, 10L);
			}
		}
	}
	
	public void pause() {
		
	}
	
	public void unpause() {
		
	}
	
	public void givePlayerManhuntCompass(Player player) {
		ItemStack compass = new ItemStack(Material.COMPASS);
		ItemMeta meta = compass.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Right Click to pick located player");
		lore.add(ChatColor.GRAY + "Left click to locate nearest player");
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Manhunt Compass");
		meta.setLore(lore);
		compass.setItemMeta(meta);
		player.getInventory().addItem(compass);
	}
	
	@SuppressWarnings("deprecation")
	public boolean start() {
		/*
		 * Make sure every player has picked a team
		 */
		for (String id : players) {
			
			if (!Util.containsUUID(hunters, id) && !Util.containsUUID(speedrunners, id)) {
				Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.DARK_RED + "" + ChatColor.BOLD + (players.size() - hunters.size() - speedrunners.size()) + " players have yet to pick a team!");
				return false;
			}
		}
		if (speedrunners.size() == 0) {
			Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Nobody is on the speedrunner team!");
			return false;
		} else if (hunters.size() == 0) {
			Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Nobody is on the hunter team!");
			return false;
		}
		
		if (!Util.allUUIDOnline(players)) {
			Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Not all participants are online! Remove them or wait for them to log on to start!");
			return false;
		}
		
		super.start();
		hasStarted = true;
		PlayerChatManager.createTeamChat("SPEEDRUNNERS");
		PlayerChatManager.createTeamChat("HUNTERS");
		PlayerChatManager.createTeamChat("SPECTATORS");
		
		World world = Bukkit.getWorld("world");
		world.setDifficulty(Difficulty.NORMAL);
		world.setTime(0);
		for (Entity e : world.getEntities()) {
			if (e instanceof Player) {
				((Player) e).teleport(Bukkit.getWorld("lobby").getSpawnLocation());
			} else {
				if (e instanceof Monster || e instanceof Item) {
					e.remove();
				}
			}
		}
		if (Bukkit.getPlayer(Util.getUUID(gameMaster)) != null) {
			Bukkit.getPlayer(Util.getUUID(gameMaster)).closeInventory();
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
	        
	        activePlayers.add(id);
	        
	        /*
	         * Load locations
	         */
	        
	        playersDimensionLocation.put(p.getUniqueId().toString() + "_world", Bukkit.getWorld("world").getSpawnLocation());
	        playersDimensionLocation.put(p.getUniqueId().toString() + "_world_nether", Bukkit.getWorld("world_nether").getSpawnLocation());
	        playersDimensionLocation.put(p.getUniqueId().toString() + "_world_the_end", Bukkit.getWorld("world_the_end").getSpawnLocation());
	        
	        /*
	         * Load tracking who
	         */
	        
	        
	        Location loc = world.getSpawnLocation();
	        if (Util.containsUUID(speedrunners, id)) {
	        	PlayerChatManager.addPlayerToTeam(p.getName(), "SPEEDRUNNERS");
	        	trackingWho.put(p.getUniqueId().toString(), hunters.get(0));
	        	p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt] " + ChatColor.RESET + ChatColor.AQUA + "You are a speedrunner! Beat the enderdragon to win, if you die you lose! Move quick, the hunters will begin hunting in " + hunterWaitTime + " seconds!");
	        	
	        } else {
	        	trackingWho.put(p.getUniqueId().toString(), speedrunners.get(0));
	        	PlayerChatManager.addPlayerToTeam(p.getName(), "HUNTERS");
	        	p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt] " + ChatColor.RESET + ChatColor.DARK_RED + "You are a hunter! Stop the speedrunners from beating the enderdragon! Hold the compass and right click to locate the next speedrunner and left click to locate the nearest speedrunner!  You can begin moving in " + hunterWaitTime + " seconds!");
	        	givePlayerManhuntCompass(p);
	        	updateCompassLocation(p);
	        	loc.setX(loc.getX() + 10);
	        	loc = world.getHighestBlockAt(loc).getLocation();
	        	loc.setY(loc.getY() + 1);
	        }
			
			p.teleport(loc);
			playersDimensionLocation.put(p.getUniqueId().toString() + "_" + p.getWorld().getName(), p.getLocation());
		} 
		
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(p, new Runnable() {
			public void run() {
				if (!gameOver) {
					Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt] " + ChatColor.RESET + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Hunters can move!");
					huntersCanMove = true;
				} 
			}
		},(hunterWaitTime * 20L));
		
		
		updateCompass();
		startRewards();
		
		Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt] " + ChatColor.RESET + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Start speedrunning Speedrunners! Good luck!");
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
			pi.addCoins(PlayerRewardsHandler.COINS_PER_MINUTE_MANHUNT * PlayerRewardsHandler.COINS_MULTIPLIER, ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt] " + ChatColor.RESET + ChatColor.YELLOW + "You gained ", true);
			pi.addXP(PlayerRewardsHandler.XP_PER_MINUTE_MANHUNT * PlayerRewardsHandler.XP_MULTIPLIER, ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt] " + ChatColor.RESET + ChatColor.YELLOW + "You gained ", true);
		}
	}
	
	public void updateCompass() {
		
		
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plug, new Runnable() {
			@Override
			public void run() {
				for (String id : hunters) {
					Player p = Bukkit.getPlayer(Util.getUUID(id));
					if(p == null)
						continue;
					if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
						updateCompassLocation(p);
					}
				}
				if (gameOver == false) {
					updateCompass();
				}
			}
		},10L);
	}
	
	public boolean end() {
		PlayerChatManager.deleteTeamChat("SPEEDRUNNERS");
		PlayerChatManager.deleteTeamChat("HUNTERS");
		PlayerChatManager.deleteTeamChat("SPECTATORS");
		Main.mainTab.setRunType(0);
		gameOver = true;
		
		if (winningTeam == null) {
			TextComponent message = new TextComponent(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt]" + ChatColor.RESET + " Cancelled Game! :(");
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
		        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plug, new Runnable() {

					@Override
					public void run() {
						p.teleport(Bukkit.getWorld("lobby").getSpawnLocation());
					}
		        	
		        },10L);
			}
		}
		players.clear();
		Main.currentGame = null;
		Util.updateAllPlayers();
		return false;
	}
	
	public void showMenu(Player player) {
		if (player.getUniqueId().toString().equals(gameMaster)) {
			player.openInventory(gameMenuAdmin);
		} else {
			player.openInventory(gameMenuPlayers);
		}
	}
	
	public boolean hunterCanMove() {
		return huntersCanMove;
	}
	
	public boolean initModifiers() {
		
		
		return true;
	}
}
