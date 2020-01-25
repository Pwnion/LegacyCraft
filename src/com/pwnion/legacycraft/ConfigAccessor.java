package com.pwnion.legacycraft;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigAccessor {
	private String customFileName;
	private final Plugin plugin = LegacyCraft.getPlugin();
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	
	public ConfigAccessor(String fileName) {
		this.customFileName = fileName;
	}
	
	public void reloadCustomConfig() {
	    if(customConfigFile == null) {
	    	try {
	    		customConfigFile = new File(plugin.getDataFolder(), customFileName);
	    	} catch(Exception ex) {
	    		plugin.getLogger().log(Level.SEVERE, plugin.getDataFolder().toString() + " | " + customFileName, ex);
	    	}
	    	
	    }
	    customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

	    Reader defConfigStream;
		try {
			defConfigStream = new InputStreamReader(plugin.getResource(customFileName), "UTF8");
		} catch(UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		}
		
	    if(defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        customConfig.setDefaults(defConfig);
	    }
	}
	
	public FileConfiguration getCustomConfig() {
	    if(customConfig == null) {
	        reloadCustomConfig();
	    }
	    
	    return customConfig;
	}
	
	public void saveCustomConfig() {
	    if(customConfig == null || customConfigFile == null) {
	        return;
	    }
	    
	    try {
	        getCustomConfig().save(customConfigFile);
	    } catch (IOException ex) {
	        plugin.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
	    }
	}
	
	public void saveDefaultConfig() {
	    if(customConfigFile == null) {
	        customConfigFile = new File(plugin.getDataFolder(), customFileName);
	    }
	    if(!customConfigFile.exists()) {            
	         plugin.saveResource(customFileName, false);
	     }
	}
}