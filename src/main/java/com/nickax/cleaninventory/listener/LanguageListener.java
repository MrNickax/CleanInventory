package com.nickax.cleaninventory.listener;

import com.nickax.cleaninventory.CleanInventory;
import com.nickax.cleaninventory.data.PlayerData;
import com.nickax.cleaninventory.repository.PlayerDataRepository;
import com.nickax.genten.listener.SwitchableListener;
import com.nickax.genten.repository.dual.TargetRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class LanguageListener extends SwitchableListener {

    private final PlayerDataRepository playerDataRepository;

    public LanguageListener(CleanInventory plugin) {
        super(plugin);
        this.playerDataRepository = plugin.getPlayerDataRepository();
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = playerDataRepository.get(player.getUniqueId(), TargetRepository.ONE);
        if (playerData.getLanguageId() == null) {
            playerData.setLanguageId(player.getLocale());
        }
    }
}
