package me.Alientation.AlienPlugin.Listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.Alientation.AlienPlugin.DeathSwap;
import me.Alientation.AlienPlugin.Main;
import me.Alientation.AlienPlugin.Util.ChatInputGetter;
import me.Alientation.AlienPlugin.Util.PlayerChatManager;
import me.Alientation.AlienPlugin.Util.PlayerRewardsHandler;
import me.Alientation.AlienPlugin.Util.Util;
import net.md_5.bungee.api.chat.TextComponent;

public class DeathSwapListener implements Listener{
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (Main.currentGame == null || !Main.currentGame.name.equals("deathswap") || Main.currentGame.hasStarted == false)
			return;
		DeathSwap deathSwap = (DeathSwap) Main.currentGame;
		PlayerRewardsHandler.playerInterfaceMappings.get(e.getEntity().getUniqueId().toString()).getStats().addData("deathSwapDeaths", 1);
		if (e.getEntity().getKiller() != null) {
			PlayerRewardsHandler.playerInterfaceMappings.get(e.getEntity().getKiller().getUniqueId().toString()).getStats().addData("deathSwapKills", 1);
			PlayerRewardsHandler.playerInterfaceMappings.get(e.getEntity().getKiller().getUniqueId().toString()).addCoins(PlayerRewardsHandler.COINS_PER_KILL_DEATHSWAP, ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.YELLOW + "You gained ", true);
			PlayerRewardsHandler.playerInterfaceMappings.get(e.getEntity().getKiller().getUniqueId().toString()).addXP(PlayerRewardsHandler.XP_PER_KILL_DEATHSWAP, ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap] " + ChatColor.RESET + ChatColor.YELLOW + "You gained ", true);
		}
		deathSwap.playerDeath(e.getEntity());
	}
	
	
	@EventHandler()
	public void onClick(InventoryClickEvent event) {
		if (Main.currentGame == null || !Main.currentGame.name.equals("deathswap") || event.getClickedInventory() == null)
			return;
		DeathSwap game = (DeathSwap) Main.currentGame;
		Player player = (Player) event.getWhoClicked();
		
		
		if (!event.getClickedInventory().equals(((DeathSwap) Main.currentGame).gameMenuAdmin)) {
			if (event.getClickedInventory().equals(((DeathSwap) Main.currentGame).gameMenuPlayers)) {
				if (event.getCurrentItem() == null) return;
				if (event.getCurrentItem().getItemMeta() == null) return;
				if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
				event.setCancelled(true);
				switch(event.getSlot()) {
				case 0:
					game.addPlayer(player);
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					if (!Util.containsUUID(game.players, player.getUniqueId().toString()) ) {
						player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You have not joined the game yet!");
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
							player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You placed a bet of " + bet + " coins!");
							game.playerBets.put(player.getUniqueId().toString(), bet);
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
					game.playerBets.remove(player.getUniqueId().toString());
					game.removePlayer(player);
					break;
				default:
					break;
				}
				if (!game.gameOver) {
					game.updateMenu();
				}
				player.closeInventory();
			} else if (event.getView().getTitle().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Player List")) {
				event.setCancelled(true);
				
				ItemStack skull = event.getCurrentItem();
				if (skull == null || skull.getItemMeta().getLore() == null) {
					return;
				}
				Util.removeAllUUID(game.players, skull.getItemMeta().getLore().get(0));
				game.playerBets.remove(skull.getItemMeta().getLore().get(0));
				game.updateMenu();
				for (String id : game.players) {
					Player p = Bukkit.getPlayer(Util.getUUID(id));
					if (p == null) {
						break;
					}
					if (p.getOpenInventory().getTitle().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Death Swap Game")) {
						p.closeInventory();
						p.openInventory(game.gameMenuPlayers);
					}
				}
				game.openPlayerList((Player)event.getWhoClicked());
			} else {
				return;
			}
		} else {
			if (event.getCurrentItem() == null) return;
			if (event.getCurrentItem().getItemMeta() == null) return;
			if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
			
			event.setCancelled(true);
			if (event.getClick() == ClickType.RIGHT) {
				switch(event.getSlot()) {
				case 0:
					game.start();
					break;
				case 1:
					game.swapSeconds = Math.max(10,game.swapSeconds - 10);
					TextComponent message = new TextComponent(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap]" + ChatColor.RESET + " Swap time set to " + game.swapSeconds + " seconds!");
					
					player.spigot().sendMessage(message);
					break;
				case 2:
					game.spawnRadius = Math.max(50,game.spawnRadius - 50);
					message = new TextComponent(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap]" + ChatColor.RESET + " Spawn Radius set to " + game.spawnRadius + " blocks from center!");
					player.spigot().sendMessage(message);
					break;
				case 3:
					game.spawnCenterX = (int) (Math.random() * 1000);
					game.spawnCenterZ = (int) (Math.random() * 1000);
					message = new TextComponent(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap]" + ChatColor.RESET + " Spawn Center set to (" + game.spawnCenterX + ", " + game.spawnCenterZ + ")");
					player.spigot().sendMessage(message);
					break;
				case 4:
					game.openModifierMenu(player);
					break;
				case 5:
					if (!Util.containsUUID(game.players, player.getUniqueId().toString()) ) {
						player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You have not joined the game yet!");
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
							player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You placed a bet of " + bet + " coins!");
							game.playerBets.put(player.getUniqueId().toString(), bet);
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
				case 7:
					player.closeInventory();
					game.openPlayerList(player);
					return;
				case 8:
					game.end();
					player.closeInventory();
					break;
				default:
					break;
				}
			} else {
				switch(event.getSlot()) {
				case 0:
					game.start();
					break;
				case 1:
					game.swapSeconds = game.swapSeconds + 10;
					TextComponent message = new TextComponent(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap]" + ChatColor.RESET + " Swap time set to " + game.swapSeconds + " seconds!");
					player.spigot().sendMessage(message);
					break;
				case 2:
					game.spawnRadius = game.spawnRadius + 50;
					message = new TextComponent(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap]" + ChatColor.RESET + " Spawn Radius set to " + game.spawnRadius + " blocks from center!");
					player.spigot().sendMessage(message);
					break;
				case 3:
					game.spawnCenterX = (int) (Math.random() * 1000);
					game.spawnCenterZ = (int) (Math.random() * 1000);
					message = new TextComponent(ChatColor.GOLD + "" + ChatColor.BOLD + "[DeathSwap]" + ChatColor.RESET + " Spawn Center set to (" + game.spawnCenterX + ", " + game.spawnCenterZ + ")");
					player.spigot().sendMessage(message);
					break;
				case 5:
					if (!Util.containsUUID(game.players, player.getUniqueId().toString()) ) {
						player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You have not joined the game yet!");
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
							player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You placed a bet of " + bet + " coins!");
							game.playerBets.put(player.getUniqueId().toString(), bet);
							
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
			
			}
			if (!game.gameOver && game.hasStarted == false) {
				game.updateMenu();
				game.showMenu(player);
			}
		}
	}
	
	@EventHandler
	public void onHunterHitDuringWaitTime(EntityDamageByEntityEvent event) {
		if (Main.currentGame == null || !Main.currentGame.name.equals("deathswap")) {
			return;
		}
		DeathSwap game = (DeathSwap) Main.currentGame;
		if (event.getDamager() instanceof Player && !Util.containsUUID(game.activePlayers, event.getDamager().getUniqueId().toString())) {
			event.setCancelled(true);
		}
		
	}
	
}
