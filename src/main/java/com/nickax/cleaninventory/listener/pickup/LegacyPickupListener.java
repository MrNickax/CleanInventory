package com.nickax.cleaninventory.listener.pickup;

import com.nickax.cleaninventory.CleanInventory;
import com.nickax.genten.listener.SwitchableListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class LegacyPickupListener extends SwitchableListener {

    private final PickupHandler pickupHandler;

    public LegacyPickupListener(CleanInventory plugin) {
        super(plugin);
        this.pickupHandler = new PickupHandler(plugin);
    }

    @EventHandler
    private void onPlayerPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem().getItemStack();
        pickupHandler.handle(player, item, event);
    }
}
