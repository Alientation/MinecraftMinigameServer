package me.Alientation.AlienPlugin.Util.NewCommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class RankTab implements TabCompleter{
	public String[] args1 = {
			"give", "list"
	};
	
	public String[] rankList = {
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
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> result = new ArrayList<String>();
		if (args.length == 1) {
			for (String a : args1) {
				if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
					result.add(a);
				}
			}
			return result;
		} else if (args[0].equals("give") && args.length == 3) {
			for (String a : rankList) {
				if (a.toLowerCase().startsWith(args[2].toLowerCase())) {
					result.add(a);
				}
			}
			return result;
		}
		return null;
	}

}
