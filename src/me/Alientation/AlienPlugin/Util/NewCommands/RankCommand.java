package me.Alientation.AlienPlugin.Util.NewCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Alientation.AlienPlugin.Util.GameFileHandler;
import me.Alientation.AlienPlugin.Util.Util;
import net.md_5.bungee.api.ChatColor;

public class RankCommand implements CommandExecutor{
	
	public final static String[] RANKS = {
			ChatColor.GRAY + "[PEASANT]",										//0
			ChatColor.WHITE + "[MEMBER]",										//1
			ChatColor.GREEN + "[DUMBOHEAD]",									//2
			ChatColor.MAGIC + "[QWERTY]" + ChatColor.RESET + ChatColor.YELLOW,	//3
			ChatColor.RED + "" + ChatColor.BOLD + "[YUTOOBER]",					//4
			ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[MOD]",				//5
			ChatColor.DARK_RED + "" + ChatColor.BOLD + "[ADMIN]",				//6
			ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "[MANAGER]",			//7
			ChatColor.DARK_RED + "" + ChatColor.BOLD + "[ADMIN]",
			//ChatColor.GOLD + "" + ChatColor.BOLD + "[OWNER]"					//8
	};
	
	public final static String[] arguments = {
			"peasant",
			"member",
			"dumbohead",
			"qwerty",
			"helper",
			"mod",
			"admin",
			"manager",
			"owner"
	};
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandPermissions.hasPermission(sender, cmd, label, args)) {
			return false;
		}
		switch(args[0]) {
		case "give":
			if (args.length != 3) {
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient arguments for " + ChatColor.BOLD + "/rank give <player> <rank>");
				return false;
			}
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (Bukkit.getPlayer(args[1]) != null) {
					for (int i = 0; i < arguments.length; i++) {
						if (args[2].equals(arguments[i])) {
							player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Changed " + args[1] + "'s rank to " + args[2]);
							GameFileHandler.save(Bukkit.getPlayer(args[1]).getUniqueId().toString() + "_rank", i);
							GameFileHandler.writeToFile();
							Util.updatePlayerName(Bukkit.getPlayer(args[1]));
							return true;
						} else if (i == arguments.length - 1){
							player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Invalid Rank for " + ChatColor.BOLD + "/setRank <player> <rank>");
							return false;
						}
					}
				} else {
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Invalid Player for " + ChatColor.BOLD + "/setRank <player> <rank>");
					return false;
				}
			} else {
				if (Bukkit.getPlayer(args[1]) != null) {
					for (int i = 0; i < arguments.length; i++) {
						if (args[2].equals(arguments[i])) {
							sender.sendMessage("Changed " + args[1] + "'s Rank to " + args[2]);
							GameFileHandler.save(Bukkit.getPlayer(args[1]).getUniqueId().toString() + "_rank", i);
							GameFileHandler.writeToFile();
							Util.updatePlayerName(Bukkit.getPlayer(args[1]));
							return true;
						} else if (i == arguments.length - 1){
							sender.sendMessage("Invalid Rank for /setRank <player> <rank>");
							return false;
						}
					}
				} else {
					sender.sendMessage("Invalid Player for /setRank <player> <rank>");
					return false;
				}
			}
			return false;
		case "list":
			sender.sendMessage(ChatColor.DARK_GREEN  + "" + ChatColor.BOLD + "           Rank List             ");
			sender.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "==========================");
			for (String s : RANKS) {
				sender.sendMessage("  ♦ " + ChatColor.GRAY + s.toUpperCase());
			}
			break;
		default:
			break;
		}
		return false;
	}

}
