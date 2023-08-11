package me.Alientation.AlienPlugin.Util;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class TeamChat {
	private String name;
	private ArrayList<String> members;
	
	public TeamChat(String name) {
		this.name = name;
		members = new ArrayList<String>();
	}
	
	public String getName() {
		return name;
	}
	
	public boolean sendTeamMessage(String message, Player sender) {
		for (String id : members) {
			Player p = Bukkit.getPlayer(Util.getUUID(id));
			if (p == null) {
				continue;
			}
			p.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "[" + ChatColor.RESET + ChatColor.DARK_AQUA + ChatColor.BOLD + "TEAMCHAT" + ChatColor.WHITE + "-" + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD + name + "]" + ChatColor.RESET + " <" + sender.getDisplayName() + "> " + message);
		}
		return true;
	}
	
	
	public ArrayList<String> getMembers() {
		return members;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setMembers(ArrayList<String> members) {
		this.members = members;
	}
	
	public boolean addMembers(Player member) {
		if (hasMember(member)) {
			return false;
		}
		this.members.add(member.getUniqueId().toString());
		return true;
	}
	
	public boolean removeMember(Player member) {
		if (hasMember(member)) {
			Util.removeAllUUID(members, member.getUniqueId().toString());
			return true;
		}
		return false;
	}
	
	public boolean hasMember(Player player) {
		return Util.containsUUID(members, player.getUniqueId().toString());
	}
}
