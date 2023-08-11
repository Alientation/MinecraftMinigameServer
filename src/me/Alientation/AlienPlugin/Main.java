package me.Alientation.AlienPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import me.Alientation.AlienPlugin.Listener.ChatListener;
import me.Alientation.AlienPlugin.Listener.DeathSwapListener;
import me.Alientation.AlienPlugin.Listener.MainListener;
import me.Alientation.AlienPlugin.Listener.ManhuntListener;
import me.Alientation.AlienPlugin.Util.GameFileHandler;
import me.Alientation.AlienPlugin.Util.PlayerChatManager;
import me.Alientation.AlienPlugin.Util.PlayerInterface;
import me.Alientation.AlienPlugin.Util.PlayerRewardsHandler;
import me.Alientation.AlienPlugin.Util.TabManager;
import me.Alientation.AlienPlugin.Util.Util;
import me.Alientation.AlienPlugin.Util.NewCommands.ChatCommand;
import me.Alientation.AlienPlugin.Util.NewCommands.ChatTab;
import me.Alientation.AlienPlugin.Util.NewCommands.CoinCommand;
import me.Alientation.AlienPlugin.Util.NewCommands.CoinTab;
import me.Alientation.AlienPlugin.Util.NewCommands.FindCommand;
import me.Alientation.AlienPlugin.Util.NewCommands.FindTab;
import me.Alientation.AlienPlugin.Util.NewCommands.GameCommand;
import me.Alientation.AlienPlugin.Util.NewCommands.GameTab;
import me.Alientation.AlienPlugin.Util.NewCommands.ModeCommand;
import me.Alientation.AlienPlugin.Util.NewCommands.ModeTab;
import me.Alientation.AlienPlugin.Util.NewCommands.RankCommand;
import me.Alientation.AlienPlugin.Util.NewCommands.RankTab;
import me.Alientation.AlienPlugin.Util.NewCommands.TestCommand;
import me.Alientation.AlienPlugin.Util.NewCommands.TestTab;
import me.Alientation.AlienPlugin.Util.NewCommands.WorldCommand;
import me.Alientation.AlienPlugin.Util.NewCommands.WorldTab;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Main extends JavaPlugin{
	/*
	 * TODO LIST:
	 * 
	 * 
	 * - Add GUI Menus
	 * - Add kits and perks
	 * - Add XP and COIN Boosters
	 * - when player eliminates other player after switching, it is counted as a kill even if they did not directly kill them
	 * - Add a better win effect system
	 * - Add better error handling and checks to achieve redundancy
	 * - Add money and cool cosmetics
	 * 
	 * 
	 * 
	 * 
	 */
	public static Game currentGame = null;
	public static TabManager mainTab;
	public static JSONObject data;
	public static JavaPlugin plug;
	@Override
	public void onEnable() {
		
		File f = new File("C:\\Users\\mcpir\\Desktop\\Spigot1.16.5\\AlienWorldData\\data.json");
		if (!f.exists()) {
			data = new JSONObject();
			GameFileHandler.init(data);
		} else {
			try {
				JSONParser parser = new JSONParser();
				FileReader reader = new FileReader("C:\\Users\\mcpir\\Desktop\\Spigot1.16.5\\AlienWorldData\\data.json");
				data = (JSONObject) parser.parse(reader);
				GameFileHandler.init(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		PlayerChatManager.init();
		this.getCommand("find").setExecutor(new FindCommand());
		this.getCommand("find").setTabCompleter(new FindTab());
		this.getCommand("game").setExecutor(new GameCommand());
		this.getCommand("game").setTabCompleter(new GameTab());
		this.getCommand("mode").setExecutor(new ModeCommand());
		this.getCommand("mode").setTabCompleter(new ModeTab());
		this.getCommand("rank").setExecutor(new RankCommand());
		this.getCommand("rank").setTabCompleter(new RankTab());
		this.getCommand("test").setExecutor(new TestCommand());
		this.getCommand("test").setTabCompleter(new TestTab());
		this.getCommand("world").setExecutor(new WorldCommand());
		this.getCommand("world").setTabCompleter(new WorldTab());
		this.getCommand("chat").setExecutor(new ChatCommand());
		this.getCommand("chat").setTabCompleter(new ChatTab());
		this.getCommand("coin").setExecutor(new CoinCommand());
		this.getCommand("coin").setTabCompleter(new CoinTab());
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				Bukkit.getWorld("lobby").setTime(0);
				for (Entity e : Bukkit.getWorld("lobby").getEntities()) {
					if (e instanceof Player) {
						continue;
					}
					if (e instanceof Monster) {
						e.remove();
					}
				}
			}
		},20L,6000L);
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					Util.createBoard(p);
				}
			}
		},10L,200L);
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (PlayerRewardsHandler.playerInterfaceMappings.containsKey(p.getUniqueId().toString())) {
				continue;
			}
			PlayerRewardsHandler.playerInterfaceMappings.put(p.getUniqueId().toString(), new PlayerInterface(p));
		}
		
		Bukkit.createWorld(new WorldCreator("lobby"));
		Bukkit.createWorld(new WorldCreator("minigame"));
		
		plug = this;
		this.saveDefaultConfig();
		mainTab = new TabManager((Plugin) this, 6);
		Util.loadTabManager(mainTab);
		mainTab.setRunType(0);
		/*
		 * runType
		 * 0: lobby
		 * 1: deathswap
		 * 2: manhunt
		 * 3: findtheitem
		 * 4: duels
		 * 5: parkourswap
		 */
		mainTab.showTab();
		getServer().getPluginManager().registerEvents(new DeathSwapListener(), (Plugin)this);
		getServer().getPluginManager().registerEvents(new ManhuntListener(), (Plugin)this);
		getServer().getPluginManager().registerEvents(new MainListener(), (Plugin) this);
		getServer().getPluginManager().registerEvents(new ChatListener(), (Plugin) this);
		Util.updateAllPlayers();
	}
	
	@Override
	public void onDisable() {
		PlayerRewardsHandler.saveAll();
		GameFileHandler.writeToFile();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			switch(cmd.getName().toLowerCase()) {
			case "lobby":
				player.teleport(Bukkit.getWorld("lobby").getSpawnLocation());
				player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Teleporting to LOBBY!");
				break;
			case "tc":
				if (PlayerChatManager.getTeamChat(player) == null) {
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You are not in a team chat!");
					return false;
				}
				if (args.length >= 1) {
					String message = args[0];
					for (int i = 1; i < args.length; i++) {
						message = message + " " + args[i];
					}
					PlayerChatManager.getTeamChat(player).sendTeamMessage(message, player);
					return true;
				} else {
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "You need to send a message bruh!");
					return false;
				}
			default:
				player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(ALIENGAMES) " + ChatColor.RESET + "" + ChatColor.WHITE + "Invalid Command");
				break;
			}
		} else {
			switch(cmd.getName().toLowerCase()) {
			
			}
		}
		return false;
	}
	
	public static void broadcastGlobalMessage(TextComponent message) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.spigot().sendMessage(message);
		}
	}
	
}
