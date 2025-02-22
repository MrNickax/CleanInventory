package com.nickax.cleaninventory;

import com.nickax.cleaninventory.command.CleanInventoryCommand;
import com.nickax.cleaninventory.config.MainConfig;
import com.nickax.cleaninventory.data.PlayerData;
import com.nickax.cleaninventory.data.PlayerDataRepository;
import com.nickax.cleaninventory.data.PlayerDataSaveTask;
import com.nickax.cleaninventory.inventory.ItemBlacklistUpdate;
import com.nickax.cleaninventory.listener.PlayerDataListener;
import com.nickax.cleaninventory.listener.InventoryListener;
import com.nickax.cleaninventory.listener.pickup.LegacyPickupListener;
import com.nickax.cleaninventory.listener.pickup.PickupListener;
import com.nickax.genten.command.CommandRegistry;
import com.nickax.genten.command.model.CommandProperties;
import com.nickax.genten.config.FileConfig;
import com.nickax.genten.credential.DatabaseCredential;
import com.nickax.genten.inventory.BaseInventory;
import com.nickax.genten.inventory.InventoryLogic;;
import com.nickax.genten.inventory.loader.InventoryLoader;
import com.nickax.genten.language.Language;
import com.nickax.genten.language.listener.LanguageListener;
import com.nickax.genten.language.operation.LanguageAccessor;
import com.nickax.genten.language.operation.LanguageLoader;
import com.nickax.genten.library.Libraries;
import com.nickax.genten.library.LibraryLoader;
import com.nickax.genten.listener.ListenerRegistry;
import com.nickax.genten.plugin.PluginUpdater;
import com.nickax.genten.repository.Repository;
import com.nickax.genten.repository.cache.LocalRepository;
import com.nickax.genten.repository.dual.TargetRepository;
import com.nickax.genten.repository.storage.JsonRepository;
import com.nickax.genten.spigot.SpigotVersion;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.UUID;

public final class CleanInventory extends JavaPlugin {

    private final LibraryLoader libraryLoader = new LibraryLoader(this);
    private final SpigotVersion version = SpigotVersion.getCurrent();
    private final MainConfig mainConfig = new MainConfig(this);
    private PlayerDataRepository playerDataRepository;
    private PlayerDataSaveTask playerDataSaveTask;
    private LanguageAccessor languageAccessor;
    private InventoryLogic inventoryLogic;
    private BaseInventory itemBlacklistInventory;

    @Override
    public void onLoad() {
        libraryLoader.load(Libraries.MONGO_JAVA_DRIVER.get());
    }

    @Override
    public void onEnable() {
        SpigotVersion.checkCompatibility(getLogger());
        initializePlugin();
        loadCommands();
        PluginUpdater.checkForUpdates(this, 122572, mainConfig.isAutoUpdateEnabled());
        loadMetrics();
    }

    @Override
    public void onDisable() {
        shutdownPlugin();
    }

    public void reload() {
        shutdownPlugin();
        initializePlugin();
    }

    public PlayerDataRepository getPlayerDataRepository() {
        return playerDataRepository;
    }

    public Repository<UUID, PlayerData> getPlayerDataCache() {
        return playerDataRepository.get(TargetRepository.ONE);
    }

    public LanguageAccessor getLanguageAccessor() {
        return languageAccessor;
    }

    public BaseInventory getItemBlacklistInventory() {
        return itemBlacklistInventory;
    }

    private void initializePlugin() {
        loadMainConfig();
        loadPlayerData();
        loadLanguages();
        loadInventories();
        registerPickupListener();
    }

    private void shutdownPlugin() {
        playerDataSaveTask.cancel();
        playerDataRepository.saveFromCacheToStorage();
        inventoryLogic.disable();
        ListenerRegistry.unregisterAll();
    }

    private void loadMainConfig() {
        mainConfig.load();
        mainConfig.restore();
        mainConfig.save();
    }

