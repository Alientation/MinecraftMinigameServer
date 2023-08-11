package me.Alientation.AlienPlugin.Util.GUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.entity.Player;

import me.Alientation.AlienPlugin.DeathSwap;
import me.Alientation.AlienPlugin.Game;
import me.Alientation.AlienPlugin.Main;
import me.Alientation.AlienPlugin.Modifiers.Modifier;
import me.Alientation.AlienPlugin.Util.ChatInputGetter;
import me.Alientation.AlienPlugin.Util.PlayerChatManager;
import me.Alientation.AlienPlugin.Util.PlayerRewardsHandler;
import me.Alientation.AlienPlugin.Util.Util;

public class DeathSwapGameAdminGUI extends GUI{
	
	private Game game;
	
	public DeathSwapGameAdminGUI(String name, int size, String id, Game game) {
		super(name, size, id);
		this.game = game;
	}
	
	public boolean update() {
		DeathSwap deathSwap = (DeathSwap) this.game;
		List<String> lore = new ArrayList<String>();
		
		
		lore.add(ChatColor.GRAY + "Player list!");
		lore.add(ChatColor.GRAY + "--------------------------");
		for (String playerID : deathSwap.players) {
			Player p = Bukkit.getPlayer(Util.getUUID(playerID));
			if (p == null) {
				lore.add("  ♦ " + ChatColor.GRAY + " OFFLINE PLAYER - " + playerID);
			} else {
				lore.add("  ♦ " + ChatColor.GRAY + p.getName());
			}
		}
		
		super.addItem(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Begin Game!", Material.GREEN_WOOL, lore, new ClickedAction() {
			@Override
			public boolean run(Player player) {
				for (String playerID : deathSwap.players) {
					if (Bukkit.getPlayer(Util.getUUID(playerID)) != null) {
						Bukkit.getPlayer(Util.getUUID(playerID)).closeInventory();
					}
				}
				deathSwap.start();
				return true;
			}
		}, 0);
		lore.clear();
		
