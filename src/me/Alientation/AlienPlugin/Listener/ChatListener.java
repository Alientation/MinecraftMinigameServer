package me.Alientation.AlienPlugin.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.Alientation.AlienPlugin.Util.PlayerChatManager;

public class ChatListener implements Listener{
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (PlayerChatManager.inputRequired.get(event.getPlayer().getUniqueId().toString()) != null) {
			PlayerChatManager.inputRequired.get(event.getPlayer().getUniqueId().toString()).playerInput(event.getPlayer(), event.getMessage());
			PlayerChatManager.inputRequired.remove(event.getPlayer().getUniqueId().toString());
			event.setCancelled(true);
		} 
		
		/*else {
			if (PlayerChatManager.getTeamChat(event.getPlayer()) != null) {
				PlayerChatManager.getTeamChat(event.getPlayer()).sendTeamMessage(event.getMessage(), event.getPlayer());
				event.setCancelled(true);
			}
		}
		*/
	}
}
