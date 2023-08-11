package me.Alientation.AlienPlugin.Modifiers;

import org.bukkit.Bukkit;

import me.Alientation.AlienPlugin.Game;
import me.Alientation.AlienPlugin.Main;
import net.md_5.bungee.api.ChatColor;

public class BorderShrinkModifier extends BorderModifier{

	public float sizeShrink;
	public int delayInSeconds;
	
	
	public BorderShrinkModifier(Game game, String name) {
		super(game, name);
		sizeShrink = (float) 1.0;
		delayInSeconds = 60;
		GUI = Bukkit.createInventory(null, 27,ChatColor.GOLD + "" + ChatColor.BOLD + "Border Shrink Modifier Settings");
	}
	
	public boolean begin() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plug, new Runnable() {
			@Override
			public void run() {
				border.setSize(border.getSize() - sizeShrink);
				if (game.gameOver == false) {
					begin();
				}
			}
		}, delayInSeconds * 20L);
		return false;
	}

}
