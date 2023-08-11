package me.Alientation.AlienPlugin.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import me.Alientation.AlienPlugin.DeathSwap;
import me.Alientation.AlienPlugin.FindTheItem;
import me.Alientation.AlienPlugin.Main;
import me.Alientation.AlienPlugin.Manhunt;
import me.Alientation.AlienPlugin.Util.NewCommands.RankCommand;

public class Util {
	
	public static void createBoard(Player player) {
		
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective obj = board.registerNewObjective("AlienPlugin", "dummy", ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "<< " + ChatColor.AQUA + ChatColor.BOLD +"Alien Plugin " + ChatColor.DARK_GREEN + ChatColor.BOLD + ">>");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		Score score = obj.getScore(ChatColor.BLUE + "=-=-=-=-=-=-=-=-=-=-=");
		score.setScore(3);
		if (!PlayerRewardsHandler.playerInterfaceMappings.containsKey(player.getUniqueId().toString())) {
			return;
		}
		
		if (Main.currentGame == null || !Util.containsUUID(Main.currentGame.players, player.getUniqueId().toString())) {
			Score score2 = obj.getScore(ChatColor.YELLOW +""+ ChatColor.BOLD + "Coins: " + ChatColor.WHITE + PlayerRewardsHandler.playerInterfaceMappings.get(player.getUniqueId().toString()).getCoinBalance());
			score2.setScore(2);
			Score score3 = obj.getScore(ChatColor.AQUA +""+ ChatColor.BOLD + "XP: " + ChatColor.WHITE + PlayerRewardsHandler.playerInterfaceMappings.get(player.getUniqueId().toString()).getXPBalance());
			score3.setScore(1);
			Score score4 = obj.getScore(ChatColor.AQUA +""+ ChatColor.BOLD + "Wins: " + ChatColor.WHITE + PlayerRewardsHandler.playerInterfaceMappings.get(player.getUniqueId().toString()).getStats().getIntegerData("winCount"));
			score4.setScore(0);
		} else if (Main.currentGame.name.equals("manhunt")) {
			Manhunt game = (Manhunt) Main.currentGame;
			if (Util.containsUUID(game.speedrunners, player.getUniqueId().toString())) {
				Score score2 = obj.getScore(ChatColor.AQUA + "SPEEDRUNNER");
				score2.setScore(2);
				Score score3 = obj.getScore(ChatColor.YELLOW + "<< TEAMMATES >>");
				score3.setScore(1);
				
				for (String id : game.speedrunners) {
					if (id.equals(player.getUniqueId().toString())) {
						continue;
					}
					if (Bukkit.getPlayer(id) != null) {
						Score score10 = obj.getScore(ChatColor.WHITE + "  ♦ " + Bukkit.getPlayer(id));
						score10.setScore(0);
					} else {
						Score score10 = obj.getScore(ChatColor.GRAY +""+ ChatColor.ITALIC + "  ♦ (OFFLINE) " + Bukkit.getPlayer(id));
						score10.setScore(-1);
					}
				}
				Score score4 = obj.getScore(ChatColor.GREEN +""+ ChatColor.BOLD + "COIN BET: " + ChatColor.YELLOW + Math.round(game.speedrunnerBets.getOrDefault(player.getUniqueId().toString(),0f)));
				score4.setScore(-2);
				
			} else if (Util.containsUUID(game.hunters, player.getUniqueId().toString())) {
				Score score2 = obj.getScore(ChatColor.RED + "HUNTER");
				score2.setScore(2);
				Score score3 = obj.getScore(ChatColor.YELLOW + "<< TEAMMATES >>");
				score3.setScore(1);
				
				for (String id : game.hunters) {
					if (id.equals(player.getUniqueId().toString())) {
						continue;
					}
					if (Bukkit.getPlayer(id) != null) {
						Score score10 = obj.getScore(ChatColor.WHITE + "  ♦ " + Bukkit.getPlayer(id));
						score10.setScore(0);
					} else {
						Score score10 = obj.getScore(ChatColor.GRAY +""+ ChatColor.ITALIC + "  ♦ (OFFLINE) " + Bukkit.getPlayer(id));
						score10.setScore(-1);
					}
				}
				Score score4 = obj.getScore(ChatColor.GREEN +""+ ChatColor.BOLD + "COIN BET: " + ChatColor.YELLOW + Math.round(game.hunterBets.getOrDefault(player.getUniqueId().toString(),(float) 0)));
				score4.setScore(-2);
			} else {
				Score score2 = obj.getScore(ChatColor.RED + "SPECTATOR");
				score2.setScore(2);
				Score score4 = obj.getScore(ChatColor.GREEN +""+ ChatColor.BOLD + "COIN BET: " + ChatColor.YELLOW + Math.max(Math.round(game.hunterBets.getOrDefault(player.getUniqueId().toString(),(float) 0)), Math.round(game.speedrunnerBets.getOrDefault(player.getUniqueId().toString(),(float) 0))));
				score4.setScore(-2);
			}
		} else if (Main.currentGame.name.equals("deathswap")) {
			DeathSwap game = (DeathSwap) Main.currentGame;
			if (Util.containsUUID(game.activePlayers, player.getUniqueId().toString())) {
				Score score2 = obj.getScore(ChatColor.RED + "DEATHSWAP");
				score2.setScore(2);
				Score score4 = obj.getScore(ChatColor.GREEN +""+ ChatColor.BOLD + "COIN BET: " + ChatColor.YELLOW + Math.round(game.playerBets.getOrDefault(player.getUniqueId().toString(), 0f)));
				score4.setScore(-2);
			} else {
				Score score2 = obj.getScore(ChatColor.RED + "SPECTATOR");
				score2.setScore(2);
				Score score4 = obj.getScore(ChatColor.GREEN +""+ ChatColor.BOLD + "COIN BET: " + ChatColor.YELLOW + Math.round(game.playerBets.getOrDefault(player.getUniqueId().toString(), 0f)));
				score4.setScore(-2);
			}
		}
		player.setScoreboard(board);
	}
	
