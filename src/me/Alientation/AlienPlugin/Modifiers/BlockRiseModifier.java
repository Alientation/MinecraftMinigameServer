package me.Alientation.AlienPlugin.Modifiers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import me.Alientation.AlienPlugin.Game;
import me.Alientation.AlienPlugin.Main;
import me.Alientation.AlienPlugin.Util.Util;
import net.md_5.bungee.api.ChatColor;

public class BlockRiseModifier extends BlockSpawnModifier{
	
	public Set<Chunk> completedChunks = new HashSet<Chunk>();
	public ArrayList<Chunk> chunks = new ArrayList<Chunk>();
	public HashMap<Chunk,Integer> yLevel = new HashMap<Chunk,Integer>();
	
	public BlockRiseModifier(Game game, String name) {
		super(game, name);
		GUI = Bukkit.createInventory(null, 27,ChatColor.GOLD + "" + ChatColor.BOLD + "Block Rise Modifier Settings");
	}
	
	public boolean begin() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plug, new Runnable() {
			@Override
			public void run() {
				for (String id : game.activePlayers) {
					Player p = Bukkit.getPlayer(Util.getUUID(id));
					if (p == null) {
						continue;
					}
					if (p.getLocation() != null && !chunks.contains(p.getLocation().getChunk()) && completedChunks.contains(p.getLocation().getChunk()) == false) {
						chunks.add(p.getLocation().getChunk());
						yLevel.put(p.getLocation().getChunk(), 0);
					}
				}
				
				for (int index = 0; index < chunks.size(); index++) {
					Chunk c = chunks.get(index);
					int y = yLevel.get(c);
					int chunkX = c.getX() >> 4;
					int chunkZ = c.getZ() >> 4;
					for (int x = 0; x < 16; x++) {
						for (int z = 0; z < 16; z++) {
							c.getBlock(x + chunkX, y, z + chunkZ).setType(block);
						}
					}
					
					if (y == game.initialWorld.getMaxHeight()) {
						chunks.remove(index);
						index -= 1;
						yLevel.remove(c);
						completedChunks.add(c);
						continue;
					}
					yLevel.put(c, yLevel.get(c) + 1);
				}
				if (game.gameOver == false) {
					begin();
				}
			}
		}, delayInSeconds * 20L);
		return false;
	}
}
