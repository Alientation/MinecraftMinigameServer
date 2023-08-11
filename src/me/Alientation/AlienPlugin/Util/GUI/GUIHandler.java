package me.Alientation.AlienPlugin.Util.GUI;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class GUIHandler {
	public static HashMap<String,GUI> mapsGUI = new HashMap<String,GUI>();
	
	public static boolean openGUI(Player player, String id) {
		if (mapsGUI.containsKey(id)) {
			player.closeInventory();
			player.openInventory(mapsGUI.get(id).getGUI());
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean handleClick(Player player, String id, int slot) {
		if (mapsGUI.containsKey(id)) {
			return mapsGUI.get(id).clicked(slot, player);
		} else {
			return false;
		}
	}
	
	public static boolean updateGUI(String id) {
		if (mapsGUI.containsKey(id)) {
			return mapsGUI.get(id).update();
		} else {
			return false;
		}
	}
}
