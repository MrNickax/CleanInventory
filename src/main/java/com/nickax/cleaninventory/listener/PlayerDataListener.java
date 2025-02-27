package com.nickax.cleaninventory.listener;

import com.nickax.cleaninventory.CleanInventory;
import com.nickax.cleaninventory.data.PlayerDataRepository;
import com.nickax.genten.listener.SwitchableListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDataListener extends SwitchableListener {

    private final PlayerDataRepository playerDataRepository;

    public PlayerDataListener(CleanInventory plugin) {
        super(plugin);
        this.playerDataRepository = plugin.getPlayerDataRepository();
    }

    @EventHandler
    private void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        playerDataRepository.loadFromStorageToCache(player);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerDataRepository.saveFromCacheToStorage(player);
    }
}
