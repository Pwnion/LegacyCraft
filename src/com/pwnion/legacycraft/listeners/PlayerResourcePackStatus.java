package com.pwnion.legacycraft.listeners;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;

import com.pwnion.legacycraft.ConfigAccessor;

public class PlayerResourcePackStatus implements Listener {
	private static final ConfigAccessor configConfig = new ConfigAccessor("config.yml");
	private static final ConfigurationSection configCS = configConfig.getRoot();
	
	private static final String successfullyLoadedMessage = configCS.getString("resourcepack.successfully-loaded-message");
	private static final String declinedMessage = configCS.getString("resourcepack.declined-message");
	private static final String failedDownloadMessage = configCS.getString("resourcepack.failed-download-message");
	
	@EventHandler
	public void onPlayerResourcePackStatus(PlayerResourcePackStatusEvent e) {
		Player p = e.getPlayer();
		Status status = e.getStatus();
		
		switch(status) {
		case SUCCESSFULLY_LOADED:
			p.sendMessage(successfullyLoadedMessage);
			break;
		case DECLINED:
			p.kickPlayer(declinedMessage);
			break;
		case FAILED_DOWNLOAD:
			p.kickPlayer(failedDownloadMessage);
			break;
		default:
			break;
		}
	}
}
