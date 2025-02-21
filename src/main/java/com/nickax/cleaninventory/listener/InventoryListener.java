package com.nickax.cleaninventory.listener;

import com.nickax.cleaninventory.CleanInventory;
import com.nickax.cleaninventory.data.PlayerData;
import com.nickax.genten.inventory.BaseInventory;
import com.nickax.genten.inventory.InventoryRegistry;
import com.nickax.genten.item.Item;
import com.nickax.genten.listener.SwitchableListener;
import com.nickax.genten.repository.Repository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class InventoryListener extends SwitchableListener {

    private final Repository<UUID, PlayerData> cache;
    
    public InventoryListener(CleanInventory plugin) {
        super(plugin);
        this.cache = plugin.getPlayerDataCache();
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        BaseInventory inventory = InventoryRegistry.getInventory(player);
        if (inventory != null) {
            Inventory clicked = event.getClickedInventory();
            if (clicked != null && clicked.equals(player.getInventory())) {
                ItemStack itemStack = event.getCurrentItem();
                if (itemStack != null) {
                    Item item = Item.fromItemStack(itemStack);
                    PlayerData playerData = cache.get(player.getUniqueId());
                    playerData.addBlackListedItem(item);
                    event.setCancelled(true);
                }
            }
        }
    }
}
