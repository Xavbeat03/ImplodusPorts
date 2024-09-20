package me.NinjaMandalorian.ImplodusPorts;

import me.NinjaMandalorian.ImplodusPorts.settings.Settings;
import org.bukkit.Bukkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class Logger {
    private static final String LOG_DIR_PATH = "plugins/ImplodusPorts/logs";
    private static final Level LOG_LEVEL = Settings.loggerLevel;
	
	private Logger () {}

    public static void log(String message) {
        if (LOG_LEVEL.intValue() <= Level.INFO.intValue()) {
            Bukkit.getLogger().info(message);
            quietLog("[INFO] " + message);
        }
    }

    public static void warn(String message) {
        if (LOG_LEVEL.intValue() <= Level.WARNING.intValue()) {
            Bukkit.getLogger().warning(message);
            quietLog("[WARN] " + message);
        }
    }

    public static void debug(String message) {
        final String msg = "[DEBUG] %s".formatted(message);
        if (LOG_LEVEL.intValue() <= Level.FINE.intValue()) {
            Bukkit.getLogger().info(msg);
            quietLog("[DEBUG] " + message);
        }
    }

    private static synchronized void quietLog(String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        String logFilePath = LOG_DIR_PATH + File.separator + "IPorts_log-" + date + ".txt";

        File logDir = new File(LOG_DIR_PATH);
        boolean dirCreated = logDir.exists() || logDir.mkdirs();
        if (!dirCreated) {
            Bukkit.getLogger().severe("Failed to create log directory");
            return;
        }
        

        try (FileWriter fw = new FileWriter(logFilePath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(message);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to write to log file: " + e.getMessage());
        }
    }
}

