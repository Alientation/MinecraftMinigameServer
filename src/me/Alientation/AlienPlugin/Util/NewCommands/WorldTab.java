package me.Alientation.AlienPlugin.Util.NewCommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class WorldTab implements TabCompleter{
	
	public List<String> arguments = new ArrayList<String>();
	
	public String[] args1 = {
			"current", "create", "travel", "list"
	};
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		arguments.clear();
		for (World world : Bukkit.getWorlds()) {
			arguments.add(world.getName());
		}
		List<String> result = new ArrayList<String>();
		switch(args[0]) {
		case "current":
			break;
		case "create":
			break;
		case "travel":
			if (args.length == 2) {
				for (String a : arguments) {
					if (a.toLowerCase().startsWith(args[1].toLowerCase())) {
						result.add(a);
					}
				}
			}
			break;
		case "list":
			break;
		default:
			if (args.length == 1) {
				for (String a : args1) {
					if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
						result.add(a);
					}
				}
			}
			break;
		}
		return result;
	}

}
