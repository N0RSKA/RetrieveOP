package dev.norska.rop.listeners;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import dev.norska.rop.RetrieveOP;
import dev.norska.rop.api.RetrieveOPAPI;

public class RetrieveOPLogoutDeop implements Listener {

	private RetrieveOP main;

	public RetrieveOPLogoutDeop(RetrieveOP main) {
		this.main = main;
	}

	Date now = new Date();
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	@EventHandler
	public void onLogout(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (RetrieveOPAPI.isDeopOnLogoutEnabled()) {
			if (p.isOp()) {
				p.setOp(false);
				if (p.hasPermission("rop.logoutbypass") || RetrieveOPAPI.getSuperAdmins(main).contains(p.getName())) {
					p.setOp(true);
				}
				main.usageLogger.log(format.format(now) + " /// LOGOUT: " + p.getName() + " logged out and got their OP removed!");
				return;
			}
		}
	}

}
