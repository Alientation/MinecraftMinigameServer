package me.Alientation.AlienPlugin.Util.NewCommands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class ModeCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandPermissions.hasPermission(sender, cmd, label, args)) {
			return false;
		}
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 1) {
				switch (args[0].toLowerCase()) {
				case "creative":
					player.setGameMode(GameMode.CREATIVE);
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Your gamemode has been changed to Creative mode!");
					break;
				case "survival":
					player.setGameMode(GameMode.SURVIVAL);
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Your gamemode has been changed to Survival mode!");
					break;
				case "spectator":
					player.setGameMode(GameMode.SPECTATOR);
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Your gamemode has been changed to Spectator mode!");
					break;
				case "adventure":
					player.setGameMode(GameMode.ADVENTURE);
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Your gamemode has been changed to Adventure mode!");
					break;
				default:
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Invalid gamemode for /<gamemode>");
					break;
				}
			} else {
				player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Invalid number of arguments for /setRank <player> <rank>");
			}
			
		} else {
			sender.sendMessage("The console cannot use this command!");
		}
		return false;
	}

}
