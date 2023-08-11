package me.Alientation.AlienPlugin.Util;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerChatManager {
	public static ArrayList<TeamChat> teamChatList;
	public static HashMap<String, ChatInputGetter> inputRequired;
	
	public static void init() {
		teamChatList = new ArrayList<TeamChat>();
		inputRequired = new HashMap<String, ChatInputGetter>();
	}
	
	
	public static TeamChat getTeamChat(Player player) {
		for (TeamChat tc : teamChatList) {
			if (tc.hasMember(player)) {
				return tc;
			}
		}
		return null;
	}
	
	public static TeamChat getTeamChat(String name) {
		for (TeamChat tc : teamChatList) {
			if (tc.getName().equals(name)) {
				return tc;
			}
		}
		return null;
	}
	
	public static boolean playerHasTeam(Player player) {
		for (TeamChat tc : teamChatList) {
			if (tc.hasMember(player)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean playerHasTeam(String player) {
		return playerHasTeam(Bukkit.getPlayer(player));
	}
	
	public static boolean hasTeamChat(String name) {
		for (TeamChat tc : teamChatList) {
			if (tc.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean createTeamChat(String name) {
		if (getTeamChat(name) != null) {
			return false;
		}
		TeamChat tc = new TeamChat(name);
		teamChatList.add(tc);
		return true;
	}
	
	public static boolean deleteTeamChat(String name) {
		for (int i = 0; i < teamChatList.size(); i++) {
			if (teamChatList.get(i).getName().equals(name)) {
				teamChatList.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public static boolean deleteTeamChat(TeamChat teamChat) {
		if (teamChatList.contains(teamChat)) {
			teamChatList.remove(teamChat);
			return true;
		}
		return false;
	}
	
	public static boolean addPlayerToTeam(String player, String team) {
		if (getTeamChat(team) != null && Bukkit.getPlayer(player) != null) {
			removePlayerFromTeam(player);
			getTeamChat(team).addMembers(Bukkit.getPlayer(player));
			return true;
		}
		return false;
	}
	
	public static boolean addPlayerToTeam(Player player, TeamChat team) {
		if (team != null && player != null) {
			removePlayerFromTeam(player);
			team.addMembers(player);
			return true;
		}
		return false;
	}
	
	public static boolean removePlayerFromTeam(String player) {
		if (playerHasTeam(player) == false) {
			return false;
		}
		if (Bukkit.getPlayer(player) != null) {
			getTeamChat(Bukkit.getPlayer(player)).removeMember(Bukkit.getPlayer(player));
			return true;
		}
		return false;
	}
	
	public static boolean removePlayerFromTeam(Player player) {
		if (playerHasTeam(player) == false) {
			return false;
		}
		if (player != null) {
			getTeamChat(player).removeMember(player);
			return true;
		}
		return false;
	}
}
