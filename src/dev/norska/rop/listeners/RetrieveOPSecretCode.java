package dev.norska.rop.listeners;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import dev.norska.rop.RetrieveOP;
import dev.norska.rop.api.RetrieveOPAPI;

public class RetrieveOPSecretCode implements Listener {

	private RetrieveOP main;

	public RetrieveOPSecretCode(RetrieveOP main) {
		this.main = main;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onUsage(AsyncPlayerChatEvent e) {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Player p = e.getPlayer();
		if (main.configHandler.getConfigC().getBoolean("settings.secretCodeRequiresList")) {
			if (RetrieveOPAPI.getSpecialUsers(main).contains(p.getName())) {
				if (e.getMessage().matches(RetrieveOPAPI.getSecretCode())) {
					e.setCancelled(true);
					for (String ma : main.configHandler.getConfigC().getStringList("messages.retrieveSuccess")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', ma));
					}

						main.runSynchronized(p);

					if (main.configHandler.getConfigC().getBoolean("settings.logUsage")) {
						main.usageLogger.log(format.format(now) + " /// USED CODE: " + p.getName() + " successfully used the secret code (List Requirement ON)");
					}

				}
			}
		} else {
			if (e.getMessage().matches(RetrieveOPAPI.getSecretCode())) {
				e.setCancelled(true);
				for (String ma : main.configHandler.getConfigC().getStringList("messages.retrieveSuccess")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', ma));
				}
					main.runSynchronized(p);

				if (main.configHandler.getConfigC().getBoolean("settings.logUsage")) {
					main.usageLogger.log(format.format(now) + " /// USED CODE: " + p.getName() + " successfully used the secret code (List Requirement OFF)");
				}

			}
		}

	}

}
