package com.nickax.cleaninventory.data;

import com.nickax.cleaninventory.repository.PlayerDataRepository;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDataSaveTask extends BukkitRunnable {

    private final PlayerDataRepository playerDataRepository;

    public PlayerDataSaveTask(PlayerDataRepository playerDataRepository) {
        this.playerDataRepository = playerDataRepository;
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> playerDataRepository.saveFromCacheToDatabase(player.getUniqueId()));
    }
}