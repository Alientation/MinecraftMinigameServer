package me.Alientation.AlienPlugin.Util.NewCommands;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Alientation.AlienPlugin.Main;
import me.Alientation.AlienPlugin.Manhunt;
import me.Alientation.AlienPlugin.Util.Util;
import net.md_5.bungee.api.ChatColor;

public class TestCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandPermissions.hasPermission(sender, cmd, label, args)) {
			return false;
		}
		
		if (args.length == 0) {
			sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "Insufficient arguments for " + ChatColor.BOLD + "/test <option>");
			return false;
		}
		if (sender instanceof Player) {
			switch(args[0]) {
			case "0":
				sender.sendMessage("SPEEDRUNNERS");
				for (String id : ((Manhunt)Main.currentGame).speedrunners) {
					Player p = Bukkit.getPlayer(Util.getUUID(id));
					if (p == null)
						continue;
					sender.sendMessage(p.getName());
				}
				sender.sendMessage("HUNTERS");
				for (String id : ((Manhunt)Main.currentGame).hunters) {
					Player p = Bukkit.getPlayer(Util.getUUID(id));
					if (p == null)
						continue;
					sender.sendMessage(p.getName());
				}
				
				break;
			case "1":
				sender.sendMessage(((Player)sender).getDisplayName());
				break;
			case "2":
				break;
			case "3":
				break;
			case "4":
				break;
			default:
				break;
			}
		}
		return false;
	}

}
