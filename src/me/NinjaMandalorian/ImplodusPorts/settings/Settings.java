package me.NinjaMandalorian.ImplodusPorts.settings;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.NinjaMandalorian.ImplodusPorts.ImplodusPorts;

public class Settings {
    
    private static ImplodusPorts plugin;
    private static FileConfiguration config;
    
    public static void init() {
        plugin = ImplodusPorts.getInstance();
        
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        
        config.options().copyDefaults(false);
        
        reloadConfig();
        
    }
    
    /**
     * Reloads the config with the internal config.yml
     */
    public static void reloadConfig() {
        
        // Gets internal config file as a Reader
        Reader defConfigStream = new InputStreamReader(plugin.getResource("config.yml"));
        if (defConfigStream != null) {
            
            // Converts into a YamlConfig
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            
            // Gets all default values in a map for iteration
            Map<String, Object> defMap = defConfig.getValues(true);
            
            // Gets all current values in a map for iteration
            Map<String, Object> configMap = config.getValues(true);
            
            for (Entry<String,Object> configEntry : configMap.entrySet()) {
                // If it is a memory section then it is ignored.
                if (configEntry.getValue() instanceof MemorySection) continue;
                
                if (defMap.containsKey(configEntry.getKey())) {
                    defConfig.set(configEntry.getKey(), configEntry.getValue());
                }
            }
            
            try {
                config = defConfig;
                config.save(new File(plugin.getDataFolder(), "config.yml"));
            } catch(IOException e) {
                Bukkit.getLogger().info("IOException when saving config.");
            }
       
        }
    }
    
    /**
     * Compares two version strings
     * @param old - Old version
     * @param current - Current version
     * @return 1 = newer; 0 = same; -1 = older;
     */
    public static int verComp(String old, String current) {
        // Splits versions
        String[] oldArr = old.split("\\.");
        String[] curArr = current.split("\\.");
        if (oldArr.length != curArr.length) {
            // Returns 1 if not of same formatting
            return 1;
        }
        
        for (int i = 0; i < oldArr.length; i++) {
            // Compares each row
            int oldInt = Integer.parseInt(oldArr[i]);
            int curInt = Integer.parseInt(curArr[i]);
            
            if (oldInt < curInt) return 1;
            else if (oldInt > curInt) return -1; 
        }
        // Return same if no differences
        return 0;
    }
}
