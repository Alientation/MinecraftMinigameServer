package me.Alientation.AlienPlugin.Util.GUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUI {
	protected String idName;
	protected String name;
	protected Inventory GUI;
	protected ClickedAction[] actions;
	
	
	public GUI(String name, int size, String id) {
		this.idName = id;
		this.actions = new ClickedAction[size];
		this.name = name;
		this.GUI = Bukkit.createInventory(null, size, name);
	}
	
	public boolean addItem(String displayName, Material itemMaterial, String lore, ClickedAction action, int slot) {
		if (slot >= this.actions.length) {
			return false;
		}
		
		ItemStack item = new ItemStack(itemMaterial);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(convertStringToLore(lore));
		this.actions[slot] = action;
		this.GUI.setItem(slot, item);
		return true;
	}
	
	public boolean addItem(String displayName, Material itemMaterial, List<String> lore, ClickedAction action, int slot) {
		if (slot >= this.actions.length) {
			return false;
		}
		
		ItemStack item = new ItemStack(itemMaterial);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(lore);
		this.actions[slot] = action;
		this.GUI.setItem(slot, item);
		return true;
	}
	
	public static List<String> convertStringToLore(String lore) {
		List<String> newLore = new ArrayList<String>();
		for (String s : lore.split("\n")) {
			newLore.add(s);
		}
		return newLore;
	}
	
	public boolean update() {
		return true;
	}
	
	public boolean clicked(int slot, Player player) {
		if (this.actions[slot] != null) {
			return this.actions[slot].run(player);
		}
		return false;
	}
	
	public Inventory getGUI() {
		return this.GUI;
	}
	
	public String getID() {
		return this.idName;
	}
	
	public String getName() {
		return this.name;
	}
}
