package com.nickax.cleaninventory.config;

import com.nickax.cleaninventory.credential.DatabaseCredentialLoader;
import com.nickax.genten.config.Config;
import com.nickax.genten.credential.DatabaseCredential;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class MainConfig extends Config {

    public MainConfig(JavaPlugin plugin) {
        super(plugin, "config.yml", "config.yml");
    }

    public boolean isAutoUpdateEnabled() {
        return getValue("auto-update").asType(Boolean.class);
    }

    public DatabaseCredential getDatabaseCredential() {
        ConfigurationSection section = getValue("storage").asType(ConfigurationSection.class);
        return DatabaseCredentialLoader.load(section);
    }
    
    public String getStorageType() {
        return getValue("storage.type").asType(String.class);
    }

    public boolean isDataAutoSaveEnabled() {
        return getValue("storage.auto-save.enabled").asType(Boolean.class);
    }

    public int getDataAutoSaveInterval() {
        return getValue("storage.auto-save.interval").asType(Integer.class);
    }
}
