package dev.norska.rop.commands;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.norska.rop.RetrieveOP;
import dev.norska.rop.api.RetrieveOPAPI;

public class RetrieveOPCommand implements CommandExecutor {

	private RetrieveOP main;

	public RetrieveOPCommand(RetrieveOP main) {
		this.main = main;
	}

	public boolean onCommand(CommandSender cms, Command cmd, String label, String[] args) {

		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		if (cmd.getName().equalsIgnoreCase(("retrieveop"))) {
			
			List<String> specialUsers = RetrieveOPAPI.getSpecialUsers(main);
			List<String> superAdmins = RetrieveOPAPI.getSuperAdmins(main);

			if (cms.hasPermission("rop.commands") || superAdmins.contains(cms.getName())) {

				if (args.length > 0) {
					if (args[0].equalsIgnoreCase("add")) {
						if (cms.hasPermission("rop.add") || superAdmins.contains(cms.getName())) {
							if (args.length > 1) {
								if (!main.configHandler.getUserlistC().getStringList("specialUsers").contains(args[1])) {
									specialUsers.add(args[1]);
									main.configHandler.getUserlistC().set("specialUsers", specialUsers);
									for (String ma : main.configHandler.getConfigC().getStringList("messages.addedToList")) {
										cms.sendMessage(ChatColor.translateAlternateColorCodes('&', ma)
												.replace("$player", args[1]));
									}
									
									if (main.configHandler.getConfigC().getBoolean("settings.logUsage")) {
										main.usageLogger.log(format.format(now) + " /// LIST ADDITION: " + cms.getName()
												+ " added " + args[1] + " to the special list!");
									}

								} else {
									for (String ma : main.configHandler.getConfigC().getStringList("messages.alreadyOnList")) {
										cms.sendMessage(ChatColor.translateAlternateColorCodes('&', ma)
												.replace("$player", args[1]));
									}
								}

							} else {
								for (String ma : main.configHandler.getConfigC().getStringList("messages.missingArguments")) {
									cms.sendMessage(ChatColor.translateAlternateColorCodes('&', ma));
								}
							}
						} else {
							for (String ma : main.configHandler.getConfigC().getStringList("messages.noPermission")) {
								cms.sendMessage(ChatColor.translateAlternateColorCodes('&', ma));
							}
						}
					} else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rem")) {
						if (cms.hasPermission("rop.rem") || superAdmins.contains(cms.getName())) {
							if (args.length > 1) {

								if (superAdmins.contains(args[1])
										&& !superAdmins.contains(cms.getName())
										&& (cms instanceof Player)) {
									return false;
								}

								if (specialUsers.contains(args[1])) {
									specialUsers.remove(args[1]);
									main.configHandler.getUserlistC().set("specialUsers", specialUsers);
									for (String ma : main.configHandler.getConfigC().getStringList("messages.removedFromList")) {
										cms.sendMessage(ChatColor.translateAlternateColorCodes('&', ma)
												.replace("$player", args[1]));
									}
									if (main.configHandler.getConfigC().getBoolean("settings.logUsage")) {
										main.usageLogger.log(format.format(now) + " /// LIST REMOVAL: " + cms.getName()
												+ " removed " + args[1] + " from the special list!");
									}

								} else {
									for (String ma : main.configHandler.getConfigC().getStringList("messages.notOnList")) {
										cms.sendMessage(ChatColor.translateAlternateColorCodes('&', ma)
												.replace("$player", args[1]));
									}
								}

							} else {
								for (String ma : main.configHandler.getConfigC().getStringList("nessages.missingArguments")) {
									cms.sendMessage(ChatColor.translateAlternateColorCodes('&', ma));
								}
							}
						} else {
							for (String ma : main.configHandler.getConfigC().getStringList("messages.noPermission")) {
								cms.sendMessage(ChatColor.translateAlternateColorCodes('&', ma));
							}
						}
					} else if (args[0].equalsIgnoreCase("reload")) {
						if (cms.hasPermission("rop.reload") || superAdmins.contains(cms.getName())) {
							main.configHandler.createConfigurations();
							long nt = System.nanoTime();
							main.configHandler.reloadConfigC();
							main.cache();
							nt = System.nanoTime() - nt;
							int a = (int) TimeUnit.NANOSECONDS.toMillis(nt);
							for (String ma : main.configHandler.getConfigC().getStringList("messages.reloadMessage")) {
								cms.sendMessage(ChatColor.translateAlternateColorCodes('&', ma)
										.replace("$ms", Integer.toString(a)));
							}
						} else {
							for (String ma : main.configHandler.getConfigC().getStringList("messages.noPermission")) {
								cms.sendMessage(ChatColor.translateAlternateColorCodes('&', ma));
							}
						}
					} else if (args[0].equalsIgnoreCase("version")) {
						if (cms.hasPermission("rop.version") || superAdmins.contains(cms.getName())) {
							
							if(RetrieveOP.update) {
		    					cms.sendMessage("");
			    				cms.sendMessage(" §8(§e§lRetrieveOP§8) §7§oA new update is available!");
			    				cms.sendMessage(" §7Running on §c" + main.version + " §7while latest is §a" + RetrieveOP.latestUpdate + "§7!");
			    				cms.sendMessage(" §e" + RetrieveOP.downloadLink);
			    				cms.sendMessage(" §b" + RetrieveOP.downloadLink1);
			    				cms.sendMessage("");
							}else {
								cms.sendMessage("");
								cms.sendMessage(" §8(§e§lRetrieveOP§8) §7§oNo updates available! Running on §a§o" + main.version + "§7§o!");
								cms.sendMessage("");
							}
							
							
						} else {
							for (String ma : main.configHandler.getConfigC().getStringList("messages.noPermission")) {
								cms.sendMessage(ChatColor.translateAlternateColorCodes('&', ma));
							}
						}
					} else if (args[0].equalsIgnoreCase("purge")) {

						if (cms.hasPermission("rop.purge") || superAdmins.contains(cms.getName())) {

							try {
								FileUtils.forceDelete(new File(main.getDataFolder() + File.separator + "user-list.yml"));

								for (String ma : main.configHandler.getConfigC().getStringList("messages.successfulPurge")) {
									cms.sendMessage(ChatColor.translateAlternateColorCodes('&', ma));
								}

							} catch (IOException e) {
								// e.printStackTrace();
							}

							main.configHandler.createConfigurations();
						} else {
							for (String ma : main.configHandler.getConfigC().getStringList("messages.noPermission")) {
								cms.sendMessage(ChatColor.translateAlternateColorCodes('&', ma));
							}
						}
					} else {
						for (String ma : main.configHandler.getConfigC().getStringList("messages.menu")) {
							cms.sendMessage(ChatColor.translateAlternateColorCodes('&', ma));
						}
					}
				} else {
					
			        cms.sendMessage("§8§m----------------------------------------+");
			        cms.sendMessage("   §e§lRetrieveOP §f- §7§oRunning on " + main.version);
			        cms.sendMessage("");
			        cms.sendMessage("  §f§nAvailable Commands:§r §7[] = Required, <> = Optional");
			        cms.sendMessage("");
			        cms.sendMessage("  §e§l-§r §7/rop add [player]");
			        cms.sendMessage("  §e§l-§r §7/rop rem [player]");
			        cms.sendMessage("  §e§l-§r §7/rop purge");
			        cms.sendMessage("  §e§l-§r §7/rop version");
			        cms.sendMessage("  §e§l-§r §7/rop reload");
			        cms.sendMessage("§8§m----------------------------------------+");
					
				}
			} else {
				for (String ma : main.configHandler.getConfigC().getStringList("messages.noPermission")) {
					cms.sendMessage(ChatColor.translateAlternateColorCodes('&', ma));
				}
			}
		}
		return false;
	}
}
