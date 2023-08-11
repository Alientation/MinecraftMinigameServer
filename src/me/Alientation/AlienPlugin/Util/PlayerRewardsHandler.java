package me.Alientation.AlienPlugin.Util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class PlayerRewardsHandler {
	public static HashMap<String,PlayerInterface> playerInterfaceMappings = new HashMap<String,PlayerInterface>();
	
	/*
	 * DATA VALUES
	 */
	
	
	public static float COINS_MULTIPLIER = 1;
	public static float XP_MULTIPLIER = 1;
	
	public static final float COINS_PER_MINUTE_DEATHSWAP = 16;
	public static final float COINS_PER_MINUTE_MANHUNT = 8;
	public static final float COINS_PER_MINUTE_FINDTHEITEM = 12;
	public static final float COINS_PER_MINUTE_DUELS = 16;
	public static final float COINS_PER_MINUTE_PARKOURSWAP = 8;
	
	public static final float XP_PER_MINUTE_DEATHSWAP = 32;
	public static final float XP_PER_MINUTE_MANHUNT = 16;
	public static final float XP_PER_MINUTE_FINDTHEITEM = 24;
	public static final float XP_PER_MINUTE_DUELS = 32;
	public static final float XP_PER_MINUTE_PARKOURSWAP = 16;
	
	public static final float COINS_PER_WIN_DEATHSWAP = 256;
	public static final float COINS_PER_WIN_MANHUNT = 128;
	public static final float COINS_PER_WIN_FINDTHEITEM = 196;
	public static final float COINS_PER_WIN_DUELS = 32;
	public static final float COINS_PER_WIN_PARKOURSWAP = 64;
	
	public static final float XP_PER_WIN_DEATHSWAP = 512;
	public static final float XP_PER_WIN_MANHUNT = 256;
	public static final float XP_PER_WIN_FINDTHEITEM = 392;
	public static final float XP_PER_WIN_DUELS = 64;
	public static final float XP_PER_WIN_PARKOURSWAP = 128;
	
	
	public static final float COINS_PER_KILL_DEATHSWAP = 16;
	public static final float COINS_PER_KILL_MANHUNT = 8;
	public static final float COINS_PER_KILL_FINDTHEITEM = 12;
	public static final float COINS_PER_KILL_DUELS = 16;
	public static final float COINS_PER_KILL_PARKOURSWAP = 8;
	
	public static final float XP_PER_KILL_DEATHSWAP = 32;
	public static final float XP_PER_KILL_MANHUNT = 16;
	public static final float XP_PER_KILL_FINDTHEITEM = 24;
	public static final float XP_PER_KILL_DUELS = 32;
	public static final float XP_PER_KILL_PARKOURSWAP = 16;
	
	
	
	public static void addPlayer(Player player) { 
		PlayerInterface playerInterface = new PlayerInterface(player);
		playerInterfaceMappings.put(player.getUniqueId().toString(),playerInterface);
	}
	
	
	public static void saveAll() {
		for (@SuppressWarnings({ "rawtypes" }) Map.Entry mapElement : playerInterfaceMappings.entrySet()) {
			((PlayerInterface) mapElement.getValue()).save();
		}
	}
}
