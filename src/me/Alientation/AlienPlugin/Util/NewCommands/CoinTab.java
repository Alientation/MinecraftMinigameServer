package me.Alientation.AlienPlugin.Util.NewCommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CoinTab implements TabCompleter{
	
	public String args1[] = {
		"leaderboard","give" 
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
		} else if (args.length == 2 && args[0].equals("give")) {
			return null;
		} else if (args.length >= 2 && args[0].equals("leaderboard")) {
			return result;
		} else if (args.length == 3 && args[0].equals("give")) {
			return result;
		}
		return result;
	}

}
