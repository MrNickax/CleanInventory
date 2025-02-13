package com.nickax.cleaninventory.listener;

import com.nickax.cleaninventory.CleanInventory;
import com.nickax.cleaninventory.data.PlayerData;
import com.nickax.cleaninventory.item.Item;
import com.nickax.cleaninventory.repository.PlayerDataRepository;
import com.nickax.genten.inventory.BaseInventory;
import com.nickax.genten.inventory.InventoryRegistry;
import com.nickax.genten.listener.SwitchableListener;
import com.nickax.genten.repository.dual.TargetRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Test extends SwitchableListener {

    private final PlayerDataRepository playerDataRepository;
    
    public Test(CleanInventory plugin) {
        super(plugin);
        this.playerDataRepository = plugin.getPlayerDataRepository();
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
                    Item item = new Item(itemStack);
                    PlayerData playerData = playerDataRepository.get(player.getUniqueId(), TargetRepository.ONE);
                    playerData.addBlackListedItem(item);
                    event.setCancelled(true);
                }
            }
        }
    }
}
