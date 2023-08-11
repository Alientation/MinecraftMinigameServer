package me.Alientation.AlienPlugin.Listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import me.Alientation.AlienPlugin.Main;
import me.Alientation.AlienPlugin.Manhunt;
import me.Alientation.AlienPlugin.Util.ChatInputGetter;
import me.Alientation.AlienPlugin.Util.PlayerChatManager;
import me.Alientation.AlienPlugin.Util.PlayerRewardsHandler;
import me.Alientation.AlienPlugin.Util.Util;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ManhuntListener implements Listener{
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (Main.currentGame == null || !Main.currentGame.name.equals("manhunt") || event.getClickedInventory() == null)
			return;
		Manhunt game = (Manhunt) Main.currentGame;
		Player player = (Player) event.getWhoClicked();
		if (!event.getClickedInventory().equals(game.gameMenuAdmin)) {
			if (event.getClickedInventory().equals(game.gameMenuPlayers)) {
				if (event.getCurrentItem() == null) return;
				if (event.getCurrentItem().getItemMeta() == null) return;
				if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
				event.setCancelled(true);
				switch(event.getSlot()) {
				case 0:
					if (Util.containsUUID(game.speedrunners, player.getUniqueId().toString())) {
						player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt]" + ChatColor.RESET + " You have already joined the speedrunners!");
					} else {
						Util.removeAllUUID(game.hunters, player.getUniqueId().toString());
						Util.removeAllUUID(game.players, player.getUniqueId().toString());
						game.players.add(player.getUniqueId().toString());
						game.speedrunners.add(player.getUniqueId().toString());
						player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt]" + ChatColor.RESET + " You joined the speedrunners!");
						for (String id : game.players) {
							Player p = Bukkit.getPlayer(Util.getUUID(id));
							if (p == null)
								continue;
							if (!p.getUniqueId().toString().equals(player.getUniqueId().toString())) {
								p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt]" + ChatColor.RESET + player.getName() + " joined the speedrunners!");
							}
						}
					}
					break;
				case 1:
					if (Util.containsUUID(game.hunters, player.getUniqueId().toString())) {
						player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt]" + ChatColor.RESET + " You have already joined the hunters!");
					} else {
						Util.removeAllUUID(game.speedrunners, player.getUniqueId().toString());
						Util.removeAllUUID(game.players, player.getUniqueId().toString());
						game.players.add(player.getUniqueId().toString());
						game.hunters.add(player.getUniqueId().toString());
						player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt]" + ChatColor.RESET + " You joined the hunters!");
						for (String id : game.players) {
							Player p = Bukkit.getPlayer(Util.getUUID(id));
							if (p == null)
								continue;
							if (!p.getUniqueId().toString().equals(player.getUniqueId().toString())) {
								p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt]" + ChatColor.RESET + player.getName() + " joined the hunters!");
							}
						}
					}
					break;
				case 2:
					break;
				case 3:
					if (!Util.containsUUID(game.hunters, player.getUniqueId().toString()) && !Util.containsUUID(game.speedrunners, player.getUniqueId().toString())) {
						player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You have not joined a team yet!");
						return;
					}
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
							if (Util.containsUUID(game.speedrunners, player.getUniqueId().toString())) {
								game.speedrunnerBets.put(player.getUniqueId().toString(), bet);
							} else if (Util.containsUUID(game.hunters, player.getUniqueId().toString())) {
								game.hunterBets.put(player.getUniqueId().toString(), bet);
							}
							player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You placed a bet of " + bet + " coins!");
							Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plug, new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Util.createBoard(player);
								}
							});
							return true;
						}
					});
					
					player.closeInventory();
					return;
				case 4:
					break;
				case 5:
					break;
				case 6:
					break;
				case 7:
					break;
				case 8:
					Util.removeAllUUID(game.speedrunners, player.getUniqueId().toString());
					Util.removeAllUUID(game.hunters, player.getUniqueId().toString());
					Util.removeAllUUID(game.players, player.getUniqueId().toString());
					Util.removeAllUUID(game.activePlayers, player.getUniqueId().toString());
					game.speedrunnerBets.remove(player.getUniqueId().toString());
					game.hunterBets.remove(player.getUniqueId().toString());
					player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt]" + ChatColor.RESET + " You left the Manhunt game! :(");
					for (String id : game.players) {
						Player p = Bukkit.getPlayer(Util.getUUID(id));
						if (p == null)
							continue;
						p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt]" + ChatColor.RESET + player.getName() + " has left the Manhunt game! :(");
					}
					break;
				default:
					break;
				}
				game.updateMenu();
				player.closeInventory();
			} else if (event.getView().getTitle().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Track Speedrunner")) {
				event.setCancelled(true);
				ItemStack skull = event.getCurrentItem();
				if (skull == null || skull.getItemMeta().getLore() == null) {
					return;
				}
				Player p = Bukkit.getPlayer(UUID.fromString(skull.getItemMeta().getLore().get(0)));
				if (p != null) {
					game.trackingWho.put(player.getUniqueId().toString(), p.getUniqueId().toString());
					game.updateCompassLocation(player);
				}
				player.closeInventory();
			} else if (event.getView().getTitle().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Modifier Settings")) {
				event.setCancelled(true);
				switch(event.getSlot()) {
				case 0:
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
					break;
				case 7:
					break;
				case 8:
					break;
				case 9:
					break;
				case 10:
					break;
				case 11:
					break;
				case 12:
					break;
				case 13:
					break;
				case 14:
					break;
				case 15:
					break;
				case 16:
					break;
				case 17:
					break;
				case 18:
					break;
				case 19:
					break;
				case 20:
					break;
				case 21:
					break;
				case 22:
					player.closeInventory();
					game.showMenu(player);
					break;
				case 23:
					break;
				case 24:
					break;
				case 25:
					break;
				case 26:
					break;
				default:
					break;
				}
				return;
			} else if (event.getView().getTitle().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Player List")) {
				event.setCancelled(true);
				
				ItemStack skull = event.getCurrentItem();
				if (skull == null || skull.getItemMeta().getLore() == null) {
					return;
				}
				Util.removeAllUUID(game.players, skull.getItemMeta().getLore().get(0));
				Util.removeAllUUID(game.hunters, skull.getItemMeta().getLore().get(0));
				Util.removeAllUUID(game.speedrunners, skull.getItemMeta().getLore().get(0));
				game.speedrunnerBets.remove(skull.getItemMeta().getLore().get(0));
				game.hunterBets.remove(skull.getItemMeta().getLore().get(0));
				game.updateMenu();
				for (String id : game.players) {
					Player p = Bukkit.getPlayer(Util.getUUID(id));
					if (p == null) {
						break;
					}
					if (p.getOpenInventory().getTitle().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Manhunt Game")) {
						p.closeInventory();
						p.openInventory(game.gameMenuPlayers);
					}
				}
				game.openPlayerList((Player)event.getWhoClicked());
			}
		} else {
			if (event.getCurrentItem() == null) return;
			if (event.getCurrentItem().getItemMeta() == null) return;
			if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
			event.setCancelled(true);
			switch(event.getSlot()) {
			case 0:
				game.start();
				break;
			case 1:
				if (event.getClick() == ClickType.LEFT) {
					game.hunterWaitTime = Math.max(1,game.hunterWaitTime + 1);
					TextComponent message = new TextComponent(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt]" + ChatColor.RESET + " Hunter wait time set to " + game.hunterWaitTime + " seconds!");
					
					player.spigot().sendMessage(message);
				} else if (event.getClick() == ClickType.RIGHT) {
					game.hunterWaitTime = Math.max(1,game.hunterWaitTime - 1);
					TextComponent message = new TextComponent(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt]" + ChatColor.RESET + " Hunter wait time set to " + game.hunterWaitTime + " seconds!");
					
					player.spigot().sendMessage(message);
				}
				break;
			case 2:
				break;
			case 3:
				if (!Util.containsUUID(game.hunters, player.getUniqueId().toString()) && !Util.containsUUID(game.speedrunners, player.getUniqueId().toString())) {
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You have not joined a team yet!");
					return;
				}
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
						if (Util.containsUUID(game.speedrunners, player.getUniqueId().toString())) {
							game.speedrunnerBets.put(player.getUniqueId().toString(), bet);
						} else if (Util.containsUUID(game.hunters, player.getUniqueId().toString())) {
							game.hunterBets.put(player.getUniqueId().toString(), bet);
						}
						player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You placed a bet of " + bet + " coins!");
						
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plug, new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Util.createBoard(player);
							}
						});
						
						return true;
					}
				});
				
				player.closeInventory();
				return;
			case 4:
				game.openModifierMenu(player);
				return;
			case 5:
				if (Util.containsUUID(game.speedrunners, player.getUniqueId().toString())) {
					player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt]" + ChatColor.RESET + " You have already joined the speedrunners!");
				} else {
					Util.removeAllUUID(game.hunters, player.getUniqueId().toString());
					Util.removeAllUUID(game.players, player.getUniqueId().toString());
					game.players.add(player.getUniqueId().toString());
					game.speedrunners.add(player.getUniqueId().toString());
					player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt]" + ChatColor.RESET + " You joined the speedrunners!");
					for (String id : game.players) {
						Player p = Bukkit.getPlayer(Util.getUUID(id));
						if (p == null)
							continue;
						if (!p.getUniqueId().toString().equals(player.getUniqueId().toString())) {
							p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt]" + ChatColor.RESET + player.getName() + " joined the speedrunners!");
						}
					}
				}
				break;
			case 6:
				if (Util.containsUUID(game.hunters, player.getUniqueId().toString())) {
					player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt]" + ChatColor.RESET + " You have already joined the hunters!");
				} else {
					Util.removeAllUUID(game.speedrunners, player.getUniqueId().toString());
					Util.removeAllUUID(game.players, player.getUniqueId().toString());
					game.players.add(player.getUniqueId().toString());
					game.hunters.add(player.getUniqueId().toString());
					player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt]" + ChatColor.RESET + " You joined the hunters!");
					for (String id : game.players) {
						Player p = Bukkit.getPlayer(Util.getUUID(id));
						if (p == null)
							continue;
						if (!p.getUniqueId().toString().equals(player.getUniqueId().toString())) {
							p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Manhunt]" + ChatColor.RESET + player.getName() + " joined the hunters!");
						}
					}
				}
				break;
			case 7:
				player.closeInventory();
				game.openPlayerList(player);
				return;
			case 8:
				for (String id : game.players) {
					Player p = Bukkit.getPlayer(Util.getUUID(id));
					if (p == null)
						continue;
					p.closeInventory();
				}
				game.end();
				break;
			default:
				break;
			}
			if (!game.gameOver && game.hasStarted == false) {
				game.updateMenu();
				game.showMenu(player);
			}
		}
	}
	
	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent event) {
		if (Main.currentGame == null || !Main.currentGame.name.equals("manhunt")) {
			return;
		}
		Manhunt game = ((Manhunt)Main.currentGame);
		ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
		if (item == null || game.hasStarted == false || item == null) {
			return;
		}
		if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Manhunt Compass") && Util.containsUUID(((Manhunt) Main.currentGame).hunters, event.getPlayer().getUniqueId().toString())) {
			game.updateCompassLocation(event.getPlayer(), event.getNewSlot());
			if (game.trackingWho.get(event.getPlayer().getUniqueId().toString()) == null || Bukkit.getPlayer(Util.getUUID(game.trackingWho.get(event.getPlayer().getUniqueId().toString()))) == null) {
				return;
			}
			event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Locating... " + Bukkit.getPlayer(Util.getUUID(game.trackingWho.get(event.getPlayer().getUniqueId().toString()))).getName()));
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (Main.currentGame == null || !Main.currentGame.name.equals("manhunt") || Main.currentGame.hasStarted == false) {
			return;
		}
		Manhunt game = ((Manhunt)Main.currentGame);
		if (Util.containsUUID(game.hunters, event.getEntity().getUniqueId().toString())) {
			PlayerRewardsHandler.playerInterfaceMappings.get(event.getEntity().getUniqueId().toString()).getStats().addData("hunterManhuntDeaths", 1);
			if (event.getEntity().getKiller() != null) {
				PlayerRewardsHandler.playerInterfaceMappings.get(event.getEntity().getKiller().getUniqueId().toString()).getStats().addData("speedrunnerManhuntKills", 1);
				PlayerRewardsHandler.playerInterfaceMappings.get(event.getEntity().getKiller().getUniqueId().toString()).addCoins(PlayerRewardsHandler.COINS_PER_KILL_DEATHSWAP, ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.YELLOW + "You gained ", true);
				PlayerRewardsHandler.playerInterfaceMappings.get(event.getEntity().getKiller().getUniqueId().toString()).addXP(PlayerRewardsHandler.XP_PER_KILL_DEATHSWAP, ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.YELLOW + "You gained ", true);
			}
		} else if (Util.containsUUID(game.speedrunners, event.getEntity().toString())) {
			PlayerRewardsHandler.playerInterfaceMappings.get(event.getEntity().getUniqueId().toString()).getStats().addData("speedrunnerManhuntDeaths", 1);
			if (event.getEntity().getKiller() != null) {
				PlayerRewardsHandler.playerInterfaceMappings.get(event.getEntity().getKiller().getUniqueId().toString()).getStats().addData("hunterManhuntKills", 1);
				PlayerRewardsHandler.playerInterfaceMappings.get(event.getEntity().getKiller().getUniqueId().toString()).addCoins(PlayerRewardsHandler.COINS_PER_KILL_DEATHSWAP, ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.YELLOW + "You gained ", true);
				PlayerRewardsHandler.playerInterfaceMappings.get(event.getEntity().getKiller().getUniqueId().toString()).addXP(PlayerRewardsHandler.XP_PER_KILL_DEATHSWAP, ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.YELLOW + "You gained ", true);
			}
		}
		game.playerDeath(event.getEntity());
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if (Main.currentGame == null || Main.currentGame.hasStarted == false || !Main.currentGame.name.equals("manhunt")) {
			return;
		}
		if (event.getRespawnLocation().getWorld().getName().equals("lobby")) {
			event.setRespawnLocation(Bukkit.getWorld("world").getSpawnLocation());
		}
			
		((Manhunt)Main.currentGame).givePlayerManhuntCompass(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerUse(PlayerInteractEvent event) {
		if (Main.currentGame == null || !Main.currentGame.name.equals("manhunt") || event.getPlayer().getInventory().getItemInMainHand() == null || Main.currentGame.hasStarted == false) {
			return;
		}
		Manhunt game = ((Manhunt)Main.currentGame);
		if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.COMPASS && Util.containsUUID(((Manhunt) Main.currentGame).hunters, event.getPlayer().getUniqueId().toString())) {
			if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
				/*
				 *locate nearest player
				 */
				game.locateNearestPlayer(event.getPlayer());
				game.updateCompassLocation(event.getPlayer());
				if (game.trackingWho.get(event.getPlayer().getUniqueId().toString()) == null || Bukkit.getPlayer(Util.getUUID(game.trackingWho.get(event.getPlayer().getUniqueId().toString()))) == null) {
					return;
				}
				event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Locating Nearest Player... " + Bukkit.getPlayer(Util.getUUID(game.trackingWho.get(event.getPlayer().getUniqueId().toString()))).getName()));
			} else {
				/*
				 * pick located player
				 */
				game.switchTracker(event.getPlayer());
				game.updateCompassLocation(event.getPlayer());
				if (game.trackingWho.get(event.getPlayer().getUniqueId().toString()) == null || Bukkit.getPlayer(Util.getUUID(game.trackingWho.get(event.getPlayer().getUniqueId().toString()))) == null) {
					return;
				}
				event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Switching to... " + Bukkit.getPlayer(Util.getUUID(game.trackingWho.get(event.getPlayer().getUniqueId().toString()))).getName()));
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (Main.currentGame == null || !Main.currentGame.name.equals("manhunt")) {
			return;
		} else {
			Manhunt game = ((Manhunt)Main.currentGame);
			game.playersDimensionLocation.put(event.getPlayer().getUniqueId() + "_" + event.getPlayer().getWorld().getName(), event.getPlayer().getLocation());
			if (!game.hunterCanMove() && Util.containsUUID(game.hunters, event.getPlayer().getUniqueId().toString()) && game.hasStarted == true) {
				event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Wait, you can't move yet!"));
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onEnderDragonDeath(EntityDeathEvent e) {
		if (Main.currentGame == null || !Main.currentGame.name.equals("manhunt")) {
			return;
		} else {
			if (e.getEntity() instanceof EnderDragon) {
				Manhunt game = (Manhunt) Main.currentGame;
				game.winningTeam = "Speedrunners";
				game.end();
			}
		}
	}
	
	@EventHandler
	public void onHunterHitDuringWaitTime(EntityDamageByEntityEvent event) {
		if (Main.currentGame == null || !Main.currentGame.name.equals("manhunt")) {
			return;
		}
		Manhunt game = (Manhunt) Main.currentGame;
		if (game.huntersCanMove == false && Util.containsUUID(game.hunters, event.getEntity().getUniqueId().toString())) {
			event.setCancelled(true);
		} else if (event.getDamager() instanceof Player && !Util.containsUUID(game.activePlayers, event.getDamager().getUniqueId().toString())) {
			event.setCancelled(true);
		}
		
	}
}
