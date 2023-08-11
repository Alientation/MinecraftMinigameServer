package me.Alientation.AlienPlugin.Util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerListHeaderFooter;

public class TabManager {
	
	private List<ArrayList<ChatComponentText>> headers = new ArrayList<ArrayList<ChatComponentText>>();
	private ArrayList<ArrayList<ChatComponentText>> footers = new ArrayList<ArrayList<ChatComponentText>>();
	
	public int count1 = 0;
	public int count2 = 0;
	
	private Plugin plugin;
	private int runType = -1;
	
	public TabManager(Plugin plugin, int countTypes) {
		this.plugin = plugin;
		for (int c = 0; c < countTypes; c++) {
			headers.add(new ArrayList<ChatComponentText>());
			footers.add(new ArrayList<ChatComponentText>());
		}
	}
	
	public void showTab() {
		if (headers.isEmpty() && footers.isEmpty()) {
			return;
		}
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
			int count1 = 0;
			int count2 = 0;
			
			@Override
			public void run() {
				try {
					if (runType != -1) {
						Field a = packet.getClass().getDeclaredField("header");
						a.setAccessible(true);
						
						Field b = packet.getClass().getDeclaredField("footer");
						b.setAccessible(true);
						
						if (count1 >= headers.get(runType).size()) {
							count1 = 0;
						}
						if (count2 >= footers.get(runType).size()) {
							count2 = 0;
						}
						a.set(packet, headers.get(runType).get(count1));
						b.set(packet, footers.get(runType).get(count2));
						
						if (Bukkit.getOnlinePlayers().size() != 0) { 
							for (Player player : Bukkit.getOnlinePlayers()) {
								((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
							}
						}
						count1++;
						count2++;
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 3L, 3L);
		
	}
	
	public void addHeader(int type, String header) {
		headers.get(type).add(new ChatComponentText(format(header)));
	}
	
	public void addFooter(int type, String footer) {
		footers.get(type).add(new ChatComponentText(format(footer)));
	}
	
	private String format(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public void setRunType(int t) {
		runType = t;
		if (runType >= footers.size()) {
			runType = -1;
		}
		count1 = 0;
		count2 = 0;
	}
	
	public int clear() {
		int hold = runType;
		runType = -1;
		for (int i = 0; i < footers.size(); i++) {
			footers.get(i).clear();
		}
		for (int i = 0; i < footers.size(); i++) {
			headers.get(i).clear();
		}
		return hold;
	}
}
