package me.Alientation.AlienPlugin.Util.NewCommands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class FindCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandPermissions.hasPermission(sender, cmd, label, args)) {
			return false;
		}
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Incorrect Arguments for " + ChatColor.BOLD + "/find <player>");
			} else {
				if (Bukkit.getPlayer(args[0]) != null) {
					player.setGameMode(GameMode.CREATIVE);
					player.setFlying(true);
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Teleporting to " + args[0] + "!");
					Location loc = Bukkit.getPlayer(args[0]).getWorld().getHighestBlockAt(Bukkit.getPlayer(args[0]).getLocation()).getLocation();
					loc.setY(loc.getY() + 10);
					player.teleport(loc);
					
				} else {
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Invalid player for " + ChatColor.BOLD + "/find <player>");
				}
			}
			return true;
		} else {
			sender.sendMessage("The console cannot access this command!");
		}
		return false;
	}

}
