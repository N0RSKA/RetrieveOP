package dev.norska.rop;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import dev.norska.rop.commands.RetrieveOPCommand;
import dev.norska.rop.config.RetrieveOPConfigHandler;
import dev.norska.rop.listeners.RetrieveOPLogoutDeop;
import dev.norska.rop.listeners.RetrieveOPSecretCode;
import dev.norska.rop.metrics.Metrics;
import dev.norska.rop.update.ROPJoinNotifier;
import dev.norska.rop.update.ROPUpdateChecker;
import dev.norska.rop.utils.RetrieveOPUsageLogger;

public class RetrieveOP extends JavaPlugin {
	
	public static Boolean update, checkUpdate;
	public static String latestUpdate;
	public static int resourceID = 49750;
	public static String downloadLink = "https://www.spigotmc.org/resources/" + resourceID + "/";
	public static String downloadLink1 = "https://www.mc-market.org/resources/5649/";
	public String version = getDescription().getVersion();

	public RetrieveOPConfigHandler configHandler = new RetrieveOPConfigHandler(this);

	public void onEnable() {
		
    	Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage("§eRetrieveOP §7" + version + "§e, a premium resource by §fNorska §e- §aExecuting startup sequence...");
		Bukkit.getConsoleSender().sendMessage(" ");
		
		generateFiles();
		registerCommands();
		registerEvents();
		startMetrics();
		cache();
		completedMessage();
		checkUpdates();
		
	}
	
	public void onDisable() {
		configHandler.saveUserlistC();
	}
	
	@SuppressWarnings("unused")
	private void startMetrics() {
		Metrics metrics = new Metrics(this);
	}

	private int a;
	private long nt;

	public File userlist;
	public File config;

	public YamlConfiguration userlistC = new YamlConfiguration();
	public YamlConfiguration configC = new YamlConfiguration();

	public RetrieveOPUsageLogger usageLogger = new RetrieveOPUsageLogger(this);

	private void generateFiles() {
		userlist = new File(getDataFolder(), "user-list.yml");
		config = new File(getDataFolder(), "config.yml");
		configHandler.createConfigurations();
		configHandler.loadYAML();
	}

	private void registerEvents() {
		nt = System.nanoTime();
		Bukkit.getPluginManager().registerEvents(new RetrieveOPSecretCode(this), this);
		Bukkit.getPluginManager().registerEvents(new RetrieveOPLogoutDeop(this), this);
		Bukkit.getPluginManager().registerEvents(new ROPJoinNotifier(this), this);
		nt = System.nanoTime() - nt;
		a = (int) TimeUnit.NANOSECONDS.toMillis(nt);
		Bukkit.getConsoleSender().sendMessage("§e[§a+§e] Registered listeners §7(took " + a + "§7ms)");
	}

	private void registerCommands() {
		nt = System.nanoTime();
		this.getCommand("retrieveop").setExecutor(new RetrieveOPCommand(this));
		nt = System.nanoTime() - nt;
		a = (int) TimeUnit.NANOSECONDS.toMillis(nt);
		Bukkit.getConsoleSender().sendMessage("§e[§a+§e] Registered commands §7(took " + a + "§7ms)");
	}

	private void completedMessage() {
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage("§e[§a+§e] §aCompleted startup. Enjoy the plugin!");
		Bukkit.getConsoleSender().sendMessage(" ");
	}
	
    private void checkUpdates() {
    	ROPUpdateChecker updater = new ROPUpdateChecker(this, resourceID);
	    try {
	        if (updater.checkForUpdates()) {
	    		new BukkitRunnable() {
	    			@Override
	    			public void run() {
	    				if(configHandler.getConfigC().contains("updates.notifications")) {
	    					checkUpdate = configHandler.getConfigC().getBoolean("updates.notifications");
	    				} else {
	    					checkUpdate = true;
	    				}
	    				if(checkUpdate) {
	    					Bukkit.getConsoleSender().sendMessage("");
		    				Bukkit.getConsoleSender().sendMessage(" §8(§e§lRetrieveOP§8) §7§oA new update is available!");
		    				Bukkit.getConsoleSender().sendMessage(" §7Running on §c" + version + " §7while latest is §a" + RetrieveOP.latestUpdate + "§7!");
		    				Bukkit.getConsoleSender().sendMessage(" §e" + RetrieveOP.downloadLink);
		    				Bukkit.getConsoleSender().sendMessage(" §b" + RetrieveOP.downloadLink1);
		    				Bukkit.getConsoleSender().sendMessage("");
	    				}
	    			}
	    		}.runTaskLater(this, 20 * 5);
	        	update = true;
	        	latestUpdate = updater.getLatestVersion();
	        }else {
	        	update = false;
	        }
	    } catch (Exception e) {
	        update = false;
	    }
    }
    
    public static String code;
    public static Boolean deopOnLogout;
    
    public void cache() {
    	code = configHandler.getConfigC().getString("settings.secretCode");
    	deopOnLogout = configHandler.getConfigC().getBoolean("settings.deopOnLogout");
    }

	public void runSynchronized(Player p) {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (String command : getConfig().getStringList("settings.secretCodeExecutes")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command
							.replace("$player", p.getName()));
				}
			}
		}.runTaskLater(this, 10);
	}
}