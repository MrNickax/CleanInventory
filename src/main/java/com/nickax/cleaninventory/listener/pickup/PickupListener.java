package com.nickax.cleaninventory.listener.pickup;

import com.nickax.cleaninventory.CleanInventory;
import com.nickax.cleaninventory.data.PlayerData;
import com.nickax.cleaninventory.item.Item;
import com.nickax.cleaninventory.repository.PlayerDataRepository;
import com.nickax.genten.listener.SwitchableListener;
import com.nickax.genten.repository.dual.TargetRepository;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PickupListener extends SwitchableListener {

    private final PlayerDataRepository playerDataRepository;

    public PickupListener(CleanInventory plugin) {
        super(plugin);
        this.playerDataRepository = plugin.getPlayerDataRepository();
    }

    public boolean cancelPickup(Player player, ItemStack pickup) {
        PlayerData playerData = playerDataRepository.get(player.getUniqueId(), TargetRepository.ONE);
        return playerData.containsBlackListedItem(new Item(pickup));
    }
}
