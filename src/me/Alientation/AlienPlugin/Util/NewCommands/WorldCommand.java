package me.Alientation.AlienPlugin.Util.NewCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class WorldCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandPermissions.hasPermission(sender, cmd, label, args)) {
			return false;
		}
		switch(args[0].toLowerCase()) {
		case "list":
			if (args.length == 1) {
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "World List - " + Bukkit.getWorlds().size() + "\n-----------------------------");
				for (int i = 0; i < Bukkit.getWorlds().size(); i++) {
					sender.sendMessage("  ♦ " + ChatColor.GRAY + Bukkit.getWorlds().get(i).getName());
				}
			} else {
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Invalid number of arguments for " + ChatColor.BOLD + "/world list");
			}
			return false;
		case "current":
			if (sender instanceof Player) {
				Player player = (Player) sender;
				player.sendMessage(ChatColor.GRAY + "Your Current World is... " + player.getWorld().getName());
				return true;
			} else {
				sender.sendMessage("The console cannot use this command!");
			}
			return false;
		case "create":
			if (sender instanceof Player) {
				Player player = (Player) sender;
				player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "This command does not currently work!");
				return true;
			} else {
				sender.sendMessage("This command does not work as of yet!");
			}
			return false;
		case "travel":
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length == 1) {
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Incorrect Arguments for " + ChatColor.BOLD + "/world travel <world>");
					return false;
				} else if (args.length == 2) {
					if (Bukkit.getWorld(args[1]) == null) {
						player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Invalid World for " + ChatColor.BOLD + "/world travel <world>");
						return false;
					} else {
						player.teleport(Bukkit.getWorld(args[1]).getSpawnLocation());
						player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Teleporting to " + args[1] + "!");
						return true;
					}
					
				} else {
					if (Bukkit.getWorld(args[1]) == null) {
						player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Invalid World for " + ChatColor.BOLD + "/travelToWorld <world>");
						return false;
					} else if (Bukkit.getPlayer(args[2]) == null) {
						player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Invalid Player for " + ChatColor.BOLD + "/travelToWorld <world> <player>");
						return false;
					} else {
						Bukkit.getPlayer(args[2]).teleport(Bukkit.getWorld(args[1]).getSpawnLocation());
						return true;
					}
				}
			}
			return false;
		default:
			break;
		}
		return false;
	}

}
