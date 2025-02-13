package com.nickax.cleaninventory.listener;

import com.nickax.cleaninventory.CleanInventory;
import com.nickax.cleaninventory.data.PlayerData;
import com.nickax.cleaninventory.item.Item;
import com.nickax.cleaninventory.repository.PlayerDataRepository;
import com.nickax.genten.listener.SwitchableListener;
import com.nickax.genten.repository.dual.TargetRepository;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class InventoryListener extends SwitchableListener {
    
    private final InventoryManager inventoryManager;
    private final PlayerDataRepository playerDataRepository;
    
    public InventoryListener(CleanInventory plugin) {
        super(plugin);
        this.inventoryManager = plugin.getInventoryManager();
        this.playerDataRepository = plugin.getPlayerDataRepository();
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Optional<SmartInventory> optional = inventoryManager.getInventory(player);
        boolean isBlacklistInventoryOpen = optional.map(smartInventory -> smartInventory.getId().equals("blacklist")).orElse(false);
        if (isBlacklistInventoryOpen) {
            Inventory clicked = event.getClickedInventory();
            if (clicked != null && clicked.equals(player.getInventory())) {
                ItemStack itemStack = event.getCurrentItem();
                if (itemStack != null) {
                    Item item = new Item(itemStack);
                    PlayerData playerData = playerDataRepository.get(player.getUniqueId(), TargetRepository.ONE);
                    playerData.addBlackListedItem(item);
                    event.setCancelled(true);
                }
            }
        }
    }
}
