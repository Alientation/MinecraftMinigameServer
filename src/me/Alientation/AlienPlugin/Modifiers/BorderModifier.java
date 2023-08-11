package me.Alientation.AlienPlugin.Modifiers;

import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;

import me.Alientation.AlienPlugin.Game;
import net.md_5.bungee.api.ChatColor;

public class BorderModifier extends Modifier{
	WorldBorder border;
	public BorderModifier(Game game, String name) {
		super(game, name);
		border = game.initialWorld.getWorldBorder();
		GUI = Bukkit.createInventory(null, 27,ChatColor.GOLD + "" + ChatColor.BOLD + "Border Modifier Settings");
	}

	public boolean begin() {
		
		return false;
	}
	
	public void setCenter(float x, float z) {
		border.setCenter(x,z);
	}
	
	public void setSize(float size) {
		border.setSize(size);
	}
}
