package me.Alientation.AlienPlugin.Modifiers;

import org.bukkit.Bukkit;

import me.Alientation.AlienPlugin.Game;
import net.md_5.bungee.api.ChatColor;

public class MobSpawnModifier extends Modifier{

	public MobSpawnModifier(Game game, String name) {
		super(game, name);
		GUI = Bukkit.createInventory(null, 27,ChatColor.GOLD + "" + ChatColor.BOLD + "Mob Spawn Modifier Settings");
	}
	
	public boolean begin() {
		return false;
	}
}
