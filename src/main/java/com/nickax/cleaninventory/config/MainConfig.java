package com.nickax.cleaninventory.config;

import com.google.common.reflect.TypeToken;
import com.nickax.cleaninventory.credential.DatabaseCredentialLoader;
import com.nickax.genten.config.Config;
import com.nickax.genten.credential.DatabaseCredential;
import com.nickax.genten.repository.database.DatabaseCredentialProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class MainConfig extends Config implements DatabaseCredentialProvider {

    public MainConfig(JavaPlugin plugin) {
        super(plugin, "config.yml", "config.yml");
    }

    public boolean isAutoUpdateEnabled() {
        return getValue("auto-update").asType(Boolean.class);
    }

    public String getDefaultLanguage() {
        return getValue("language.default").asType(String.class);
    }

    public List<String> getEnabledLanguages() {
        return getValue("language.enabled").asType(new TypeToken<List<String>>() {}.getType());
    }

    @Override
    public DatabaseCredential getDatabaseCredential() {
        ConfigurationSection section = getValue("storage").asType(ConfigurationSection.class);
        return DatabaseCredentialLoader.load(section);
    }

    public boolean isDataAutoSaveEnabled() {
        return getValue("storage.auto-save.enabled").asType(Boolean.class);
    }

    public int getDataAutoSaveInterval() {
        return getValue("storage.auto-save.interval").asType(Integer.class);
    }
}