		lore.add(ChatColor.GRAY + "Click to set the swap time!");
		super.addItem(ChatColor.WHITE + "" + ChatColor.BOLD + "Change Swap Time (Current " + deathSwap.swapSeconds + ")", Material.CLOCK, lore, new ClickedAction() {
			@Override
			public boolean run(Player player) {
				player.closeInventory();
				player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap]" + ChatColor.RESET + " Enter new Swap Time!");
				PlayerChatManager.inputRequired.put(player.getUniqueId().toString(), new ChatInputGetter() {
					@Override
					public boolean playerInput(Player player, String message) {
						if (NumberUtils.isParsable(message)) {
							int number = (int) Double.parseDouble(message);
							deathSwap.swapSeconds = number;
							GUIHandler.updateGUI("DeathSwapGameAdminGUI");
							GUIHandler.updateGUI("DeathSwapModifierGUI");
							GUIHandler.updateGUI("DeathSwapPlayerGUI");
							for (String playerID : deathSwap.players) {
								if (Bukkit.getPlayer(Util.getUUID(playerID)) != null) {
									Player p = Bukkit.getPlayer(Util.getUUID(playerID));
									if (p.getOpenInventory() != null && p.getOpenInventory().getTitle().equals(name)) {
										GUIHandler.openGUI(p, "DeathSwapPlayerGUI");
									}
								}
							}
						} else {
							player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap]" + ChatColor.RESET + " That is not a valid numerical input!");
							return false;
						}
						return true;
					}
				});
				return true;
			}
		}, 1);
		lore.clear();
		
		lore.add(ChatColor.GRAY + "Click to set the spawn radius!");
		super.addItem(ChatColor.WHITE + "" + ChatColor.BOLD + "Change Spawn Radius (Current " + deathSwap.spawnRadius + ")", Material.NETHER_STAR, lore, new ClickedAction() {
			@Override
			public boolean run(Player player) {
				player.closeInventory();
				player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap]" + ChatColor.RESET + " Enter new Spawn Radius!");
				PlayerChatManager.inputRequired.put(player.getUniqueId().toString(), new ChatInputGetter() {
					@Override
					public boolean playerInput(Player player, String message) {
						if (NumberUtils.isParsable(message)) {
							int number = (int) Double.parseDouble(message);
							deathSwap.spawnRadius = number;
							GUIHandler.updateGUI("DeathSwapGameAdminGUI");
							GUIHandler.updateGUI("DeathSwapModifierGUI");
							GUIHandler.updateGUI("DeathSwapPlayerGUI");
							for (String playerID : deathSwap.players) {
								if (Bukkit.getPlayer(Util.getUUID(playerID)) != null) {
									Player p = Bukkit.getPlayer(Util.getUUID(playerID));
									if (p.getOpenInventory() != null && p.getOpenInventory().getTitle().equals(name)) {
										GUIHandler.openGUI(p, "DeathSwapPlayerGUI");
									}
								}
							}
						} else {
							player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap]" + ChatColor.RESET + " That is not a valid numerical input!");
							return false;
						}
						return true;
					}
				});
				return true;
			}
		}, 2);
		lore.clear();
		
		lore.add(ChatColor.GRAY + "Click to set the spawn center!");
		super.addItem(ChatColor.WHITE + "" + ChatColor.BOLD + "Change Spawn Center (Current " + deathSwap.spawnCenterX + "," + deathSwap.spawnCenterZ + ")", Material.BLUE_BED, lore, new ClickedAction() {
			@Override
			public boolean run(Player player) {
				player.closeInventory();
				player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap]" + ChatColor.RESET + " Enter new Spawn Center! <x,z>");
				PlayerChatManager.inputRequired.put(player.getUniqueId().toString(), new ChatInputGetter() {
					@Override
					public boolean playerInput(Player player, String message) {
						String[] list = message.split(",");
						if (list.length == 2 && NumberUtils.isParsable(list[0]) && NumberUtils.isParsable(list[1])) {
							int x = (int) Double.parseDouble(list[0]);
							int z = (int) Double.parseDouble(list[1]);
							deathSwap.spawnCenterX = x;
							deathSwap.spawnCenterZ = z;
							GUIHandler.updateGUI("DeathSwapGameAdminGUI");
							GUIHandler.updateGUI("DeathSwapModifierGUI");
							GUIHandler.updateGUI("DeathSwapPlayerGUI");
							for (String playerID : deathSwap.players) {
								if (Bukkit.getPlayer(Util.getUUID(playerID)) != null) {
									Player p = Bukkit.getPlayer(Util.getUUID(playerID));
									if (p.getOpenInventory() != null && p.getOpenInventory().getTitle().equals(name)) {
										GUIHandler.openGUI(p, "DeathSwapPlayerGUI");
									}
								}
							}
						} else {
							player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap]" + ChatColor.RESET + " That is not a valid input!");
							return false;
						}
						return true;
					}
				});
				return true;
			}
		}, 3);
		lore.clear();
		
		lore.add(ChatColor.GRAY + "Current Game Modifiers");
		lore.add(ChatColor.GRAY + "-------------------------------");
		for (Modifier m : deathSwap.modifiers) {
			lore.add("  ♦ " + ChatColor.GRAY + "" + m.name);
		}
		super.addItem(ChatColor.WHITE + "" + ChatColor.BOLD + "Click to add Modifiers", Material.COMMAND_BLOCK, lore, new ClickedAction() {
			@Override
			public boolean run(Player player) {
				GUIHandler.openGUI(player, "DeathSwapModifierGUI");
				return true;
			}
		}, 4);
		lore.clear();

		lore.add(ChatColor.GRAY + "Bet List");
		lore.add(ChatColor.GRAY + "==============");
		for (String playerID : deathSwap.players) {
			if (deathSwap.playerBets.get(playerID) == null) {
				continue;
			}
			if (Bukkit.getPlayer(Util.getUUID(playerID)) == null) {
				lore.add("  ♦ " + ChatColor.GRAY + "" + ChatColor.ITALIC + deathSwap.playerBets.get(playerID) + " - OFFLINE PLAYER - " + playerID);
				continue;
			}
			lore.add("  ♦ " + ChatColor.GRAY + "" + ChatColor.ITALIC + deathSwap.playerBets.get(playerID) + " - " + Bukkit.getPlayer(Util.getUUID(playerID)).getName());
		}
		super.addItem(ChatColor.GREEN + "" + ChatColor.BOLD + "Bet Coins!", Material.GOLD_NUGGET, lore, new ClickedAction() {
			@Override
			public boolean run(Player player) {
				if (!Util.containsUUID(game.players, player.getUniqueId().toString()) ) {
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You have not joined the game yet!");
					return false;
				}
				player.closeInventory();
				player.sendMessage(ChatColor.WHITE + ">>> Input a bet amount <<<");
				PlayerChatManager.inputRequired.put(player.getUniqueId().toString(), new ChatInputGetter() {
					@Override
					public boolean playerInput(Player player, String message) {
						float bet = 0;
						try {
							bet = Float.parseFloat(message);
						} catch(Exception e) {
							player.sendMessage("That ain't a number");
							return false;
						}
						if (bet > PlayerRewardsHandler.playerInterfaceMappings.get(player.getUniqueId().toString()).getCoinBalance()) {
							player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You can not bet more coins than what you have!\nYou have " + PlayerRewardsHandler.playerInterfaceMappings.get(player.getUniqueId().toString()).getCoinBalance() + " coins!");
							return false;
						}
						player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You placed a bet of " + bet + " coins!");
						deathSwap.playerBets.put(player.getUniqueId().toString(), bet);
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plug, new Runnable() {
							@Override
							public void run() {
								Util.createBoard(player);
							}
						});
						GUIHandler.updateGUI("DeathSwapGameAdminGUI");
						GUIHandler.updateGUI("DeathSwapModifierGUI");
						GUIHandler.updateGUI("DeathSwapPlayerGUI");
						for (String playerID : deathSwap.players) {
							if (Bukkit.getPlayer(Util.getUUID(playerID)) != null) {
								Player p = Bukkit.getPlayer(Util.getUUID(playerID));
								if (p.getOpenInventory() != null && p.getOpenInventory().getTitle().equals(name)) {
									GUIHandler.openGUI(p, "DeathSwapPlayerGUI");
								}
							}
						}
						return true;
					}
				});
				
				player.closeInventory();
				return true;
			}
		}, 5);
		lore.clear();
		
		lore.add(ChatColor.GRAY + "Click to open player list!");
		super.addItem(ChatColor.WHITE + "" + ChatColor.BOLD + "Remove Players", Material.WRITABLE_BOOK, lore, new ClickedAction() {
			@Override
			public boolean run(Player player) {
				GUIHandler.openGUI(player, "DeathSwapPlayerListGUI");
				return true;
			}
		}, 7);
		lore.clear();
		
		lore.add(ChatColor.GRAY + "Click to cancel game create!");
		super.addItem(ChatColor.WHITE + "" + ChatColor.BOLD + "Cancel", Material.BARRIER, lore, new ClickedAction() {
			@Override
			public boolean run(Player player) {
				for (String playerID : deathSwap.players) {
					if (Bukkit.getPlayer(Util.getUUID(playerID)) != null) {
						Player p = Bukkit.getPlayer(Util.getUUID(playerID));
						if (p.getOpenInventory() != null && p.getOpenInventory().getTitle().equals(name)) {
							p.closeInventory();
						}
					}
				}
				deathSwap.end();
				return true;
			}
		}, 8);
		lore.clear();
		return true;
	}
	
}
