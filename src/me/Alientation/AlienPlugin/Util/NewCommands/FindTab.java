package me.Alientation.AlienPlugin.Util.NewCommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class FindTab implements TabCompleter{
	public List<String> arguments = new ArrayList<String>();
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (arguments.isEmpty()) {
			
		}
		List<String> result = new ArrayList<String>();
		if (args.length > 1) {
			return result;
		}
		return null;
	}

}
