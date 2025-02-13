package com.nickax.cleaninventory;

import com.nickax.cleaninventory.command.CleanInventoryCommand;
import com.nickax.cleaninventory.config.MainConfig;
import com.nickax.cleaninventory.data.PlayerData;
import com.nickax.cleaninventory.data.PlayerDataSaveTask;
import com.nickax.cleaninventory.listener.InventoryListener;
import com.nickax.cleaninventory.listener.LanguageListener;
import com.nickax.cleaninventory.listener.ListenerRegistry;
import com.nickax.cleaninventory.listener.PlayerDataListener;
import com.nickax.cleaninventory.listener.pickup.StandardPickupListener;
import com.nickax.cleaninventory.repository.PlayerDataRepository;
import com.nickax.genten.command.CommandRegistry;
import com.nickax.genten.language.Language;
import com.nickax.genten.language.LanguageAccessor;
import com.nickax.genten.language.LanguageLoader;
import com.nickax.genten.library.Libraries;
import com.nickax.genten.library.LibraryLoader;
import com.nickax.genten.plugin.PluginUpdater;
import com.nickax.genten.repository.JsonRepository;
import com.nickax.genten.repository.LocalRepository;
import com.nickax.genten.repository.Repository;
import com.nickax.genten.repository.database.DatabaseLoader;
import com.nickax.genten.repository.dual.TargetRepository;
import com.nickax.genten.spigot.SpigotVersion;
import fr.minuskube.inv.InventoryManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.UUID;

public final class CleanInventory extends JavaPlugin {

    private InventoryManager inventoryManager;
    private final MainConfig mainConfig = new MainConfig(this);
    private PlayerDataRepository playerDataRepository;
    private PlayerDataSaveTask playerDataSaveTask;
    private LanguageAccessor languageAccessor;
    private final PluginUpdater pluginUpdater = new PluginUpdater(getLogger());

    @Override
    public void onLoad() {
        LibraryLoader libraryLoader = new LibraryLoader(this);
        libraryLoader.load(Libraries.MONGO_JAVA_DRIVER.get());
    }

    @Override
    public void onEnable() {
        SpigotVersion.checkCompatibility(getLogger());
        initMainConfig();
        loadPlayerData();
        loadLanguages();
        loadInventoryManager();
        loadListeners();
        loadCommands();
        loadMetrics();
        checkForUpdates();
    }

    @Override
    public void onDisable() {
        savePlayerData();
        ListenerRegistry.unregisterAll();
        CommandRegistry.unregisterAll();
    }

    public void reload() {
        initMainConfig();
        ListenerRegistry.unregisterAll();
        playerDataSaveTask.cancel();
        savePlayerData();
        loadPlayerData();
        loadLanguages();
        loadListeners();
        CommandRegistry.unregisterAll();
        loadCommands();
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public PlayerDataRepository getPlayerDataRepository() {
        return playerDataRepository;
    }

    public LanguageAccessor getLanguageAccessor() {
        return languageAccessor;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    private void initMainConfig() {
        mainConfig.load();
        mainConfig.save();
        mainConfig.restore();
    }

    private void loadPlayerData() {
        loadPlayerDataRepository();
        loadPlayerDataForOnlinePlayers();
        startPlayerDataSaveTask();
        ListenerRegistry.add(new PlayerDataListener(this));
    }

    private void loadPlayerDataRepository() {
        Repository<UUID, PlayerData> defaultDatabase = new JsonRepository<>(new File(getDataFolder(), "data/player"), PlayerData.class);
        Repository<UUID, PlayerData> database = DatabaseLoader.load(mainConfig, PlayerData.class, defaultDatabase);
        playerDataRepository = new PlayerDataRepository(new LocalRepository<>(), database);
    }

    private void loadPlayerDataForOnlinePlayers() {
        Bukkit.getOnlinePlayers().forEach(player ->
                playerDataRepository.loadFromDatabaseToCache(player.getUniqueId(), new PlayerData(player)));
    }

    private void startPlayerDataSaveTask() {
        playerDataSaveTask = new PlayerDataSaveTask(playerDataRepository);
        if (mainConfig.isDataAutoSaveEnabled()) {
            int interval = mainConfig.getDataAutoSaveInterval() * 60 * 20;
            playerDataSaveTask.runTaskTimer(this, interval, interval);
        }
    }

    private void loadLanguages() {
        LanguageLoader languageLoader = new LanguageLoader(this);
        List<Language> languages = languageLoader.load(mainConfig.getEnabledLanguages());
        Language defaultLanguage = getDefaultLanguage(languages);
        languageAccessor = new LanguageAccessor(languages, defaultLanguage, playerDataRepository.get(TargetRepository.ONE));
        ListenerRegistry.add(new LanguageListener(this));
    }

    private Language getDefaultLanguage(List<Language> languages) {
        return languages.stream()
                .filter(language -> language.getId().equalsIgnoreCase(mainConfig.getDefaultLanguage()))
                .findFirst()
                .orElse(null);
    }

    private void loadInventoryManager() {
        inventoryManager = new InventoryManager(this);
        inventoryManager.init();
        ListenerRegistry.add(new InventoryListener(this));
    }

    private void loadListeners() {
        ListenerRegistry.add(new StandardPickupListener(this));
        ListenerRegistry.registerAll();
    }

    private void loadCommands() {
        CleanInventoryCommand command = new CleanInventoryCommand(this);
        CommandRegistry.register(command);
    }

    private void loadMetrics() {
        new Metrics(this, 24770);
    }

    private void checkForUpdates() {
        getLogger().info("Checking for plugin updates...");
        if (pluginUpdater.isUpdateAvailable(this, 122572)) {
            handlePluginUpdate();
        } else {
            getLogger().info("Your plugin is up to date. No new updates are available");
        }
    }

    private void handlePluginUpdate() {
        if (mainConfig.isAutoUpdateEnabled()) {
            performPluginUpdate();
        } else {
            notifyUserAboutManualUpdate();
        }
    }

    private void performPluginUpdate() {
        getLogger().info("Automatic updates are enabled. Downloading the latest version...");
        pluginUpdater.update(this, 122572);
    }

    private void notifyUserAboutManualUpdate() {
        getLogger().info("Automatic updates are disabled. Visit the following link to download the update:");
        getLogger().info("https://www.spigotmc.org/resources/122572");
    }

    private void savePlayerData() {
        Bukkit.getOnlinePlayers().forEach(player -> playerDataRepository.saveFromCacheToDatabase(player.getUniqueId()));
    }
}
