package me.Alientation.AlienPlugin.Util;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Statistics {
	private String playerID;
	
	private HashMap<String,Integer> integerData = new HashMap<String,Integer>();
	/*
	 * hunterManhuntWins
	 * hunterManhuntKills
	 * hunterManhuntDeaths
	 * hunterManhuntTimePlayed
	 * 
	 * speedrunnerManhuntWins
	 * speedrunnerManhuntKills
	 * speedrunnerManhuntDeaths
	 * speedrunnerManhuntTimePlayed
	 * 
	 * deathSwapWins
	 * deathSwapKills
	 * deathSwapDeaths
	 * deathSwapTimePlayed
	 */
	
	private HashMap<String,Double> decimalData = new HashMap<String, Double>();
	/*
	 * hunterManhuntCoinsGained
	 * hunterManhuntXPGained
	 * hunterManhuntCoinBetsLost
	 * 
	 * speedrunnerManhuntCoinsGained
	 * speedrunnerManhuntXPGained
	 * speedrunnerCoinBetsLost
	 * 
	 * deathSwapCoinsGained
	 * deathSwapXPGained
	 * speedrunnerCoinBetsLost
	 * 
	 * 
	 */
	
	@SuppressWarnings("unchecked")
	public Statistics(Player player) {
		this.playerID = player.getUniqueId().toString();
		try {
			this.integerData = (HashMap<String,Integer>) GameFileHandler.load(playerID + "_integerData", new HashMap<String,Integer>());
		} catch(Exception e) {
			this.integerData = new HashMap<String,Integer>();
		}
		try {
			this.decimalData = (HashMap<String,Double>) GameFileHandler.load(playerID + "_decimalData", new HashMap<String,Double>());
		} catch(Exception e) {
			this.decimalData = new HashMap<String,Double>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public Statistics(String id) {
		this.playerID = id;
		try {
			this.integerData = (HashMap<String,Integer>) GameFileHandler.load(playerID + "_integerData", new HashMap<String,Integer>());
		} catch(Exception e) {
			this.integerData = new HashMap<String,Integer>();
		}
		try {
			this.decimalData = (HashMap<String,Double>) GameFileHandler.load(playerID + "_decimalData", new HashMap<String,Double>());
		} catch(Exception e) {
			this.decimalData = new HashMap<String,Double>();
		}
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(Util.getUUID(playerID));
	}
	
	public String getPlayerID() {
		return this.playerID;
	}
	
	public HashMap<String,Integer> getIntegerData() {
		return this.integerData;
	}
	
	public int getIntegerData(String label) {
		return this.integerData.getOrDefault(label,0);
	}
	
	public HashMap<String,Double> getDecimalData() {
		return this.decimalData;
	}
	
	public double getDecimalData(String label) {
		return this.decimalData.getOrDefault(label,0.0);
	}
	
	public void addData(String label, int data) {
		putData(label, data + Integer.parseInt("" + this.integerData.getOrDefault(label, 0)));
	}
	
	public void addData(String label, double data) {
		putData(label, data + this.decimalData.getOrDefault(label, 0.0));
	}
	
	public void putData(String label, int data) {
		this.integerData.put(label, data);
	}
	
	public void putData(String label, double data) {
		this.decimalData.put(label, data);
	}
	
	public void save() {
		GameFileHandler.save(playerID + "_integerData", integerData);
		GameFileHandler.save(playerID + "_decimalData", decimalData);
	}
}
