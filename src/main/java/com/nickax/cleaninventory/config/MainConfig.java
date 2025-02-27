package com.nickax.cleaninventory.config;

import com.google.common.reflect.TypeToken;
import com.nickax.genten.config.ConfigSection;
import com.nickax.genten.config.FileConfig;
import com.nickax.genten.credential.DatabaseCredential;
import com.nickax.genten.credential.loader.DatabaseCredentialLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class MainConfig extends FileConfig {

    private final DatabaseCredentialLoader databaseCredentialLoader;

    public MainConfig(JavaPlugin plugin) {
        super(new File(plugin.getDataFolder(), "config.yml"), plugin.getResource("config.yml"));
        this.databaseCredentialLoader = new DatabaseCredentialLoader("cleaninventory");
    }

    public boolean isAutoUpdateEnabled() {
        return castValue("auto-update", Boolean.class);
    }

    public String getDefaultLanguage() {
        return castValue("language.default", String.class);
    }

    public List<String> getEnabledLanguages() {
        return castValue("language.enabled", new TypeToken<>() {});
    }

    public DatabaseCredential getDatabaseCredential() {
        ConfigSection section = castValue("storage", ConfigSection.class);
        return databaseCredentialLoader.load(section);
    }

    public boolean isDataAutoSaveEnabled() {
        return castValue("storage.auto-save.enabled", Boolean.class);
    }

    public int getDataAutoSaveInterval() {
        return castValue("storage.auto-save.interval", Integer.class);
    }
}
