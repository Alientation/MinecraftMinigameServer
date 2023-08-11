package me.Alientation.AlienPlugin.Modifiers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.Alientation.AlienPlugin.Game;

public class Modifier {
	public static Inventory GUI;
	
	
	public Game game;
	public String name;
	public Modifier(Game game, String name) {
		this.game = game;
		this.name = name;
	}
	
	public boolean begin() {
		return false;
	}
	
	public static void displayGUI(Player player) {
		player.closeInventory();
		player.openInventory(GUI);
	}
}
