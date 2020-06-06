package dev.norska.rop.update;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import dev.norska.rop.RetrieveOP;

public class ROPJoinNotifier implements Listener{
	
	private RetrieveOP main;

	public ROPJoinNotifier(RetrieveOP main) {
		this.main = main;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(!RetrieveOP.checkUpdate) {
			return;
		}
		
		Player p = e.getPlayer();
		
		if(p.isOp() && RetrieveOP.update) {
			new BukkitRunnable() {
				@Override
				public void run() {
					p.sendMessage("");
					p.sendMessage(" §8(§e§lRetrieveOP§8) §7§oA new update is available!");
					p.sendMessage(" §7Running on §c" + main.version + " §7while latest is §a" + RetrieveOP.latestUpdate + "§7!");
					p.sendMessage(" §e" + RetrieveOP.downloadLink);
					p.sendMessage(" §b" + RetrieveOP.downloadLink1);
					p.sendMessage("");
				}

			}.runTaskLater(main, 60);
		}
	}

}
