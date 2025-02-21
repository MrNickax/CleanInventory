package com.nickax.cleaninventory.data;

import com.nickax.cleaninventory.CleanInventory;
import com.nickax.cleaninventory.config.MainConfig;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDataSaveTask extends BukkitRunnable {

    private final CleanInventory plugin;
    private final PlayerDataRepository playerDataRepository;

    public PlayerDataSaveTask(CleanInventory plugin) {
        this.plugin = plugin;
        this.playerDataRepository = plugin.getPlayerDataRepository();
    }

    public void start(MainConfig mainConfig) {
        if (mainConfig.isDataAutoSaveEnabled()) {
            int interval = mainConfig.getDataAutoSaveInterval() * 60 * 20;
            runTaskTimer(plugin, interval, interval);
        }
    }

    @Override
    public void run() {
        playerDataRepository.saveFromCacheToStorage();
    }
}