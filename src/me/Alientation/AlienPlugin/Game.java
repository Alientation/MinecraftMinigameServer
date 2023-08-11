package me.Alientation.AlienPlugin;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.entity.Player;

import me.Alientation.AlienPlugin.Modifiers.Modifier;
import me.Alientation.AlienPlugin.Util.Util;
import net.md_5.bungee.api.ChatColor;

public class Game{
	
	public boolean hasStarted;
	public World initialWorld;
	public String name;
	public boolean gameOver = false;
	public ArrayList<String> players;
	public ArrayList<String> activePlayers;
	public ArrayList<Modifier> modifiers;
	
	public Game() {
		hasStarted = false;
		modifiers = new ArrayList<Modifier>();
		players = new ArrayList<String>();
		activePlayers = new ArrayList<String>();
	}
	
	public boolean start() {
		for (Modifier mod : modifiers) {
			mod.begin();
		}
		return false;
	}
	
	public boolean end() {
		return false;
	}
	
	public void showMenu(Player player) {
		
	}
	
	public void updateMenu() {
		
	}
	
	public void updateGameMenu() {
		
	}
	
	public void pause() {
		
	}
	
	public void unpause() {
		
	}
	
	public boolean containsModifier(Modifier mod) {
		for (Modifier m : modifiers) {
			if (m.name.equals(mod.name))
				return true;
		}
		return false;
	}
	
	public boolean containsModifier(String name) {
		for (Modifier m : modifiers) {
			if (m.name.equals(name))
				return true;
		}
		return false;
	}
	
	public boolean addModifier(Modifier mod) {
		if (containsModifier(mod)) {
			return false;
		}
		modifiers.add(mod);
		return true;
	}
	
	public boolean addPlayer(Player player) {
		if (Util.containsUUID(players, player.getUniqueId().toString())) {
			player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "You already joined " + name + "!");
			return false;
		}
		player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "You have joined " + name + "!");
		players.add(player.getUniqueId().toString());
		return true;
	}
	
	public boolean removePlayer(Player player) {
		if (Util.containsUUID(players, player.getUniqueId().toString())) {
			player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "You left " + name + "!");
			Util.removeAllUUID(players, player.getUniqueId().toString());
			return true;
		} else {
			player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + ChatColor.WHITE + "You have not joined " + name + " yet!");
			return false;
		}
	}
	
	public ArrayList<Modifier> getModifiers() {
		return modifiers;
	}
	
	public ArrayList<String> getPlayers() {
		return players;
	}
}
