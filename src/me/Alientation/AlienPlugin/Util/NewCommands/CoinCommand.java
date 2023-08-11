package me.Alientation.AlienPlugin.Util.NewCommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.entity.Player;

import me.Alientation.AlienPlugin.Util.GameFileHandler;
import me.Alientation.AlienPlugin.Util.PlayerRewardsHandler;
import net.md_5.bungee.api.ChatColor;

public class CoinCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandPermissions.hasPermission(sender, cmd, label, args)) {
			return false;
		}
		
		switch(args[0]) {
		case "leaderboard":
			sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Coin Leaderboard - (NOT SORTED AS OF YET)\n<-------------------------------->");
			for (Player p : Bukkit.getOnlinePlayers()) {
				sender.sendMessage("   ♦ " + PlayerRewardsHandler.playerInterfaceMappings.get(p.getUniqueId().toString()).getCoinBalance() + String.format("%" + (14 - (PlayerRewardsHandler.playerInterfaceMappings.get(p.getUniqueId().toString()).getCoinBalance() + "").length()) + "s", " - ") + p.getName());
			}
			for (OfflinePlayer p : Bukkit.getWhitelistedPlayers()) {
				if (Bukkit.getPlayer(p.getUniqueId()) == null)
					sender.sendMessage("   ♦ " + GameFileHandler.load(p.getUniqueId().toString() + "_coinBalance", 0.0) + String.format("%" + (14 - (GameFileHandler.load(p.getUniqueId().toString() + "_coinBalance", 0.0) + "").length()) + "s", " - ") + p.getName());
			}
			
			return true;
		case "give":	
			if (args.length != 3) {
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Invalid number of arguments for " + ChatColor.BOLD + "/coin give <player> <amount>");
				return false;
			} else if (Bukkit.getPlayer(args[1]) == null) {
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Invalid player for " + ChatColor.BOLD + "/coin give <player> <amount>");
				return false;
			} else if (!NumberUtils.isParsable(args[2])) {
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Invalid amount for " + ChatColor.BOLD + "/coin give <player> <amount>");
				return false;
			}
			sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "You gave " + args[1] + " " + args[2] + " coins!");
			PlayerRewardsHandler.playerInterfaceMappings.get(Bukkit.getPlayer(args[1]).getUniqueId().toString()).addCoins(Float.parseFloat(args[2]), ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + sender.getName() + " gave you ", false);
			return true;
		default:
			sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Invalid argument for " + ChatColor.BOLD + "/coin {leaderboard/give}");
			return false;
		}
		
		
	}

}
