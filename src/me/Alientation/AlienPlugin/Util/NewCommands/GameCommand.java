package me.Alientation.AlienPlugin.Util.NewCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Alientation.AlienPlugin.DeathSwap;
import me.Alientation.AlienPlugin.Main;
import me.Alientation.AlienPlugin.Manhunt;
import net.md_5.bungee.api.ChatColor;

public class GameCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandPermissions.hasPermission(sender, cmd, label, args)) {
			return false;
		}
		switch(args[0].toLowerCase()) {
		case "start":
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length < 2) {
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Incorrect Arguments for " + ChatColor.BOLD + "/game start {deathswap/manhunt/findtheitem/duels}");
					return false;
				}
				if (Main.currentGame != null) {
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Another Game is currently running");
					return false;
				}
				switch(args[1].toLowerCase()) {
				case "deathswap":
					Main.mainTab.setRunType(1);
					Main.currentGame = new DeathSwap(player, Main.plug);
					break;
				case "manhunt":
					Main.mainTab.setRunType(2);
					Main.currentGame = new Manhunt(player, Main.plug);
					break;
				case "duels":
					break;
				case "findtheitem":
					break;
				case "parkourswap":
					break;
				default:
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Invalid game for " + ChatColor.BOLD + "/start {deathswap/manhunt/findtheitem/duels}");
					return false;
				}
				return true;
			} else {
				sender.sendMessage("The console cannot run the command!");
			}
			return false;
		case "join":
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (Main.currentGame == null) {
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "There is no Game running currently!");
					return false;
				}
				player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "You joined the Game!");
				for (Player p : player.getWorld().getPlayers()) {
					if (p != player) {
						p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + player.getName() + " joined the Game!");
					}
				}
				Main.currentGame.addPlayer(player);
				return true;
			} else {
				sender.sendMessage("The console cannot run this command");
			}
			return false;
		case "menu":
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!player.hasPermission("openmenu.use")) {
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient Permissions for " + ChatColor.BOLD + "/openmenu");
					return false;
				}
				if (Main.currentGame == null) {
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "There is no Game running currently!");
					return false;
				}
				Main.currentGame.updateMenu();
				Main.currentGame.showMenu(player);
				return true;
			} else {
				sender.sendMessage("The console cannot run this command!");
			}
			return false;
		case "pause":
			break;
		case "unpause":
			break;
		}
		return false;
	}

}
