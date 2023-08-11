package me.Alientation.AlienPlugin.Modifiers;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import me.Alientation.AlienPlugin.Game;
import net.md_5.bungee.api.ChatColor;

public class BlockSpawnModifier extends Modifier{
	public Material block;
	public int delayInSeconds;
	public BlockSpawnModifier(Game game, String name) {
		super(game, name);
		block = Material.AIR;
		GUI = Bukkit.createInventory(null, 27,ChatColor.GOLD + "" + ChatColor.BOLD + "Block Spawn Modifier Settings");
	}

	public boolean begin() {
		return false;
	}
}
