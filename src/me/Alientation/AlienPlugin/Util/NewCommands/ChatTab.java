package me.Alientation.AlienPlugin.Util.NewCommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.Alientation.AlienPlugin.Util.PlayerChatManager;
import me.Alientation.AlienPlugin.Util.TeamChat;

public class ChatTab implements TabCompleter{
	
	public String[] args1 = {
			"team", "global"
	};
	
	public String[] argsTeam = {
			"create", "add", "list", "remove"
	};
	
	public List<String> argsAddPlayerTeam = new ArrayList<String>();
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		argsAddPlayerTeam.clear();
		for (TeamChat tc : PlayerChatManager.teamChatList) {
			argsAddPlayerTeam.add(tc.getName());
		}
		
		List<String> result = new ArrayList<String>();
		
		if (args.length > 1 && args[0].equals("global")) {
			return result;
		}
		
		if (args.length == 1) {
			for (String a : args1) {
				if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
					result.add(a);
				}
			}
		} else if (args.length == 2) {
			if (args[0].equals("team")) {
				for (String a : argsTeam) {
					if (a.toLowerCase().startsWith(args[1].toLowerCase())) {
						result.add(a);
					}
				}
			}
		} else if (args.length == 3) {
			switch(args[1].toLowerCase()) {
			case "create":
				return result;
			case "add":
				return null;
			case "remove":
				return null;
			}
		} else if (args.length == 4 && (args[1].equals("add"))) {
			for (String a : argsAddPlayerTeam) {
				if (a.toLowerCase().startsWith(args[3].toLowerCase())) {
					result.add(a);
				}
			}
		}
		return result;
	}

}