    private void loadPlayerData() {
        loadPlayerDataRepository();
        startPlayerDataSaveTask();
        ListenerRegistry.register(new PlayerDataListener(this));
    }

    private void loadPlayerDataRepository() {
        Repository<UUID, PlayerData> storage = loadStorage();
        playerDataRepository = new PlayerDataRepository(new LocalRepository<>(), storage);
        playerDataRepository.loadFromStorageToCache();
    }

    private Repository<UUID, PlayerData> loadStorage() {
        DatabaseCredential credential = mainConfig.getDatabaseCredential();
        return credential != null ? credential.newDatabase(PlayerData.class) : getDefaultStorage();
    }

    private Repository<UUID, PlayerData> getDefaultStorage() {
        return new JsonRepository<>(new File(getDataFolder(), "data/player"), PlayerData.class);
    }

    private void startPlayerDataSaveTask() {
        playerDataSaveTask = new PlayerDataSaveTask(this);
        playerDataSaveTask.start(mainConfig);
    }

    private void loadLanguages() {
        loadLanguageAccessor();
        ListenerRegistry.register(new LanguageListener<>(this, getPlayerDataCache()));
    }

    private void loadLanguageAccessor() {
        List<Language> languages = getLanguages();
        Language defaultLanguage = getDefaultLanguage(languages);
        Repository<UUID, PlayerData> cache = playerDataRepository.get(TargetRepository.ONE);
        languageAccessor = new LanguageAccessor(languages, defaultLanguage, cache);
    }

    private List<Language> getLanguages() {
        LanguageLoader languageLoader = new LanguageLoader(this);
        return languageLoader.load(mainConfig.getEnabledLanguages(), mainConfig.getDefaultLanguage(), "item-black-list-menu");
    }

    private Language getDefaultLanguage(List<Language> languages) {
        return languages.stream()
                .filter(language -> language.getId().equalsIgnoreCase(mainConfig.getDefaultLanguage()))
                .findFirst()
                .orElseGet(() -> handleUnknownDefaultLanguage(languages));
    }

    private Language handleUnknownDefaultLanguage(List<Language> languages) {
        getLogger().warning(String.format("Default language '%s' not found. Using first language.", mainConfig.getDefaultLanguage()));
        getLogger().warning("The plugin cannot generate new message files for unsupported languages as the default language is not defined.");
        return languages.get(0);
    }

    private void loadInventories() {
        FileConfig itemBlacklistConfig = loadItemBlacklistConfig();
        itemBlacklistInventory = loadItemBlackListInventory(itemBlacklistConfig);
        inventoryLogic = new InventoryLogic(this);
        inventoryLogic.enable();
        ListenerRegistry.register(new InventoryListener(this));
    }

    private FileConfig loadItemBlacklistConfig() {
        FileConfig config = new FileConfig(new File(getDataFolder(), "menu/item-blacklist.yml"), getResource("menu/item-blacklist.yml"));
        config.load();
        config.save();
        return config;
    }

    private BaseInventory loadItemBlackListInventory(FileConfig config) {
        InventoryLoader loader = new InventoryLoader(getLogger(), languageAccessor);
        BaseInventory inventory = loader.load(config);
        inventory.setOnUpdate(new ItemBlacklistUpdate(this));
        return inventory;
    }

    private void loadCommands() {
        CommandRegistry commandRegistry = new CommandRegistry();
        CleanInventoryCommand cleanInventoryCommand = new CleanInventoryCommand(this);
        commandRegistry.register(cleanInventoryCommand);
    }

    private void registerPickupListener() {
        if (version.isAtLeast(SpigotVersion.V1_13)) {
            ListenerRegistry.register(new PickupListener(this));
        } else {
            ListenerRegistry.register(new LegacyPickupListener(this));
        }
    }

    private void loadMetrics() {
        new Metrics(this, 24770);
    }
}
