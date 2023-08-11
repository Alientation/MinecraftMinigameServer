package me.Alientation.AlienPlugin.Util.NewCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Alientation.AlienPlugin.Main;
import me.Alientation.AlienPlugin.Util.PlayerChatManager;
import me.Alientation.AlienPlugin.Util.TeamChat;
import me.Alientation.AlienPlugin.Util.Util;
import net.md_5.bungee.api.ChatColor;

public class ChatCommand implements CommandExecutor{
	/*
	 * 	team:
	 * 		create <name>:
	 * 		add <player> <teamchat>:
	 * 		list:
	 * 	global <message>:
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandPermissions.hasPermission(sender, cmd, label, args)) {
			return false;
		}
		
		switch(args[0]) {
		case "team":
			if (args.length < 2) {
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Invalid number of arguments for " + ChatColor.BOLD + "/chat team {create/add/list}");
				return false;
			}
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (Main.currentGame != null && Main.currentGame.hasStarted == true && PlayerChatManager.getTeamChat(player) != null) {
					if (Util.containsUUID(Main.currentGame.activePlayers, player.getUniqueId().toString())) {
						if (args.length >= 2) {
							String message = args[1];
							for (int i = 2; i < args.length; i++) {
								message = message + " " + args[i];
							}
							PlayerChatManager.getTeamChat(player).sendTeamMessage(message, player);
							return true;
						}
					}
				}
			}
			switch(args[1]) {
			case "remove":
				if (args.length == 3) {
					if (PlayerChatManager.removePlayerFromTeam(args[2])) {
						sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "You have removed " + args[2] + " from their team chat!");
						return true;
					} else {
						sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Invalid player for " + ChatColor.BOLD + "/chat team remove <player>");
						return false;
					}
				} else {
					sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Invalid number of arguments for " + ChatColor.BOLD + "/chat team remove <player> <teamchat>");
					return false;
				}
			case "create":
				if (args.length == 3) {
					if (PlayerChatManager.createTeamChat(args[2])) {
						sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "You created " + args[2] + " team chat!");
						return true;
					} else {
						sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Error, could not create " + args[2] + " team chat!");
						return false;
					}
				} else {
					sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Invalid number of arguments for " + ChatColor.BOLD + "/chat team create <teamchat>");
					return false;
				}
			case "add":
				if (args.length == 4) {
					if (PlayerChatManager.addPlayerToTeam(args[2], args[3])) {
						sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "You added " + args[2] + " to " + args[3] + " team chat!");
						return true;
					} else {
						if (Bukkit.getPlayer(args[2]) == null) {
							if (PlayerChatManager.getTeamChat(args[3]) == null) {
								sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Invalid player and teamchat for " + ChatColor.BOLD + "/chat team add <player> <teamchat>");
								return false;
							}
							sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Invalid player for " + ChatColor.BOLD + "/chat team add <player> <teamchat>");
							return false;
						} else {
							sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Invalid teamchat for " + ChatColor.BOLD + "/chat team add <player> <teamchat>");
							return false;
						}
					}
				}
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Invalid number of arguments for " + ChatColor.BOLD + "/chat team add <player> <teamchat>");
				return false;
			case "list":
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "TeamChat List - " + PlayerChatManager.teamChatList.size() + "\n-----------------------------");
				for (TeamChat tc : PlayerChatManager.teamChatList) {
					sender.sendMessage("  ♦ " + ChatColor.WHITE + ChatColor.BOLD + tc.getName());
					for (String id : tc.getMembers()) {
						Player p = Bukkit.getPlayer(Util.getUUID(id));
						if (p == null) {
							sender.sendMessage("    - " + ChatColor.GRAY + ChatColor.ITALIC + "OFFLINE PLAYER - " + id);
						} else {
							sender.sendMessage("    - " + ChatColor.GRAY + ChatColor.ITALIC + p.getName());
						}
					}
				}
				return true;
			default:
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Invalid argument for " + ChatColor.BOLD + "/chat team {create/add/list}");
				return false;
			}
		case "global":
			if (args.length < 2) {
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Invalid argument for " + ChatColor.BOLD + "/chat team {create/add/list}");
				return false;
			}
			String message = args[1];
			for (int i = 2; i < args.length; i++) {
				message = message + " " + args[i];
			}
			if (sender instanceof Player) {
				Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "[GLOBAL]" + ChatColor.RESET + " <" + ((Player) sender).getDisplayName() + "> " + message);
			} else {
				Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "[GLOBAL]" + ChatColor.RESET + " <" + ChatColor.BLUE + ChatColor.BOLD + "SERVER" + ChatColor.RESET + "> " + message);
			}
			return true;
			
		default:
			sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Invalid argument for " + ChatColor.BOLD + "/chat {team/global}");
			return false;
		}
	}

}
