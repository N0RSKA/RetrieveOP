package dev.norska.rop.update;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.plugin.java.JavaPlugin;

public class ROPUpdateChecker {
	 
    private int project = 0;
    private URL checkURL;
    private String newVersion = "";
    private JavaPlugin plugin;
 
    public ROPUpdateChecker(JavaPlugin plugin, int projectID) {
        this.plugin = plugin;
        this.newVersion = plugin.getDescription().getVersion();
        this.project = projectID;
        try {
            this.checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectID);
        } catch (MalformedURLException e) {
        }
    }
 
    public int getProjectID() {
        return project;
    }
 
    public JavaPlugin getPlugin() {
        return plugin;
    }
 
    public String getLatestVersion() {
        return newVersion;
    }
 
    public String getResourceURL() {
        return "https://www.spigotmc.org/resources/" + project;
    }
 
    public boolean checkForUpdates() throws Exception {
        URLConnection con = checkURL.openConnection();
        this.newVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
        return !plugin.getDescription().getVersion().equals(newVersion);
    }

}