package dev.norska.rop.config;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import dev.norska.rop.RetrieveOP;

public class RetrieveOPConfigHandler {

	private RetrieveOP main;

	public RetrieveOPConfigHandler(RetrieveOP main) {
		this.main = main;
	}

	public void createConfigurations() {
		if (!main.userlist.exists()) {
			main.saveResource("user-list.yml", false);
		}
		if (!main.config.exists()) {
			main.saveResource("config.yml", false);
		}
	}

	public void reloadConfigC() {
		main.configC = YamlConfiguration.loadConfiguration(main.config);
	}

	public void reloadUserlistC() {
		main.userlistC = YamlConfiguration.loadConfiguration(main.userlist);
	}

	public void saveConfigC() {
		try {
			main.configC.save(main.config);
		} catch (IOException e) {
			Bukkit.getConsoleSender().sendMessage(" ");
			Bukkit.getConsoleSender().sendMessage("§eRetrieveOP: §fYAML §7> §f[§cconfig.yml§f] failed to save!");
			Bukkit.getConsoleSender().sendMessage(" ");
			e.printStackTrace();
		}
	}

	public void saveUserlistC() {
		try {
			main.userlistC.save(main.userlist);
		} catch (IOException e) {
			Bukkit.getConsoleSender().sendMessage(" ");
			Bukkit.getConsoleSender().sendMessage("§eRetrieveOP: §fYAML §7> §f[§cuser-list.yml§f] failed to save!");
			Bukkit.getConsoleSender().sendMessage(" ");
			e.printStackTrace();
		}
	}

	public void loadYAML() {
		try {
			main.configC.load(main.config);
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getConsoleSender().sendMessage(" ");
			Bukkit.getConsoleSender().sendMessage("§eRetrieveOP: §fYAML §7> §f[§cconfig.yml§f] failed to load!");
			Bukkit.getConsoleSender().sendMessage(" ");
			e.printStackTrace();
		}
		try {
			main.userlistC.load(main.userlist);
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getConsoleSender().sendMessage(" ");
			Bukkit.getConsoleSender().sendMessage("§eRetrieveOP: §fYAML §7> §f[§cuser-list.yml§f] failed to load!");
			Bukkit.getConsoleSender().sendMessage(" ");
			e.printStackTrace();
		}
	}

	public YamlConfiguration getConfigC() {
		return main.configC;
	}

	public YamlConfiguration getUserlistC() {
		return main.userlistC;
	}

}
