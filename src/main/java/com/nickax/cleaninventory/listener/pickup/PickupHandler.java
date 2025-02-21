package com.nickax.cleaninventory.listener.pickup;

import com.nickax.cleaninventory.CleanInventory;
import com.nickax.cleaninventory.data.PlayerData;
import com.nickax.genten.repository.Repository;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PickupHandler {

    private final Repository<UUID, PlayerData> cache;

    public PickupHandler(CleanInventory plugin) {
        this.cache = plugin.getPlayerDataCache();
    }

    public void handle(Player player, ItemStack pickup, Cancellable event) {
        PlayerData playerData = cache.get(player.getUniqueId());
        if (playerData.containsBlackListedItem(pickup)) {
            event.setCancelled(true);
        }
    }
}
