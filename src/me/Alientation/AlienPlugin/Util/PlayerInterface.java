package me.Alientation.AlienPlugin.Util;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerInterface {
	private String playerID;
	private float xpBalance;
	private float coinBalance;
	private float xpMultiplier;
	private float coinMultiplier;
	private Statistics stats;
	
	
	public PlayerInterface(Player player) {
		this.playerID = player.getUniqueId().toString();
		this.coinBalance = Float.parseFloat("" + GameFileHandler.load(playerID + "_coinBalance", 0.0));
		this.xpBalance = Float.parseFloat("" + GameFileHandler.load(playerID + "_xpBalance", 0.0));
		this.xpMultiplier = Float.parseFloat("" + GameFileHandler.load(playerID + "_xpMultiplier", 0.0));
		this.coinMultiplier = Float.parseFloat("" + GameFileHandler.load(playerID + "_coinMultiplier", 0.0));
		this.stats = new Statistics(player);
	}
	
	public PlayerInterface(String id) {
		this.playerID = id;
		this.coinBalance = Float.parseFloat("" + GameFileHandler.load(playerID + "_coinBalance", 0.0));
		this.xpBalance = Float.parseFloat("" + GameFileHandler.load(playerID + "_xpBalance", 0.0));
		this.xpMultiplier = Float.parseFloat("" + GameFileHandler.load(playerID + "_xpMultiplier", 0.0));
		this.coinMultiplier = Float.parseFloat("" + GameFileHandler.load(playerID + "_coinMultiplier", 0.0));
		this.stats = new Statistics(id);
	}
	
	public String getPlayerID() {
		return this.playerID;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(Util.getUUID(playerID));
	}
	
	public float getCoinBalance() {
		return this.coinBalance;
	}
	
	public void addCoins(float coins, String cause, boolean doMultiplier) {
		if (doMultiplier) {
			this.coinBalance += coins + coins * coinMultiplier;
			if (Bukkit.getPlayer(Util.getUUID(playerID)) != null) {
				Bukkit.getPlayer(Util.getUUID(playerID)).sendMessage(cause + (coins) + " coins! (" + (PlayerRewardsHandler.COINS_MULTIPLIER * coinMultiplier) + ")");
			}
		} else {
			this.coinBalance += coins;
			if (Bukkit.getPlayer(Util.getUUID(playerID)) != null) {
				Bukkit.getPlayer(Util.getUUID(playerID)).sendMessage(cause + (coins) + " coins!");
			}
		}
	}
	
	public void removeCoins(float coins, String cause) {
		this.coinBalance -= coins;
		if (Bukkit.getPlayer(Util.getUUID(playerID)) != null) {
			Bukkit.getPlayer(Util.getUUID(playerID)).sendMessage(cause);
		}
	}
	
	public Statistics getStats() {
		return this.stats;
	}
	
	public float getXPBalance() {
		return this.xpBalance;
	}
	
	public void addXP(float xp, String cause, boolean doMultiplier) {
		if (doMultiplier) {
			this.xpBalance += xp + xp * xpMultiplier;
			if (Bukkit.getPlayer(Util.getUUID(playerID)) != null) {
				Bukkit.getPlayer(Util.getUUID(playerID)).sendMessage(cause + (xp + xp * xpMultiplier) + " xp! (" + (PlayerRewardsHandler.XP_MULTIPLIER * xpMultiplier) + ")");
			}
		} else {
			this.xpBalance += xp;
			if (Bukkit.getPlayer(Util.getUUID(playerID)) != null) {
				Bukkit.getPlayer(Util.getUUID(playerID)).sendMessage(cause + (xp) + " coins!");
			}
		}
	}
	
	public void removeXP(float xp, String cause) {
		this.xpBalance -= xp;
		if (Bukkit.getPlayer(Util.getUUID(playerID)) != null) {
			Bukkit.getPlayer(Util.getUUID(playerID)).sendMessage(cause);
		}
	}
	
	public void save() {
		GameFileHandler.save(playerID + "_coinBalance", coinBalance);
		GameFileHandler.save(playerID + "_xpBalance", xpBalance);
		GameFileHandler.save(playerID + "_xpMultiplier", xpMultiplier);
		GameFileHandler.save(playerID + "_coinMultiplier", coinMultiplier);
		this.stats.save();
	}
}