	public static void loadTabManager(TabManager mainTab) {
		
		mainTab.addHeader(0,"&e&l" + "AlienPlugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "AlienPlugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "AlienPlugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "AlienPlugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "AlienPlugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "AlienPlugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "AlienPlugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "AlienPlugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "AlienPlugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "AlienPlugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "A" + "&e&l" + "lienPlugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "Al" + "&e&l" + "ienPlugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "Ali" + "&e&l" + "enPlugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "Alie" + "&e&l" + "nPlugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "Alien" + "&e&l" + "Plugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "AlienP" + "&e&l" + "lugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "AlienPl" + "&e&l" + "ugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "AlienPlu" + "&e&l" + "gin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "AlienPlug" + "&e&l" + "in\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "AlienPlugi" + "&e&l" + "n\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cWaiting for Admin to start game!");

		mainTab.addHeader(0,"&e&l" + "A" + "&f&l" + "lienPlugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "Al" + "&f&l" + "ienPlugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "Ali" + "&f&l" + "enPlugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "Alie" + "&f&l" + "nPlugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "Alien" + "&f&l" + "Plugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "AlienP" + "&f&l" + "lugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "AlienPl" + "&f&l" + "ugin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "AlienPlu" + "&f&l" + "gin\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "AlienPlug" + "&f&l" + "in\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "AlienPlugi" + "&f&l" + "n\n&r&cWaiting for Admin to start game!");
		mainTab.addHeader(0,"&e&l" + "AlienPlugin" + "&f&l" + "\n&r&cWaiting for Admin to start game!");
		
		mainTab.addFooter(0,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(0,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(0,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(0,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(0,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(0,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(0,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(0,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(0,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(0,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(0,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(0,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(0,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(0,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(0,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(0,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(0,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(0,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		
		mainTab.addFooter(0,"&b&dFun Games and More!");
		mainTab.addFooter(0,"&b&dFun Games and More!");
		mainTab.addFooter(0,"&b&dFun Games and More!");
		mainTab.addFooter(0,"&b&dFun Games and More!");
		mainTab.addFooter(0,"&b&dFun Games and More!");
		mainTab.addFooter(0,"&b&dFun Games and More!");
		mainTab.addFooter(0,"&b&dFun Games and More!");
		mainTab.addFooter(0,"&b&dFun Games and More!");
		mainTab.addFooter(0,"&b&dFun Games and More!");
		mainTab.addFooter(0,"&b&dFun Games and More!");
		mainTab.addFooter(0,"&b&dFun Games and More!");
		mainTab.addFooter(0,"&b&dFun Games and More!");
		mainTab.addFooter(0,"&b&3Fun Games and More!");
		mainTab.addFooter(0,"&b&3Fun Games and More!");
		mainTab.addFooter(0,"&b&3Fun Games and More!");
		mainTab.addFooter(0,"&b&3Fun Games and More!");
		mainTab.addFooter(0,"&b&3Fun Games and More!");
		mainTab.addFooter(0,"&b&3Fun Games and More!");
		mainTab.addFooter(0,"&b&3Fun Games and More!");
		mainTab.addFooter(0,"&b&3Fun Games and More!");
		mainTab.addFooter(0,"&b&3Fun Games and More!");
		mainTab.addFooter(0,"&b&3Fun Games and More!");
		mainTab.addFooter(0,"&b&3Fun Games and More!");
		mainTab.addFooter(0,"&b&3Fun Games and More!");
		
		
		mainTab.addHeader(1,"&e&l" + "AlienPlugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "AlienPlugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "AlienPlugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "AlienPlugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "AlienPlugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "AlienPlugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "AlienPlugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "AlienPlugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "AlienPlugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "AlienPlugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "A" + "&e&l" + "lienPlugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "Al" + "&e&l" + "ienPlugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "Ali" + "&e&l" + "enPlugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "Alie" + "&e&l" + "nPlugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "Alien" + "&e&l" + "Plugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "AlienP" + "&e&l" + "lugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "AlienPl" + "&e&l" + "ugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "AlienPlu" + "&e&l" + "gin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "AlienPlug" + "&e&l" + "in\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "AlienPlugi" + "&e&l" + "n\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying DeathSwap!");

		mainTab.addHeader(1,"&e&l" + "A" + "&f&l" + "lienPlugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "Al" + "&f&l" + "ienPlugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "Ali" + "&f&l" + "enPlugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "Alie" + "&f&l" + "nPlugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "Alien" + "&f&l" + "Plugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "AlienP" + "&f&l" + "lugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "AlienPl" + "&f&l" + "ugin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "AlienPlu" + "&f&l" + "gin\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "AlienPlug" + "&f&l" + "in\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "AlienPlugi" + "&f&l" + "n\n&r&cPlaying DeathSwap!");
		mainTab.addHeader(1,"&e&l" + "AlienPlugin" + "&f&l" + "\n&r&cPlaying DeathSwap!");
		
		mainTab.addFooter(1,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(1,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(1,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(1,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(1,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(1,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(1,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(1,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(1,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(1,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(1,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(1,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(1,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(1,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(1,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(1,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(1,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(1,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		
		mainTab.addFooter(1,"&b&dFun Games and More!");
		mainTab.addFooter(1,"&b&dFun Games and More!");
		mainTab.addFooter(1,"&b&dFun Games and More!");
		mainTab.addFooter(1,"&b&dFun Games and More!");
		mainTab.addFooter(1,"&b&dFun Games and More!");
		mainTab.addFooter(1,"&b&dFun Games and More!");
		mainTab.addFooter(1,"&b&dFun Games and More!");
		mainTab.addFooter(1,"&b&dFun Games and More!");
		mainTab.addFooter(1,"&b&dFun Games and More!");
		mainTab.addFooter(1,"&b&dFun Games and More!");
		mainTab.addFooter(1,"&b&dFun Games and More!");
		mainTab.addFooter(1,"&b&dFun Games and More!");
		mainTab.addFooter(1,"&b&3Fun Games and More!");
		mainTab.addFooter(1,"&b&3Fun Games and More!");
		mainTab.addFooter(1,"&b&3Fun Games and More!");
		mainTab.addFooter(1,"&b&3Fun Games and More!");
		mainTab.addFooter(1,"&b&3Fun Games and More!");
		mainTab.addFooter(1,"&b&3Fun Games and More!");
		mainTab.addFooter(1,"&b&3Fun Games and More!");
		mainTab.addFooter(1,"&b&3Fun Games and More!");
		mainTab.addFooter(1,"&b&3Fun Games and More!");
		mainTab.addFooter(1,"&b&3Fun Games and More!");
		mainTab.addFooter(1,"&b&3Fun Games and More!");
		mainTab.addFooter(1,"&b&3Fun Games and More!");
		
		mainTab.addHeader(2,"&e&l" + "AlienPlugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "AlienPlugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "AlienPlugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "AlienPlugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "AlienPlugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "AlienPlugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "AlienPlugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "AlienPlugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "AlienPlugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "AlienPlugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "A" + "&e&l" + "lienPlugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "Al" + "&e&l" + "ienPlugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "Ali" + "&e&l" + "enPlugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "Alie" + "&e&l" + "nPlugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "Alien" + "&e&l" + "Plugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "AlienP" + "&e&l" + "lugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "AlienPl" + "&e&l" + "ugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "AlienPlu" + "&e&l" + "gin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "AlienPlug" + "&e&l" + "in\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "AlienPlugi" + "&e&l" + "n\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Manhunt!");

		mainTab.addHeader(2,"&e&l" + "A" + "&f&l" + "lienPlugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "Al" + "&f&l" + "ienPlugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "Ali" + "&f&l" + "enPlugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "Alie" + "&f&l" + "nPlugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "Alien" + "&f&l" + "Plugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "AlienP" + "&f&l" + "lugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "AlienPl" + "&f&l" + "ugin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "AlienPlu" + "&f&l" + "gin\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "AlienPlug" + "&f&l" + "in\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "AlienPlugi" + "&f&l" + "n\n&r&cPlaying Manhunt!");
		mainTab.addHeader(2,"&e&l" + "AlienPlugin" + "&f&l" + "\n&r&cPlaying Manhunt!");
		
		mainTab.addFooter(2,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(2,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(2,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(2,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(2,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(2,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(2,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(2,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(2,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(2,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(2,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(2,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(2,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(2,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(2,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(2,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(2,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(2,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		
		mainTab.addFooter(2,"&b&dFun Games and More!");
		mainTab.addFooter(2,"&b&dFun Games and More!");
		mainTab.addFooter(2,"&b&dFun Games and More!");
		mainTab.addFooter(2,"&b&dFun Games and More!");
		mainTab.addFooter(2,"&b&dFun Games and More!");
		mainTab.addFooter(2,"&b&dFun Games and More!");
		mainTab.addFooter(2,"&b&dFun Games and More!");
		mainTab.addFooter(2,"&b&dFun Games and More!");
		mainTab.addFooter(2,"&b&dFun Games and More!");
		mainTab.addFooter(2,"&b&dFun Games and More!");
		mainTab.addFooter(2,"&b&dFun Games and More!");
		mainTab.addFooter(2,"&b&dFun Games and More!");
		mainTab.addFooter(2,"&b&3Fun Games and More!");
		mainTab.addFooter(2,"&b&3Fun Games and More!");
		mainTab.addFooter(2,"&b&3Fun Games and More!");
		mainTab.addFooter(2,"&b&3Fun Games and More!");
		mainTab.addFooter(2,"&b&3Fun Games and More!");
		mainTab.addFooter(2,"&b&3Fun Games and More!");
		mainTab.addFooter(2,"&b&3Fun Games and More!");
		mainTab.addFooter(2,"&b&3Fun Games and More!");
		mainTab.addFooter(2,"&b&3Fun Games and More!");
		mainTab.addFooter(2,"&b&3Fun Games and More!");
		mainTab.addFooter(2,"&b&3Fun Games and More!");
		mainTab.addFooter(2,"&b&3Fun Games and More!");
		
		
		
		mainTab.addHeader(3,"&e&l" + "AlienPlugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "AlienPlugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "AlienPlugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "AlienPlugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "AlienPlugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "AlienPlugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "AlienPlugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "AlienPlugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "AlienPlugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "AlienPlugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "A" + "&e&l" + "lienPlugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "Al" + "&e&l" + "ienPlugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "Ali" + "&e&l" + "enPlugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "Alie" + "&e&l" + "nPlugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "Alien" + "&e&l" + "Plugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "AlienP" + "&e&l" + "lugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "AlienPl" + "&e&l" + "ugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "AlienPlu" + "&e&l" + "gin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "AlienPlug" + "&e&l" + "in\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "AlienPlugi" + "&e&l" + "n\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&f&l" + "AlienPlugin" + "&e&l" + "\n&r&cPlaying Find The Item!");

		mainTab.addHeader(3,"&e&l" + "A" + "&f&l" + "lienPlugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "Al" + "&f&l" + "ienPlugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "Ali" + "&f&l" + "enPlugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "Alie" + "&f&l" + "nPlugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "Alien" + "&f&l" + "Plugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "AlienP" + "&f&l" + "lugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "AlienPl" + "&f&l" + "ugin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "AlienPlu" + "&f&l" + "gin\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "AlienPlug" + "&f&l" + "in\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "AlienPlugi" + "&f&l" + "n\n&r&cPlaying Find The Item!");
		mainTab.addHeader(3,"&e&l" + "AlienPlugin" + "&f&l" + "\n&r&cPlaying Find The Item!");
		
		mainTab.addFooter(3,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(3,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(3,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(3,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(3,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(3,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(3,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(3,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(3,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(3,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(3,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(3,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(3,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(3,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(3,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(3,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(3,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		mainTab.addFooter(3,"&b&fPlayers Online: " + Bukkit.getOnlinePlayers().size());
		
		mainTab.addFooter(3,"&b&dFun Games and More!");
		mainTab.addFooter(3,"&b&dFun Games and More!");
		mainTab.addFooter(3,"&b&dFun Games and More!");
		mainTab.addFooter(3,"&b&dFun Games and More!");
		mainTab.addFooter(3,"&b&dFun Games and More!");
		mainTab.addFooter(3,"&b&dFun Games and More!");
		mainTab.addFooter(3,"&b&dFun Games and More!");
		mainTab.addFooter(3,"&b&dFun Games and More!");
		mainTab.addFooter(3,"&b&dFun Games and More!");
		mainTab.addFooter(3,"&b&dFun Games and More!");
		mainTab.addFooter(3,"&b&dFun Games and More!");
		mainTab.addFooter(3,"&b&dFun Games and More!");
		mainTab.addFooter(3,"&b&3Fun Games and More!");
		mainTab.addFooter(3,"&b&3Fun Games and More!");
		mainTab.addFooter(3,"&b&3Fun Games and More!");
		mainTab.addFooter(3,"&b&3Fun Games and More!");
		mainTab.addFooter(3,"&b&3Fun Games and More!");
		mainTab.addFooter(3,"&b&3Fun Games and More!");
		mainTab.addFooter(3,"&b&3Fun Games and More!");
		mainTab.addFooter(3,"&b&3Fun Games and More!");
		mainTab.addFooter(3,"&b&3Fun Games and More!");
		mainTab.addFooter(3,"&b&3Fun Games and More!");
		mainTab.addFooter(3,"&b&3Fun Games and More!");
		mainTab.addFooter(3,"&b&3Fun Games and More!");
		
	}
	
	public static void updateAllPlayers() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			updatePlayerName(p);
			if (Main.currentGame == null || Main.currentGame.hasStarted == false || !containsUUID(Main.currentGame.players,p.getUniqueId().toString())) {
				giveGameCompass(p);
			}
		}
	}
	
	public static boolean containsUUID(ArrayList<String> list, String id) {
		for (String ID : list) {
			if (id.equals(ID)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean removeAllUUID(ArrayList<String> list, String id) {
		if (containsUUID(list, id) == false) {
			return false;
		}
		for (int i = 0; i < list.size(); i++) {
			if (id.equals(list.get(i))) {
				list.remove(i);
				i--;
			}
		}
		return true;
	}
	
	public static boolean allUUIDOnline(ArrayList<String> list) {
		for (String id : list) {
			Player p = Bukkit.getPlayer(getUUID(id));
			if (p == null)
				return false;
		}
		return true;
	}
	
	public static UUID getUUID(String id) {
		return UUID.fromString(id);
	}
	
	public static void giveGameCompass(Player player) {
		if (Main.currentGame == null || (Main.currentGame.hasStarted == false)) {
			ItemStack compass = new ItemStack(Material.COMPASS);
			ItemMeta meta = compass.getItemMeta();
			meta.setDisplayName(ChatColor.AQUA + "Open Game Menu!");
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GRAY + "Click to open!");
			meta.setLore(lore);
			compass.setItemMeta(meta);
			player.getInventory().setItem(8, compass);
		}
	}
	
	public static void updatePlayerName(Player p) {
		if (Main.currentGame == null || Main.currentGame.hasStarted == false || !containsUUID(Main.currentGame.activePlayers,p.getUniqueId().toString())) {
			p.setDisplayName(RankCommand.RANKS[Integer.parseInt("" + GameFileHandler.load(p.getUniqueId().toString() + "_rank", 0L))] + " " + p.getName() + ChatColor.RESET);
			p.setPlayerListName(RankCommand.RANKS[Integer.parseInt("" + GameFileHandler.load(p.getUniqueId().toString() + "_rank", 0L))] + " " + p.getName() + ChatColor.RESET);
			p.setCustomName(RankCommand.RANKS[Integer.parseInt("" + GameFileHandler.load(p.getUniqueId().toString() + "_rank", 0L))] + " " + p.getName() + ChatColor.RESET);
			
			GameFileHandler.save(p.getUniqueId().toString() + "_rank", GameFileHandler.load(p.getUniqueId().toString() + "_rank", 0));
		} else if (Main.currentGame.name.equals("deathswap")) {
			DeathSwap game = (DeathSwap) Main.currentGame;
			if (containsUUID(game.activePlayers,p.getUniqueId().toString())) {
				p.setDisplayName(ChatColor.GOLD + "" + "[DEATHSWAP] " + p.getName() + ChatColor.RESET);
				p.setPlayerListName(ChatColor.GOLD + "" + "[DEATHSWAP] " + p.getName() + ChatColor.RESET);
				p.setCustomName(ChatColor.GOLD + "" + "[DEATHSWAP] " + p.getName() + ChatColor.RESET);
			} else if (containsUUID(game.players, p.getUniqueId().toString())) {
				p.setDisplayName(ChatColor.GOLD + "" + "[SPECTATOR] " + p.getName() + ChatColor.RESET);
				p.setPlayerListName(ChatColor.GOLD + "" + "[SPECTATOR] " + p.getName() + ChatColor.RESET);
				p.setCustomName(ChatColor.GOLD + "" + "[SPECTATOR] " + p.getName() + ChatColor.RESET);
			}
			
		} else if (Main.currentGame.name.equals("manhunt")) {
			Manhunt game = (Manhunt) Main.currentGame;
			if (containsUUID(game.hunters,p.getUniqueId().toString())) {
				p.setDisplayName(ChatColor.RED + "" + "[HUNTER] " + p.getName() + ChatColor.RESET);
				p.setPlayerListName(ChatColor.RED + "" + "[HUNTER] " + p.getName() + ChatColor.RESET);
				p.setCustomName(ChatColor.RED + "" + "[HUNTER] " + p.getName() + ChatColor.RESET);
			} else if (containsUUID(game.speedrunners,p.getUniqueId().toString())) {
				p.setDisplayName(ChatColor.AQUA + "" + "[SPEEDRUNNER] " + p.getName() + ChatColor.RESET);
				p.setPlayerListName(ChatColor.AQUA + "" + "[SPEEDRUNNER] " + p.getName() + ChatColor.RESET);
				p.setCustomName(ChatColor.AQUA + "" + "[SPEEDRUNNER] " + p.getName() + ChatColor.RESET);
			} else if (containsUUID(game.players,p.getUniqueId().toString())) {
				p.setDisplayName(ChatColor.GOLD + "" + "[SPECTATOR] " + p.getName() + ChatColor.RESET);
				p.setPlayerListName(ChatColor.GOLD + "" + "[SPECTATOR] " + p.getName() + ChatColor.RESET);
				p.setCustomName(ChatColor.GOLD + "" + "[SPECTATOR] " + p.getName() + ChatColor.RESET);
			}
		} else if (Main.currentGame.name.equals("findtheitem")) {
			FindTheItem game = (FindTheItem) Main.currentGame;
			if (containsUUID(game.activePlayers,p.getUniqueId().toString())) {
				p.setDisplayName(ChatColor.GOLD + "" + "[FINDTHEITEM] " + p.getName() + ChatColor.RESET);
				p.setPlayerListName(ChatColor.GOLD + "" + "[FINDTHEITEM] " + p.getName() + ChatColor.RESET);
				p.setCustomName(ChatColor.GOLD + "" + "[FINDTHEITEM] " + p.getName() + ChatColor.RESET);
			} else if (containsUUID(game.players,p.getUniqueId().toString())) {
				p.setDisplayName(ChatColor.GOLD + "" + "[SPECTATOR] " + p.getName() + ChatColor.RESET);
				p.setPlayerListName(ChatColor.GOLD + "" + "[SPECTATOR] " + p.getName() + ChatColor.RESET);
				p.setCustomName(ChatColor.GOLD + "" + "[SPECTATOR] " + p.getName() + ChatColor.RESET);
			}
		} else {
			
		}
	}
}
