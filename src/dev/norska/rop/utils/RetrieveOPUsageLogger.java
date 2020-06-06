package dev.norska.rop.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import dev.norska.rop.RetrieveOP;

public class RetrieveOPUsageLogger {

	private RetrieveOP main;

	public RetrieveOPUsageLogger(RetrieveOP main) {
		this.main = main;
	}
	
	Date now = new Date();
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

	public void log(String message) {
        try {
        	File directory = new File(main.getDataFolder() + "\\usage");
        	directory.mkdir();
            final File log = new File(main.getDataFolder() + "\\usage", format.format(now) + ".txt");
            if (!log.exists()) {
                log.createNewFile();
            }
            final FileWriter fw = new FileWriter(log, true);
            final PrintWriter pw = new PrintWriter(fw);
            pw.println(message);
            pw.flush();
            pw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}

}
