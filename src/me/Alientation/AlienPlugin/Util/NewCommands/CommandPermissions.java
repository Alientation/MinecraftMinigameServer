package me.Alientation.AlienPlugin.Util.NewCommands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Alientation.AlienPlugin.Main;
import me.Alientation.AlienPlugin.Util.GameFileHandler;
import me.Alientation.AlienPlugin.Util.Util;
import net.md_5.bungee.api.ChatColor;

public class CommandPermissions {
	public static boolean hasPermission(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		int rank = -1;
		if (sender instanceof Player) {
			player = (Player) sender;
			rank = Integer.parseInt(GameFileHandler.load(player.getUniqueId().toString() + "_rank", 0) + "");
		}
		switch(cmd.getName().toLowerCase()) {
		case "coin":
			if (args.length < 1) {
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient arguments for " + ChatColor.BOLD + "/coin {leaderboard/give}");
				return false;
			}
			switch(args[0].toLowerCase()) {
			case "leaderboard":
				return true;
			case "give":
				if (rank >= 6) {
					return true;
				}
				return false;
			}
			
			return false;
		case "chat":
			if (args.length < 1) {
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient arguments for " + ChatColor.BOLD + "/chat {team/global}");
				return false;
			}
			switch(args[0].toLowerCase()) {
			case "global":
				return true;
			case "team":
				if (player != null) {
					if (rank >= 6) {
						return true;
					}
				} else {
					return true;
				}
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient Permissions for " + ChatColor.BOLD + "/chat team {create/add/list}");
				return false;
			}
			return false;
		case "game":
			if (args.length < 1) {
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient arguments for " + ChatColor.BOLD + "/game {start/join/menu/pause/unpause}");
				return false;
			}
			switch(args[0].toLowerCase()) {
			case "start":
				if (player != null && rank >= 6) {
					return true;
				}
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient Permissions for " + ChatColor.BOLD + "/game start {deathswap/manhunt/findtheitem/duels}");
				return false;
			case "join":
				if (player != null) {
					return true;
				}
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient Permissions for " + ChatColor.BOLD + "/game join");
				return false;
			case "menu":
				if (player != null) {
					return true;
				}
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient Permissions for " + ChatColor.BOLD + "/game menu");
				return false;
			case "pause":
				if (player != null && rank >= 6) {
					return true;
				}
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient Permissions for " + ChatColor.BOLD + "/game pause");
				return false;
			case "unpause":
				if (player != null && rank >= 6) {
					return true;
				}
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient Permissions for " + ChatColor.BOLD + "/game unpause");
				return false;
			default:
				return false;
			}
		case "find":
			if (player != null) {
				if (rank >= 1 && (Main.currentGame == null || !Util.containsUUID(Main.currentGame.activePlayers, player.getUniqueId().toString()))) {
					return true;
				}
			}
			sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient Permissions for " + ChatColor.BOLD + "/find <player>");
			return false;
		case "world":
			if (args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient arguments for " + ChatColor.BOLD + "/world {current/list/create/travel}");
				return false;
			}
			switch(args[0].toLowerCase()) {
			case "current":
				if (player != null) {
					return true;
				}
				return false;
			case "create":
				if (player != null && rank >= 7) {
					return true;
				}
				return false;
			case "travel":
				if (player != null && rank >= 4) {
					if (args.length == 2 && (Main.currentGame == null || !Util.containsUUID(Main.currentGame.activePlayers, player.getUniqueId().toString()))) {
						return true;
					} else if (args.length == 3 && rank >= 5){
						return true;
					}
				}
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient Permissions for " + ChatColor.BOLD + "/game travel <world> <player>");
				return false;
			case "list":
				return true;
			default:
				return false;
			}
		case "rank":
			if (args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient arguments for " + ChatColor.BOLD + "/rank {give/list}");
				return false;
			}
			switch(args[0].toLowerCase()) {
			case "give":
				if (player == null || rank >= 6) {
					return true;
				}
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient Permissions for " + ChatColor.BOLD + "/rank give <player> <rank>");
				return false;
			case "list":
				if (player != null) {
					return true;
				}
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient Permissions for " + ChatColor.BOLD + "/rank list");
				return false;
			default:
				return false;
			}
		case "mode":
			if (player != null && rank >= 1 && (Main.currentGame == null || !Util.containsUUID(Main.currentGame.activePlayers, player.getUniqueId().toString()))) {
				return true;
			}
			sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient Permissions for " + ChatColor.BOLD + "/mode {survival/creative/spectator/adventure}");
			return false;
		case "test":
			if (player != null && rank >= 6) {
				return true;
			}
			sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient Permissions for " + ChatColor.BOLD + "/test <type>");
			return false;
		default:
			return false;
		}
	}
	
}
