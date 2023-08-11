package me.Alientation.AlienPlugin.Util.NewCommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class GameTab implements TabCompleter{
	public String[] args1 = {
			"start", "join", "menu", "pause", "unpause"
	};
	
	public String[] argsStart = {
			"deathswap", "manhunt", "findtheitem", "duels", "parkourswap"
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
		} else if (args.length == 2 && args[0].equals("start")) {
			for (String a : argsStart) {
				if (a.toLowerCase().startsWith(args[1].toLowerCase())) {
					result.add(a);
				}
			}
		}
		return result;
	}

}
